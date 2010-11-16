/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.equality;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class NullEquality {
	public static boolean onlyOneIsNull(Object first, Object second) {
		return ((first == null && second != null) || (first != null && second == null));
	}
}
