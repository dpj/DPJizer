/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package packages;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class C1 {
	region r1;
	int x in r1;
	
	C1() {}
	
	void m1() {
		x = 3;
		C2 c2 = new C2();
		c2.m2();
	}
}

class C2 {
	region r2;
	int y in r2;
	
	C2() {}
	
	void m2() {
		if (y == 3) {
			C1 c1 = new C1();
			c1.m1();
		}
	}
}