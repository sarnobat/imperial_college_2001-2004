package ic.doc.simulation.tools ;

public abstract class OrderedList extends List {

  ublic abstract boolean before( Object x, Object y ) ;

  public void insert( Object o ) {
    ListIterator it = this.getIterator() ;
    while ( it.canAdvance() && before( it.getValue(), o ) ) {
      it.advance() ;
    }
    it.add( o ) ;
  }

}




