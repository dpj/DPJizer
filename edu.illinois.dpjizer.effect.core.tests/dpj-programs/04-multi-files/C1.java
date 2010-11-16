/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package multifiles;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1 {
	region r1;
	int a in r1;
	
	C1() {
		this.a = 0;
	}
	
	void m1() {
		a = a * 4;
		C2 c2 = new C2(a);
		c2.m2();
	}
}