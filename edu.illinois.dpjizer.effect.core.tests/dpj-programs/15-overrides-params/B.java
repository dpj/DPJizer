/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package overridesparams;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class B<region Rb> extends A<Rb> {
	B() {}

	@Override
	void m() {
		y = 2;
	}
}