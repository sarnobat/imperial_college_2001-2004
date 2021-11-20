import kenya.io.InputReader;

public class Statistics {

	public static void main(String[] args) {
		
		
		///////////////////////MEAN/////////////////////////////
 
		
		////////////STANDARD DEVIATION//////////////////////////
 
		
		///////////////MEDIAN//////////////////////////////
 
		
		///////////(helper function cumulative totals)////////////
 
		
		//////////(end of helper function)////////////////////////
 
		
		////////////////////////////////////////////////////////
 
		
		///////////////PLOTTING THE GRAPH//////////////////////////
 
		
		/////////////(Helper funciton maxFreq)/////////////////////
 
		
		///////////////(end of helper function)////////////////////
 
		
		//////////////READING IN THE MARKS////////////////////////
 
		
		//////////////Calling the functions////////////////////////
 
		
		////////////////////////////////////////////////////////
 
		main( );
	}
	
  static double  findMean( int []v )
	{
		
		//post: finds the mean of a set of numbers
 
		int i;
		int total = 0;
		int n = 0;
		for( i = 0 ; i < v.length ; i++)
		{
			total = total + i * v[i];
			n = n + v[i];
		}
		double answer = total / n;
		return answer;
		
		// better way: return (double) total/n; but not permitted!
 
	}
	
  static double  findStdDeviation( int []v )
	{
		int i;
		int total = 0;
		int sigmafxsquared = 0;
		double mean = findMean( v);
		for( i = 0 ; i < v.length ; i++)
		{
			sigmafxsquared = sigmafxsquared + i * i * v[i];
			total = total + v[i];
		}
		double answer = Math.sqrt(sigmafxsquared / total - mean * mean);
		return answer;
	}
	
  static int [] totals( int []t )
	{
		
		//post: returns the cumulative totals of an array at each index
 
		int [ ] v = { t[0] , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 };
		int i;
		for( i = 1 ; i < t.length ; i++)
		{
			v[i] = v[i - 1] + t[i];
		}
		return v;
	}
	
  static int  findMedian( int []t )
	{
		int [ ] v = totals( t);
		int j;
		int totalstud = 0;
		for( j = 0 ; j < t.length ; j++)
		{
			totalstud = totalstud + t[j];
		}
		int middlepos = ( totalstud + 1 ) / 2;
		int i = 0;
		while ( middlepos > v[i] )
		{
			i = i + 1;
		}
		return i;
	}
	
  static void  displayMarks( int []v )
	{
		int row;
		int col;
		int maxfreq = maxFreq( v);
		for( col = 0 ; col < v.length ; col++)
		{
			v[col] = v[col] - maxfreq;
		}
		for( row = 0 ; row < maxfreq + 1 ; row++)
		{
			for( col = 0 ; col < v.length ; col++)
			{
				if ( v[col] == 0 )
				{
					System.out.print( "* "  );
				}
				else
				{
					if ( v[col] < 0 )
					{
						System.out.print( "  "  );
					}
					else
					{
						System.out.print( "| "  );
					}
				}
				v[col] = v[col] + 1;
			}
			System.out.println( ""  );
		}
		System.out.println( "0 1 2 3 4 5 6 7 8 9 10"  );
	}
	
  static int  maxFreq( int []v )
	{
		
		//prints the maximum frequency recorded by the array
 
		
		//(do not confuse with the mark represented by this
 
		
		//frequency)
 
		int maxfreq = v[0];
		int i;
		for( i = 1 ; i < v.length ; i++)
		{
			if ( maxfreq < v[i] )
			{
				maxfreq = v[i];
			}
		}
		return maxfreq;
	}
	
  static int [] getMarks( int []marks )
	{
		System.out.println( "Enter your set of marks -->"  );
		while ( !InputReader.isEOF() )
		{
			int mark;
			mark = InputReader.readInt();
			marks[mark] = marks[mark] + 1;
		}
		return marks;
	}
	
  static void  main(  )
	{
		int [ ] marks = { 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 };
		marks = getMarks( marks);
		System.out.println( ""  );
		double mean = findMean( marks);
		System.out.println( "Mean = "  + mean );
		System.out.println( ""  );
		double stdDeviation = findStdDeviation( marks);
		System.out.println( "Standard deviation is "  + stdDeviation );
		System.out.println( ""  );
		int m = findMedian( marks);
		System.out.print( "The median is "  + m );
		System.out.println( ""  );
		System.out.println( ""  );
		displayMarks( marks);
	}
	
}
