/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * C.m1 -> C.m2
 * C.m2 -> C.m1 where {X/X:L}
 * C.m2 -> C.m3 
 * 
 * C.m1: X
 * C.m3: X:L
 */

package ten;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region X> {

	int a in X;
	region L;
	int b in X:L;
	C<X:L> node in X:L;

	C() {}

	void m1() {
		if (a == 1) return;
		m2();
	}

	void m2() {
		node.m1();
		m3();
	}

	void m3() {
	}
}