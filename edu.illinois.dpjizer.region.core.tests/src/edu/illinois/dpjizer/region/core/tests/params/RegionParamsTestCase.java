/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.params;

import edu.illinois.dpjizer.region.core.tests.dirs.DPJizerTestDirs;
import edu.illinois.dpjizer.region.core.tests.dirs.FileComparator;
import edu.illinois.dpjizer.region.core.tests.testsetup.DPJInferencerTestCase;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionParamsTestCase extends DPJInferencerTestCase {

	DPJizerTestDirs dpjizerTestDirs;
	FileComparator fileComparator;

	@Deprecated
	protected void compareRegionVarFiles(String[] filePaths) {
		String[] actualFiles = dpjizerTestDirs.actualRegionVarPaths(filePaths, getTestDir());
		String[] expectedFiles = dpjizerTestDirs.expectedRegionVarPaths(filePaths, getTestDir());
		fileComparator.compareFiles(actualFiles, expectedFiles);
	}

}
