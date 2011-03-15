/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.transform;

import java.io.PrintWriter;

import com.google.inject.Inject;
import com.sun.tools.javac.code.dpjizer.dirs.Dirs;
import com.sun.tools.javac.tree.Pretty;


/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarWriter extends Transformer {

	Dirs dirs;

	@Inject
	public RegionVarWriter(Dirs dirs) {
		this.dirs = dirs;
	}

	@Override
	String outputDir(String originalSourceFileName) {
		String outputDir = dirs.regionVarsDir(originalSourceFileName);
		return outputDir;
	}

	@Override
	protected Pretty createPrettyPrinter(PrintWriter printWriter) {
		Pretty pretty = new DPJizerPretty(printWriter, true, 0);
		return pretty;
	}

}
