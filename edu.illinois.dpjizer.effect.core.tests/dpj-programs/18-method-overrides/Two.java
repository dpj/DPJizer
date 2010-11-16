/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class A<region R1> {
	A() {
	}

	void m(int j) {

	}
}

class B<region R2> extends A<R2> {
	int[]<<this>>#i a in R2;

	B() {
	}

	void m(int i) {
		a[0] = 0;
	}
}