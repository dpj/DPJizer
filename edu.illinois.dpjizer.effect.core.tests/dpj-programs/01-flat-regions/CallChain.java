/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.callchain;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1 {
	region r1, r3;
	int x in r1;
	C2 c2 in r3;
	
	C1() {}
	
	C2 m1() {
		x = 1;
		c2 = new C2();
		return c2;
	}
}

class C2 {
	region r2;
	int y in r2;
	
	C2() {}
	
	void m2() {
		y = 2;
	}
}

class C3 {
	
	C3() {}
	
	void m3() {
		C1 c1 = new C1();
		c1.m1().m2();
	}
}