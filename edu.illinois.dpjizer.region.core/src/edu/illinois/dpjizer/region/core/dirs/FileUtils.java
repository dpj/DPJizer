/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.dirs;

import java.io.File;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class FileUtils {

	public static void createDirIfNotFound(String dirFullName) {
		File dirObj = new File(dirFullName);
		if (dirObj.exists())
			return;
		boolean createdDir = dirObj.mkdirs();
		if (!createdDir) {
			throw new RuntimeException("Couldn't create the directory " + dirFullName); //$NON-NLS-1$
		}
	}

}
