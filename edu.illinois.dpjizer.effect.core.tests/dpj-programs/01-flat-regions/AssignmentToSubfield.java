/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.assignmenttosubfield;

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

class AssignmentToSubfield {
	region r1;
	MyPair p in r1;

	AssignmentToSubfield() {}
	
	void m() {
		p.y = 5;
	}
}