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
	public static <type T, region R4> void swap(T[]<R4> A, int i, int j) 
	//	writes R 
	{
		T tmp = A[j];
		A[j] = A[i];
		A[i] = tmp;
	}

	/**
	 * Randomly permute an array
	 */
	public static <type T, region R5> void permute(T[]<R5> A) {
		for (int i = 0; i < A.length; ++i) {
			int j = (int) (Math.random() * A.length);
			int k = (int) (Math.random() * A.length);
			DPJUtils.swap(A, j, k);
		}
	}

	public static <region R6> void permuteInt(int[]<R6> A) {
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
	public static <type T, region R7> void print(T[]<R7> A) {
		for (T t : A) {
			System.out.print(t + " ");
		}
	}
	public static <region R8> void print(int[]<R8> A) {
		for (int t : A) {
			System.out.print(t + " ");
		}
	}
}

