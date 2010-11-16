/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package flatregion.twofield;

import java.util.Date;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class TwoFields {
	region r1, r2;
	Date f1 in r1;
	Date f2 in r2;

	TwoFields() {}
	
	void m1(Date x) /*reads r2, writes r1*/ {
		m2(x);
	}
	
	Date m2(Date x) /*reads r2, writes r1*/ {
		f1 = x;
		return f2;
	}

}
