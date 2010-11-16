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

class B<region R2> extends A<R2> {
	int a in R2;

	B() {
	}

	void m() {
		a = 0;
	}
}