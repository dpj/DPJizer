/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package one;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1 {
	region R1, R2, R3;
	C2<R1> c2 in R2;
	int a in R3;

	C1() {}

	<region X> void m1(C2<region X> c2) {
		a = 1;
		c2.m3();
	}

	void m2() {
		c2 = new C2<region R1>();
		m1(c2);
	}
}

class C2<region Y> {
	int b in Y;

	C2() {}

	void m3() {
		b = 2;
	}
}
