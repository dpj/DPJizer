/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package regionparam.uninstantiated;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class C1<region R1> {
	int x in R1;
	
	C1() {}
	
	void m() {
		x = 2;
		new C2<R1>().m();
	}
}

class C2<region R2> {
	int x in R2;
	
	C2() {}
	
	void m() {
		x = 2;
		new C1<R2>().m();
	}
}
