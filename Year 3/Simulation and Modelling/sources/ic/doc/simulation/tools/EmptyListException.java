package ic.doc.simulation.tools ;

public class EmptyListException extends R timeException {
   public EmptyListException( String name )
   {
      super( "The " + name + " is empty" );
   }
}

