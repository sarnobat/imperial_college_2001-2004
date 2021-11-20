import java.text.DecimalFormat;

/*
 * Created on 13-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class IDCT {
	private static double pi = 3.14159265359;

	public static void main(String[] args) {
		/*decompress(DCT.getCoeffs(DCT.ai));
		decompress(DCT.getCoeffs(DCT.aii));
		decompress(DCT.getCoeffs(DCT.aiii));
		decompress(DCT.getCoeffs(DCT.aiv));*/
		double[][] bii = { { 10, 0, -10, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				-10, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }, {
				0, 0, 0, 0, 0, 0, 0, 0, }
		};
		decompress(bii);
	}

	private static void decompress(double[][] dctMatrix) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				double f = f(x, y, dctMatrix);
				System.out.print(trimNum(f) + "\t");
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}
	private static double f(int x, int y, double[][] F) {
		double acc = 0;
		for (int v = 0; v < 8; v++) {
			for (int u = 0; u < 8; u++) {
				acc += c(u) * c(v) * F[u][v] * Math.cos((2 * x + 1) * u * pi / 16) * Math.cos((2 * y + 1) * v * pi / 16);
			}
		}
		return acc * 1 / 4;
	}
	private static double c(int n) {
		if (n == 0) {
			return 1 / (Math.sqrt(2));
		}
		else {
			return 1;
		}

	}
	public static String trimNum(double d) {
		if (d == 0) {
			return "0";
		}
		else {

			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(d);
		}
	}
}
