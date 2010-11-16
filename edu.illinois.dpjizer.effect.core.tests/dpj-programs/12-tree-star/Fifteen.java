/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region R1> {
	int []<R1:[i]>#i a in R1;

	public C() {
	}

	void m() {
		if (a[0] == 0)
			new C<R1:[0]>().m();
	}
}