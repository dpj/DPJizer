/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class A<region R1> {
	A() {
	}

	void m() {

	}
}

class B extends A<Root> {
	region r;
	int a in r;
	int b in Root;

	B() {
	}

	void m() {
		a = 0;
		b = 1;
	}
}