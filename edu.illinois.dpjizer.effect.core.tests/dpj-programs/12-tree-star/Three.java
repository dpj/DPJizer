/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package three;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region X> {
	region L;
	region Q;
	C<X:L> left in X:L;
	int a in X;

	C() {}

	void m1() {
		left.m2();
	}

	void m2() {
		a = 3;
		new C<Q>().m1();
	}
}