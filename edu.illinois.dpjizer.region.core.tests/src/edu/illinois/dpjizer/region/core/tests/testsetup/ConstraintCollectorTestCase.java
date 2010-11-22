/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.testsetup;

import edu.illinois.dpjizer.utils.Logger;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public abstract class ConstraintCollectorTestCase extends DPJInferencerTestCase {

	protected void collectConstraints(String[] testFilePaths) {
		compilerInvoker.collectConstraints(inputFilePaths(testFilePaths));
	}

	protected void collectConstraints() {
		String name = convertTestNameToFileName();
		Logger.log("Running test " + name + " of " + getTestDir() + "...");
		String[] inputFiles = new String[] { name };
		collectConstraints(inputFiles);
		compareRegionVarFiles(inputFiles);
	}

	protected void compareRegionVarFiles(String[] filePaths) {
		String[] actualFiles = dpjizerTestDirs.actualRegionVarPaths(filePaths, getTestDir());
		String[] expectedFiles = dpjizerTestDirs.expectedRegionVarPaths(filePaths, getTestDir());
		fileComparator.compareFiles(actualFiles, expectedFiles);
	}

}
