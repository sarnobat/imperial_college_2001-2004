package ic.doc.simulation.tools ;

public class Centre {
  private Server server = new Server() ;
  private Queue queue = new Queue() ;
  private DistributionSampler dist ;

  public Centre( DistributionSampler d ) {
    dist = d ;
    server.activate() ;
  }

  public void enter( SimProcess p ) throws InterruptedException {
    queue.enqueue( p ) ;
    if ( !server.isActive() )
      server.activate() ; 
    p.passivate() ;
  } 

  class Server extends SimProcess {
    public void runProcess() throws InterruptedException {
      while ( true ) {
        while ( queue.queueLength() > 0 ) {
          hold( dist.next() ) ;
          ( (SimProcess) queue.dequeue() ).activate() ;
        }
        passivate() ;
      }
    }
  } 

  public double meanQueueLength() {
    return queue.meanQueueLength() ;
  }

  public double meanTimeInQueue() {
    return queue.meanTimeInQueue() ;
  }

  public double utilisation() {
    return server.utilisation() ;
  }

  public void reset() {
    server.reset() ;
    queue.reset() ;
  }

}
