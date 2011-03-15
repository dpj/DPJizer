/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.dirs;

import java.io.File;

import com.sun.tools.javac.code.dpjizer.dirs.Dirs;


/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerDirs implements Dirs {

	private static final String TEST_OUTPUT_DIR = "out"; //$NON-NLS-1$
	private static final String CONSTRAINTS_FILE = TEST_OUTPUT_DIR + File.separator + "constraints.txt"; //$NON-NLS-1$

	private static final String ACTUAL_METHOD_PARAMS_DIR = "params" + File.separator + "actual"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String ACTUAL_REGION_VARS_DIR = "region-vars" + File.separator + "actual"; //$NON-NLS-1$ //$NON-NLS-2$

	private String outputDir(String originalSourceFileName, String outputDir) {
		String[] fileNameParts = originalSourceFileName.split(File.separator);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fileNameParts.length - 1; i++) {
			sb.append(fileNameParts[i]);
			sb.append(File.separator);
		}
		sb.append(outputDir + File.separator);
		// sb.append(fileNameParts[fileNameParts.length - 1]);
		String annotatedFileName = sb.toString();
		return annotatedFileName;
	}

	@Override
	public String withMethodParamsDir(String originalSourceFileName) {
		return outputDir(originalSourceFileName, ACTUAL_METHOD_PARAMS_DIR);
	}

	@Override
	public String regionVarsDir(String originalSourceFileName) {
		return outputDir(originalSourceFileName, ACTUAL_REGION_VARS_DIR);
	}

	@Override
	public String getLogDirName() {
		return TEST_OUTPUT_DIR;
	}

	@Override
	public String getConstraintsFileName() {
		return CONSTRAINTS_FILE;
	}
}
