// Plots five sample distributions

import ic.doc.simulation.tools.* ;

public class distplot {
  static void plot ( DistributionSampler s,  Histogram h ) {
    for ( int i = 1 ; i <= 1000000 ; i++ ) {
      h.add( s.next() ) ;
    }
    h.display() ;
  }

  public static void main( String args[] ) {
    plot( new Gamma( 1, 3 ), new Histogram( 0, 3, 10 ) ) ;
    plot( new Exp( 1 ), new Histogram( 0, 5, 10 ) ) ;
    plot( new Normal( 5, 1.5 ), new Histogram( 0, 10, 10 ) ) ;
    plot( new Uniform( 2, 7 ), new Histogram( 0, 10, 10 ) ) ;
    plot( new Weibull( 1, 2 ), new Histogram( 0, 3, 10 ) ) ;
  }
}


