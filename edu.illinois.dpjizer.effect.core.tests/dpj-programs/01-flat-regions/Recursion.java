/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.recursion;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C {
	region r1;
	region r2;
	int a in r1;
	int b in r2;

	C() {}
	
	void m1() {
		a = 1;
		m2();
	}
	
	void m2() {
		b = 2;
		m1();
	}
}