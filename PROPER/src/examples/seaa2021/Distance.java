package seaa2021;

import org.apache.commons.math3.exception.DimensionMismatchException;

public class Distance {
	/**
	 * Calculates the L<sub>1</sub> (sum of abs) distance between two points.
	 *
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the L<sub>1</sub> distance between the two points
	 * @throws DimensionMismatchException if the array lengths differ.
	 */
	public static int distance1(int[] p1, int[] p2)
			throws DimensionMismatchException {
		// L1 distance is used for ML 
		if (checkEqualLength(p1, p2) == false) {
			throw new DimensionMismatchException(p1.length, p2.length); //@cost=5
		}
		else {
			int sum = 0;
			int i = 0;
			while (i < p1.length) {
				sum += Math.abs(p1[i] - p2[i]); //@time=2.5
				i++;
			}
			return sum;
		}
	}

	//below is the method check equals 

	public static boolean checkEqualLength(int[] a,
			int[] b) {
		if (a.length == b.length) {
			return true;
		} else {
			return false;
		}
	}

}
