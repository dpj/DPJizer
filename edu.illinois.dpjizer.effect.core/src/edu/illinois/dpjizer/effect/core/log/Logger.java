/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.log;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class Logger {

	public static void log(String message) {
		if (Modes.isInDebugMode()) {
			System.out.println(message);
		}
	}

}
