/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.aliased;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class MyPair {
	int x;
	int y;
	
	MyPair() {}
}

class AliasedField {
	region r1;
	MyPair p in r1;
	
	AliasedField() {}
	
	void m() {
		MyPair localP = p;
		localP.x = 3;
	}
}
