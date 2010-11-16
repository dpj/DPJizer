/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class C<region P> {
	public C() {
	}

	void m() 
	//	reads this:*
	{
		final C<this:[i]>[]<this:[i]>#i zz = null;

		zz[0].m();
	}
}