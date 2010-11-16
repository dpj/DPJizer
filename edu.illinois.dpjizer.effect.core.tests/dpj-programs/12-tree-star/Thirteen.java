/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region P1> {
	int x in P1;
	region R1;
	
	C1() {
	}
	
	void m1() {
		x = 0;
		new C2<P1:R1>().m2();
	}
}

class C2<region P2> {
	int y in P2;
	region R2;
	
	C2() {
	}
	
	void m2() {
		y = 0;
		new C1<P2:R2>().m1();
	}
}