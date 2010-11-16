/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package two;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class C<region X> {
	region L;
	region Q;
	C<X:L> left in X:L;

	C() {}

	void m1() {
		left.m2();
	}

	void m2() {
		new C<Q>().m1();
	}
}