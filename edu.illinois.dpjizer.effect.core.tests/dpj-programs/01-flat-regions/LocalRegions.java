/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package localregions;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1 { 
	public C1() {}

	void m1() {
		region r;
		new C2<r>().m2();
	}

}

class C2<region R2> {
	public C2() {}

	int y in R2;

	void m2() {
		y = 1;
	}
}
