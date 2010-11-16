/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package directrecursion;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class C {
	region r;
	int a in r;
	
	C() {}
	
	void m() {
		a = 1;
		m();
	}
}