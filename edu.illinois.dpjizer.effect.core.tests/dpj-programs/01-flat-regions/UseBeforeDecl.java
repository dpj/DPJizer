/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.usebefore;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C {
	region r1;
	MyPair p in r1;

	C() {}
	
	void m() {
		p.y = 5;
	}
}

class MyPair {
	int x;
	int y;
	
	MyPair() {}
}