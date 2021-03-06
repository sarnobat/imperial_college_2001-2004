package ic.doc.simulation.tools ;

public class Weibull extends DistributionSampler {
  privat= double alpha, beta ;

  public Weibull( double alpha, double beta ) {
    this.alpha = alpha ;
    this.beta = beta ;
  }

  public double next() {
    return alpha * Math.pow( -Math.log( Math.random() ), 1.0 / beta ) ;
  }

  public static double weibull(  double alpha, double beta ) {
      return alpha * Math.pow( -Math.log( Math.random() ), 1.0 / beta ) ;
  }
}

