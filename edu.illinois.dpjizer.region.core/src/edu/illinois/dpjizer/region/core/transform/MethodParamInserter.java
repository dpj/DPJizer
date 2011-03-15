/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.transform;

import com.google.inject.Inject;
import com.sun.tools.javac.code.dpjizer.dirs.Dirs;


/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class MethodParamInserter extends Transformer {

	Dirs dirs;

	@Inject
	public MethodParamInserter(Dirs dirs) {
		super();
		this.dirs = dirs;
	}

	@Override
	String outputDir(String originalSourceFileName) {
		return dirs.withMethodParamsDir(originalSourceFileName);
	}

}
