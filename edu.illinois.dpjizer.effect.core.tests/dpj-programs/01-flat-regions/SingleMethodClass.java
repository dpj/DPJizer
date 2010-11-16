/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.singlemethodclass;

import java.util.Date;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class SingleMethodClass {
	region r1;
	Date f1 in r1;

	SingleMethodClass() {}
	
	void m1(Date x) /*writes r1*/ {
		f1 = x;
	}
}
