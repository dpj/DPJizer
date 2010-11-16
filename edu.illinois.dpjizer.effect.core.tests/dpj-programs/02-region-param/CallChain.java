/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package regionparam.callchain;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region R> {
	int x in R;
	
	C1() {}
	
	C2<R> m1() {
		x = 1;
		return new C2<R>();
	}
}

class C2<region R> {
	int y in R;
	
	C2() {}
	
	void m2() {
		y = 2;
	}
}

class C3 {
	region r3;
	
	C3() {}
	
	void m3() {
		C1<r3> c1 = new C1<r3>();
		c1.m1().m2();
	}
}