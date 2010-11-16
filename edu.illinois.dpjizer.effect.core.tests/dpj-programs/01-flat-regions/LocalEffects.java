/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package localeffects;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C {
	region r;
	int x in r;
	
	C() {}
	
	void m() {
		int a;
		a = 3;
		x = 4;
	}
}