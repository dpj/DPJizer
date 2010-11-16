/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package multipleinvocations;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C {
	region r1, r2;
	int x in r1;
	int y in r2;
	
	C() {}
	
	void m1() {
		x = 2;
	}
	
	void m2() {
		y = 2;
	}
	
	void m3() {
		m1();
		m2();
	}
}