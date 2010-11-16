/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package regionparam.regionparams;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class C1<region R1> {
	region r1;
	int x in R1;
	
	C1() {}
	
	void m() {
		x = 2;
		new C2<r1>().m();
	}
}

class C2<region R2> {
	region r2;
	int x in R2;
	
	C2() {}
	
	void m() {
		x = 2;
		new C1<r2>().m();
	}
}

