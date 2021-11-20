package MIPS;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CycleManager
{
  public CycleManager()
  {
    MemoryManager mm = new MemoryManager(10000,20000,100000);
    /*
    long beg = System.currentTimeMillis();
    System.out.println(mm.store(10,123));
    long end = System.currentTimeMillis();
    System.out.println(mm.store(10100,456));
    System.out.println(mm.store(30000,789));
    System.out.println(mm.store(200000,111));
    System.out.println("store time in millis(beg/end/diff): "+end+" "+beg+" "+(end-beg));
    beg = System.currentTimeMillis();
    System.out.println(mm.retrieve(11));
    end = System.currentTimeMillis();
    System.out.println(mm.retrieve(200000));
    System.out.println(mm.retrieve(10100));
    System.out.println(mm.retrieve(30000));
    System.out.println("store time in millis(beg/end/diff): "+end+" "+beg+" "+(end-beg));
    */
    int key,value;
    long beg,end;
    int total_s_time = 0;
    int total_r_time = 0;

    int total_items = 1000;


    for (int i = 0; i<=total_items; i++)
    {
      key = Math.round( (float) Math.random() * 100000);
      value = Math.round( (float) Math.random() * 100000);

      // store

      beg = System.currentTimeMillis();
      mm.store(key, value);
      end = System.currentTimeMillis();
      total_s_time+=(end-beg);
      System.out.println("KEY:" + key + " VALUE:" + value + " stored in "+(end-beg)+" millis");

      // retrieve

      beg = System.currentTimeMillis();
      value = mm.retrieve(key);
      end = System.currentTimeMillis();
      total_r_time+=(end-beg);
      System.out.println("KEY:" + key + " VALUE:" + value + " retrieved in "+(end-beg)+" millis");

    }

    System.out.println("For "+total_items+" iterations, total store time = "+total_s_time);
    System.out.println("                    av store time = "+(total_s_time/total_items));


    System.out.println("For "+total_items+" iterations, total retrieval time = "+total_r_time);
    System.out.println("                    av retrieval time = "+(total_r_time/total_items));

    /* memory dump */

    for (int i = 0; i<=100000; i++)
    {
      System.out.println("ADDRESS: "+i+" VALUE: "+mm.retrieve(i));
    }

  }
  public static void main(String[] args)
  {
    CycleManager cycleManager1 = new CycleManager();
  }

}