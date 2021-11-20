package ic.doc.simulation.tools ;

public abstract class PSim {
  static ProcList procList = new ProcList() ;
  static Semaphore managerSem = new Semaphore() ;

  public abstract boolean stop() ;

  static class ProcList extends OrderedList {
     public boolean before( Object x, Object y ) {
        return ( ( (SimProcess) x ).wakeTime <= ( (SimProcess) y ).wakeTime ) ;
     }
  }

  public double now() {
    return Time.vtime ;
  }

  public void execute() throws InterruptedException {
    while ( !stop() ) {
      SimProcess p = (SimProcess)( procList.removeFromFront() ) ;
      Time.vtime = p.wakeTime ;
      p.processSem.up() ;
      managerSem.down() ;
    }
    SimProcess.allThreads.interrupt() ;
  }

}






