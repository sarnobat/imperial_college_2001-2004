// CPU model - this time using process interaction

import ic.doc.simulation.tools.* ;

class CPUPSim extends PSim {
  Centre cpu    = new Centre( new Exp( 1000.0/5 ) ),
         disk[] = { new Centre( new Exp( 1000.0/30 ) ),
                    new Centre( new Exp( 1000.0/27 ) ) 
                  } ;

  static CustomerMeasure respTime = new CustomerMeasure() ; 

  class Arrivals extends SimProcess {
    DistributionSampler iaTime ;
    public Arrivals( DistributionSampler d ) {
      super() ;
      iaTime = d ;
    }
    public void runProcess() throws InterruptedException {
      while ( true ) {
        hold( iaTime.next() ) ;
        new Job().activate() ;
      }
    }
  }

  class Job extends SimProcess {
    public void runProcess() throws InterruptedException {
      double arrTime = now() ;
      cpu.enter( this ) ;
      while ( Math.random() > 1.0/121 ) {
        int i = (int) ( Math.random() + 50.0/120 ) ;
        disk[i].enter( this ) ; 
        cpu.enter( this ) ;
      }
      respTime.add( now() - arrTime ) ;
    }
  }

  public boolean stop() {
    return respTime.count() == 100 ;
  }

  public CPUPSim( double lambda ) throws InterruptedException {
    new Arrivals( new Exp( lambda ) ).activate() ;
    execute() ;
    System.out.println( respTime.mean() ) ;
  }
}

public class pcpu {
  public static void main( String args[] ) throws InterruptedException {
    double lambda = Double.parseDouble( args[0] ) ;
    new CPUPSim( lambda ) ;
  }
}

