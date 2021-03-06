package ic.doc.simulation.tools ;

public class Deterministic extends DistributionSamp r {
  private double time ;

  public Deterministic( double t ) {
    time = t ;
  }

  public double next() {
    return time ;
  }

  public static double deterministic( double t ) {
    return t ;
  }
}
