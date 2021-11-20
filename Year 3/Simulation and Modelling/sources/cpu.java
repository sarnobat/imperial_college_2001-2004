// CPU model - event scheduling version as in the notes

import ic.doc.simulation.tools.* ;

class CPUSim extends Sim {
  Queue cpuQ = new Queue(),
        diskQ[] = { new Queue(), new Queue() } ;
  Exp iaTime,
      sTime[] = { new Exp( 1000.0/30 ),
                  new Exp( 1000.0/27 ) },
      xTime = new Exp( 1000.0/5 ) ;

  Measure respTime = new Measure() ;

  class Arrive extends Event {
    public Arrive( double t ) {
      super( t ) ;
    }
    public void call() {
      new Arrive( iaTime.next() ) ;
      cpuQ.enqueue( new Double( now() ) ) ;
      if ( cpuQ.queueLength() == 1 )
        new StopJob( xTime.next() ) ;
    }
  }

  class StopJob extends Event {
    public StopJob( double t ) {
      super( t ) ;
    }

    public void call() {
      Double t = (Double) cpuQ.dequeue() ;
      if ( Math.random() <= 1.0/121 ) {
        respTime.add( now() - t.doubleValue() ) ;
      }
      else {
        int i = (int) ( Math.random() + 50.0/120 ) ;
        diskQ[i].enqueue( t ) ;
        if ( diskQ[i].queueLength() == 1 )
          new PagedIn( i, sTime[i].next() ) ;
      }
      if ( cpuQ.queueLength() > 0 )
        new StopJob( xTime.next() ) ;
    }
  }

  class PagedIn extends Event {
    int diskNum ;

    public PagedIn( int disk, double t ) { 
      super( t ) ;
      diskNum = disk ; 
    }

    public void call() {
      Double t = (Double) diskQ[diskNum].dequeue() ;
      cpuQ.enqueue( t ) ;
      if ( cpuQ.queueLength() == 1 ) 
        new StopJob( xTime.next() ) ; 
      if ( diskQ[diskNum].queueLength() > 0 )
        new PagedIn( diskNum, sTime[diskNum].next() ) ;
    }
  }

  public boolean stop() {
    return now() > 30000.0 ;
  }

  public CPUSim( double lambda ) {
    iaTime = new Exp( lambda ) ;
    new Arrive( iaTime.next() ) ;
    execute() ;
    System.out.println( respTime.mean() ) ;
  }
}

public class cpu {
  public static void main( String args[] ) {
    double lambda = Double.parseDouble( args[0] ) ;
    new CPUSim( lambda ) ;
  }

}


