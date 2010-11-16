/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * C1.m1 -> C2.m2 where {X/Y}
 * C2.m2 -> C1.m1 where {Y/X:L}
 * 
 * C1.m1: X
 * 
 * If we go follow the transitions C1.m1->C2.m2->C1.m1, C1.m1 gets a new effect on X:L.
 * The cycle is a growing cycle and each time we follow it we get a new effect.
 * If C2.m2 had an effect on X, it would have been a growing cycle. 
 */

package nine;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region X> {
	int a in X;
	region L;

	C1() {}

	void m1() {
		a = 1;
		new C2<X:L>().m2();
	}
}

class C2<region Y> {

	C2() {}

	void m2() {
		new C1<Y>().m1();
	}
}