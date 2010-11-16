/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package methodregionparams;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C2 {
	C2() {}

	<region R2> int m2(C1<R2> c1, int level) {
		c1.a = 6;
		return c1.a;
	}

	<region R3> int m2(C1<R3> c1) {
		return m2(new C1<R3>(c1), 1);
	}
}