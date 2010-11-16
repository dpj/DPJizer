/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Some utility methods
 * @author Robert L. Bocchino Jr.
 * June 2008
 */

package DPJRuntime;

public class DPJUtils {
	public DPJUtils() {}

	/**
	 * Sequentially compute the log base 2 of x
	 */
	public static int log2(int x) 
	//	pure 
	{
		if (x <= 0) throw new ArithmeticException();
		int result = 0;
		while (x > 1) {
			x >>= 1;
			++result;
		}
		return result;
	}

	/**
	 * Utility method:  swap two values in array A.
	 */
	public static <type T, region R> void swap(T[]<R> A, int i, int j) 
	//	writes R 
	{
		T tmp = A[j];
		A[j] = A[i];
		A[i] = tmp;
	}

	/**
	 * Randomly permute an array
	 */
	public static <type T, region R> void permute(T[]<R> A) {
		for (int i = 0; i < A.length; ++i) {
			int j = (int) (Math.random() * A.length);
			int k = (int) (Math.random() * A.length);
			DPJUtils.swap(A, j, k);
		}
	}

	public static <region R> void permuteInt(int[]<R> A) {
		for (int i = 0; i < A.length; ++i) {
			int j = (int) (Math.random() * A.length);
			int k = (int) (Math.random() * A.length);
			int tmp = A[j];
			A[j] = A[i];
			A[i] = tmp;
		}
	}

	/**
	 * Print an array
	 */
	public static <type T, region R> void print(T[]<R> A) {
		for (T t : A) {
			System.out.print(t + " ");
		}
	}
	public static <region R> void print(int[]<R> A) {
		for (int t : A) {
			System.out.print(t + " ");
		}
	}
}

