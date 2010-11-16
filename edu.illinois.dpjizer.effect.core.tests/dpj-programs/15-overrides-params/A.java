/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package overridesparams;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class A<region R> {
	int x in R;
	int y in R;

	A() {}

	void m() {
		x = 1;
	}
}
