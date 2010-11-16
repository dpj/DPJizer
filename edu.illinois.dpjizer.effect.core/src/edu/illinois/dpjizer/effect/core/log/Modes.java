/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.log;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Modes {

	static final String DPJIZER_DEBUG_MODE = "DPJIZER_DEBUG_MODE"; //$NON-NLS-1$

	public static boolean isInDebugMode() {
		return System.getenv(DPJIZER_DEBUG_MODE) != null;
	}

}
