/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region P1> {
	int a in P1;
	
	public C1() {}
	
	void m1() {
		a = 0;
	}
}

class C2<region P2> extends C1<P2> {
	public C2() {}
	
	void m2() {
		m1();
	}
}