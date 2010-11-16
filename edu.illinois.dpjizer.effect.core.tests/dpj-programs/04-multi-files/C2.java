/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package multifiles;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C2 {
	region r2;
	int b in r2;
	
	C2(int b) {
		this.b = b;
	}
	
	void m2() {
		b = b * 2;
		C1 c1 = new C1();
		c1.m1();
	}
}