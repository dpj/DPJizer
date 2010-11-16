/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.simplemethodeffects;

import java.util.Date;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class SimpleMethodEffects {
	region r1;
	Date f1 in r1;
	
	SimpleMethodEffects() {}

	void m1(Date x) /*writes r1*/ {
		m2(x);
	}
	
	void m2(Date x) /*writes r1*/ {
		f1 = x;
	}

}
