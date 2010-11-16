/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package six;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region X> {
	region L;
	region Q;
	int a in X;

	C() {}

	void m1() {
		a = 1;
		new C<Q>().m2();
	}

	void m2() {
		a = 2;
		new C<X:L>().m3();
	}

	void m3() {
		a = 3;
		m1();
	}
}