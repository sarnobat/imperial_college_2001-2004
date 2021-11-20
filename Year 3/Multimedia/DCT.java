import java.text.DecimalFormat;

/*
 * Created on 09-Feb-2004
 *  
 */

/**
 * @author ss401
 *  
 */
public class DCT {
	static double pi = 3.14159265359;

	public static int[][] ai = { { 16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }, {
			16, 16, 16, 16, 16, 16, 16, 16 }
	};
	public static int[][] aii = { { 24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }, {
			24, 22, 18, 15, 11, 8, 6, 5 }
	};
	public static int[][] aiii = { { 24, 24, 24, 24, 24, 24, 24, 24 }, {
			22, 22, 22, 22, 22, 22, 22, 22 }, {
			18, 18, 18, 18, 18, 18, 18, 18 }, {
			15, 15, 15, 15, 15, 15, 15, 15 }, {
			11, 11, 11, 11, 11, 11, 11, 11 }, {
			8, 8, 8, 8, 8, 8, 8, 8 }, {
			6, 6, 6, 6, 6, 6, 6, 6 }, {
			5, 5, 5, 5, 5, 5, 5, 5 }
	};
	public static int[][] aiv = { { 25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }, {
			25, 15, 5, 15, 25, 15, 5, 15 }
	};
	public static void main(String[] args) {
		/*getCoeffs(ai);
		System.out.println();
		getCoeffs(aii);
		System.out.println();
		getCoeffs(aiii);
		System.out.println();
		getCoeffs(aiv);*/

		/*int[][] biv = { { 0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }
		};*/
		int[][] biv = { 
			    { 0, 0, 0, 0, 0, 0, -5, -10, }, {
				0, 0, 0, 0, 0, -5, -10, -5, }, {
				0, 0, 0, 0, -5, -10, -5, 0, }, {
				0, 0, 0, -5, -10, -5, 0, 0, }, {
				0, 0, -5, -10, -5, 0, 0, 0, }, {
				0, -5, -10, -5, 0, 0, 0, 0, }, {
				-5, -10, -5, 0, 0, 0, 0, 0, }, {
				-10, -5, 0, 0, 0, 0, 0, 0, }
		};

		getCoeffs(biv);

		/*int u = 4;
		int v =4;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
					System.out.print(IDCT.trimNum(10*(Math.cos((2 * x + 1) * u * pi / 16) * Math.cos((2 * y + 1) * v * pi / 16))) + " ");
			}
			System.out.println();
			
		}*/
	}

	public static double[][] getCoeffs(int[][] block) {
		double[][] coeffs = new double[8][8];
		for (int u = 0; u < 8; u++) {
			for (int v = 0; v < 8; v++) {
				coeffs[u][v] = dctCoeff(u, v, block);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
		return coeffs;
	}

	private static double dctCoeff(int u, int v, int[][] f) {

		double acc = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				//Note: be careful with array referencing. The y indexes the primary array and the
				//x indexes the secondary arrays
				acc += f[y][x] * Math.cos((2 * x + 1) * u * pi / 16) * Math.cos((2 * y + 1) * v * pi / 16);
			}
		}
		double result = acc * norm(u, v);
		DecimalFormat df = new DecimalFormat("0.000");

		String displayedNum = df.format(result);
		if (displayedNum.equals("-0.000")) {
			displayedNum = "0.000";
		}
		System.out.print(displayedNum);

		return result;
	}
	private static double norm(int u, int v) {
		if (u == 0 && v == 0) {
			return (1.0 / 8.0);
		}
		else if (u == 0 || v == 0) {
			return 1.0 / (4.0 * Math.sqrt(2));
		}
		else {
			return 1.0 / 4.0;
		}
	}

}
