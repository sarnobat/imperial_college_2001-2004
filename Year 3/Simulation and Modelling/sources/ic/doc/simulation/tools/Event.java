package ic.doc.simulation.tools ;

public abstract class Event {
  double time ;
  public Event( double t ) {
   time = Time.vtime + t ;
    Sim.diary.insert( this ) ;
  }

  public abstract void call() ;

}


