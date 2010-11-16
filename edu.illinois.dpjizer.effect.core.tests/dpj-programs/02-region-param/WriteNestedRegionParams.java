/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package regionparam.writenestedregionparams;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C2<region R2> {
	public int x in R2 = 5;
	
	C2() {}
}

class C1<region R1> {
	region r1;
	C2<r1> c2;

	C1() {}
	
	void m() {
		c2 = new C2<r1>();
		c2.x = 15;
	}
}

