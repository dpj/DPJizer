/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C {
	region R1;
	C c1 in R1;
	
	void C() {
		c1 = new C();
	}
	
	void m() {
		c1.m();
	}
}