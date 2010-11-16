/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.dirs;

import java.io.File;

import edu.illinois.dpjizer.region.core.tests.plugin.Messages;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class DPJizerTestDirs extends DPJizerDirs {

	private static final String DPJ_PROGRAMS_SUBDIR = "dpj-programs" + File.separator; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String ECLIPSE_PROJECT_NAME = Messages.Dirs_PluginId;

	public static final String EXPECTED_METHOD_PARAMS_DIR = "params" + File.separator + "expected"; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String EXPECTED_REGION_VARS_DIR = "region-vars" + File.separator + "expected"; //$NON-NLS-1$ //$NON-NLS-2$

	private static final String ACTUAL_METHOD_PARAMS_DIR = "params" + File.separator + "actual"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String ACTUAL_REGION_VARS_DIR = "region-vars" + File.separator + "actual"; //$NON-NLS-1$ //$NON-NLS-2$

	public String absoultePathOf(String filePath) {
		String curdir = new File(".").getAbsolutePath(); //$NON-NLS-1$
		String target = ECLIPSE_PROJECT_NAME + File.separator;
		int index = curdir.lastIndexOf(target);
		if (index < 0)
			throw new Error("Cannot locate directory " + DPJ_PROGRAMS_SUBDIR); //$NON-NLS-1$
		int len = index + target.length();
		String dir = curdir.substring(0, len) + DPJ_PROGRAMS_SUBDIR;
		return dir + filePath;
	}

	public String[] testFilesPaths(String[] filePaths, String testDir) {
		String[] absoluteFilePaths = new String[filePaths.length];
		for (int i = 0; i < absoluteFilePaths.length; i++) {
			String relativePath = testDir + File.separator + filePaths[i] + ".java"; //$NON-NLS-1$
			absoluteFilePaths[i] = absoultePathOf(relativePath);
		}
		return absoluteFilePaths;
	}

	public static String[] outputFilePaths(String[] filePaths, String outputDir) {
		String[] annotatedFilepaths = new String[filePaths.length];
		for (int i = 0; i < filePaths.length; ++i) {
			File file = new File(filePaths[i]);
			String absoluteOutputDir = file.getParent() + File.separator + outputDir;
			new File(absoluteOutputDir).mkdir();
			annotatedFilepaths[i] = absoluteOutputDir + File.separator + file.getName();
		}
		return annotatedFilepaths;
	}

	public String[] actualParamPaths(String[] filePaths, String testDir) {
		return testFilesPaths(filePaths, testDir + File.separator + ACTUAL_METHOD_PARAMS_DIR);
	}

	public String[] actualRegionVarPaths(String[] filePaths, String testDir) {
		return testFilesPaths(filePaths, testDir + File.separator + ACTUAL_REGION_VARS_DIR);
	}

	public String[] expectedParamsFilePaths(String[] filePaths) {
		return outputFilePaths(filePaths, EXPECTED_METHOD_PARAMS_DIR);
	}

	public String[] expectedParamPaths(String[] filePaths, String testDir) {
		return testFilesPaths(filePaths, testDir + File.separator + EXPECTED_METHOD_PARAMS_DIR);
	}

	public String[] expectedRegionVarPaths(String[] filePaths, String testDir) {
		return testFilesPaths(filePaths, testDir + File.separator + EXPECTED_REGION_VARS_DIR);
	}

}
