/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class A {
	region Rx, Ry;
	int x in Rx;
	int y in Ry;
	
	A() {}
	
	void m() {
		x = 1;
	}
}

class B extends A {
	B() {}
	
	@Override
	void m() {
		y = 2;
	}
}
