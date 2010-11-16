/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.dirs;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public interface Dirs {

	String getLogDirName();

	String getConstraintsFileName();

	String regionVarsDir(String originalSourceFileName);

	String withMethodParamsDir(String originalSourceFileName);

}
