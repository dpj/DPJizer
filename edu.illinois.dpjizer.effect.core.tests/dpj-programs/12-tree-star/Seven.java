/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * C.m2 -> C.m1 where {X/X}
 * C.m3 -> C.m2 where {X/X:L}
 * C.m1 -> C.m3 where {X/Q}
 * 
 * C.m1: X
 * C.m2: 
 */
package seven;

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
		new C<X>().m2();
	}

	void m2() {
		a = 2;
		new C<X:L>().m3();
	}

	void m3() {
		a = 3;
		new C<Q>().m1();
	}
}