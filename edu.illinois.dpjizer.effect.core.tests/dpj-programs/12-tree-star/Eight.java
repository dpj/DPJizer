/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * C2.m2 -> C1.m1 where {Y/X}
 * C2.m1 -> C2.m2 where {X/Y:L} 
 */
package eight;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region X> {
	int a in X;

	C1() {}

	void m1() {
		a = 1;
		new C2<X>().m2();
	}

}

class C2<region Y> {
	region L;
	int b in Y;

	C2() {}

	void m2() {
		b = 2;
		new C1<Y:L>().m1();
	}
}