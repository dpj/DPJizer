/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1 {
	int a in this;
	
	C1() {
	}
	
	void m1() {
		a = 1;
	}
}

class C2 {
	C2() {
	}
	
	void m2() {
		C1 c1 = new C1();
		c1.m1();
	}
}