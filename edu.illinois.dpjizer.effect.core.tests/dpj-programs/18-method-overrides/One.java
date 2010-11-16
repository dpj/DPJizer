/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package methodoverrides;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class A<region R1, R2> {
	A() {
	}

	void m1() {

	}
}

class B<region R3> extends A<R3, R3> {
	int a in R3;

	B() {
	}

	void m1() {
		a = 1;
	}
}