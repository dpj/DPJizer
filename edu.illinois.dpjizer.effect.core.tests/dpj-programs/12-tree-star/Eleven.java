/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package eleven;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region X> {
	region R1, R2, R3;
	int a in X;

	C() {}

	void m1() {
		a = 1;
		new C<X:R1>().m1();
		new C<X:R2>().m2();
	}

	void m2() {
		new C<X:R3>().m1();
	}
}