package ic.doc.simulation.tools ;

public class Semaphore {
  private int value = 0 ;

  synch nized public void up() {
    value++ ;
    notify() ;
  }

  synchronized public void down() throws InterruptedException {
    while ( value == 0 )  wait() ;
    value-- ;
  }
}
