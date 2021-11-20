package ic.doc.simulation.tools ;

class Diary extends OrderedList {

  public boolean before( Object x, Objec y ) {
    return ( ((Event)x).time <= ((Event)y).time ) ;
  }
}

public abstract class Sim {

static Diary diary = new Diary() ;

public double now() {
  return Time.vtime ;
}

public abstract boolean stop() ;

public void execute() {
  while ( !stop() ) {
    Event e =
      (Event) diary.removeFromFront() ;
    Time.vtime = e.time ;
    e.call() ;
  }
}


}








