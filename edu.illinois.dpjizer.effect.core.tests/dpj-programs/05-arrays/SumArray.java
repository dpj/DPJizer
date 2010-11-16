/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package arrays;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class ArraySummer<region R1, R2> {
	private int[]<<R1:[i]>>#i a in R2;
	
	public ArraySummer(int[]<R1:[i]>#i a) {
	}

	int sumArray() {
		int sum = 0;
		for (int j = 0; j < a.length; j++) 
			sum += a[j];
		return sum;
	}
	
	region r1, r2;
	
	// The compiler fails to compile when main() is made 'static'.
	int[]<r1:[i]>#i x in r2;
	public void main(String args[]) {
		x = new int[5]<r1:[i]>#i;
		x[0] = 1;
		x[1] = 2;
		x[2] = 3;
		x[3] = 4;
		x[4] = 5;
		ArraySummer<r1, r2> arraySummer = new ArraySummer<r1, r2>(x);
		System.out.println(arraySummer.sumArray());
	}
}