/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.testsetup;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public abstract class DPJInferencerTestCase extends DependencyInjector {

	protected static final String NON_TERMINATING = "non-terminating";
	protected static final String IGNORED = "ignored";

	@Rule
	public TestName name = new TestName();

	protected String getTestDir() {
		String fullClassName = this.getClass().getName();
		String[] nameParts = fullClassName.split("\\.");
		String simpleName = nameParts[nameParts.length - 1];
		return simpleName.substring("Test".length());
	}

	/**
	 * 
	 * @see {@link http://tinyurl.com/2bnodbl}
	 * 
	 */
	protected String convertTestNameToFileName() {
		String testName = name.getMethodName();
		Assert.assertTrue(testName.startsWith("test"));
		String name = testName.substring("test".length());
		return name;
	}

	protected String[] inputFilePaths(String[] filePaths) {
		return dpjizerTestDirs.testFilesPaths(filePaths, getTestDir());
	}

	// @Override
	// protected void tearDown() throws Exception {
	// super.tearDown();
	// new File(DPJizerDirs.DPJ_PROGRAMS_SUBDIR +
	// "solved-constraints.txt").delete();
	// new File(DPJizerDirs.DPJ_PROGRAMS_SUBDIR + "constraints.txt").delete();
	// }
}
