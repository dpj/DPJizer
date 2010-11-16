/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region R1> {
	int a in R1;
	
	public C1() {
	}
	
	void m1(C3<R1> c3) {
//		a = 0;
		c3.m3();
	}
}

class C2 {
	C1<[i]>[]#i c1s;
	
	public C2() {
	}

	void m2() {
		C3<[0]> c3 = null;
		c1s[0].m1(c3);
	}
}

class C3<region R3> {
	int b in R3;

	public C3() {
	}
	
	void m3() {
		b = 0;
	}
}