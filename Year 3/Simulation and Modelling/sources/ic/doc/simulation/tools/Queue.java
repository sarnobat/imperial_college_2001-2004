package ic.doc.simulation.tools ;

public class Queue {
  private int pop = 0 ;
  private CustomerMeasure responseTimeMeasure = new CustomerMeasure() ;
  private SystemMeasure popMeasure = new SystemMeasure() ;
  private List q = new List() ;

  public int queueLength() {
    return pop ;
  }

  public void enqueue( Object o ) {
    pop++ ;
    popMeasure.add( (float)pop ) ;
    q.insertAtBack( new QueueEntry( o ) ) ;
  }

  public Object dequeue() {
    if ( pop == 0 ) System.out.println( "ERROR: Attempt to dequeue an empty Queue!" ) ;
    pop-- ;
    popMeasure.add( (float)pop ) ;
    QueueEntry e = (QueueEntry) q.removeFromFront() ;
    responseTimeMeasure.add( Time.vtime - e.entryTime ) ;
    return e.entry ;
  }

  public Object front() {
    return ( (QueueEntry) q.first() ).entry ;
  }

  public boolean isEmpty() {
    return ( pop == 0 ) ;
  }

  public double meanQueueLength() {
    return popMeasure.mean() ;
  }

  public double varQueueLength() {
    return popMeasure.variance() ;
  }

  public double meanTimeInQueue() {
    return responseTimeMeasure.mean() ;
  }
  public double varTimeInQueue() {
      return responseTimeMeasure.variance() ;
    }

  public void reset() {
    responseTimeMeasure.reset() ;
    popMeasure.reset() ;
  }

//---------------------------------------------------

  class QueueEntry {
    double entryTime ;
    Object entry ;

    public QueueEntry( Object o ) {
      entryTime = Time.vtime ;
      entry = o ;
    }
  }

}




