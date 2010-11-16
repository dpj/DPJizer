/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package methodregionparams;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C1<region R1> {
	int a in R1;

	C1() {}

	C1(C1<R1> c1)  {
		this.a = c1.a;
	}

	void m1() {
		a = 1;
	}
}