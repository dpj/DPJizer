/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package five;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region X> {
	region Q;
	C<X> node in X;
	int a in X;

	C() {}

	void m1() {
		node.m2();
	}

	void m2() {
		a = 3;
		new C<X>().m1();
	}
}