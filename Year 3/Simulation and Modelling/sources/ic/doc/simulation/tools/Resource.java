package ic.doc.simulation.tools ; 

public class Resource {
  boolean busy ;
  SystemMeasure utilisationMeasure = new SystemMeasure() ;

  public Resource() {
    busy = false ;
  }

  public void claim() {
    if ( busy ) System.out.println( "ERROR: Attempt to claim a busy Resource!" ) ;
    busy = true ;
    utilisationMeasure.add( 1.0 ) ;
  }

  public void release() {
    if ( !busy ) System.out.println( "ERROR: Attempt to release an idle Resource!" ) ;
    busy = false ;
    utilisationMeasure.add( 0.0 ) ;
  } 

  public boolean isBusy() {
    return busy ;
  } 

  public double utilisation() {
    return utilisationMeasure.mean() ;
  }

  public void reset() {
    utilisationMeasure.reset() ;
  }

}


