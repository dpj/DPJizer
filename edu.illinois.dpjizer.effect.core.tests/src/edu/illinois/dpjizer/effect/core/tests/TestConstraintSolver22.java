/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.tests;

import org.junit.Test;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class TestConstraintSolver22 extends DPJInferencerTestCase {
	@Test
	public void testThisWithNoRegionParam() throws Throwable {
		compareSolvedConstraints("ThisWithNoRegionParam", new String[] { "ThisWithNoRegionParam" });
	}

	@Test
	public void testThisOutOfScope() throws Throwable {
		compareSolvedConstraints("ThisOutOfScope", new String[] { "ThisOutOfScope" });
	}

	@Test
	public void testCompilerRegionInferenceBug() throws Throwable {
		printDisabledTestMessage("Owner regions are not substituted correctly. See https://github.com/dpj/DPJ/issues/2");
		// compareSolvedConstraints("CompilerRegionInferenceBug", new String[] {
		// "CompilerRegionInferenceBug" });
	}

	@Test
	public void testExplicitMethodRegionParam() throws Throwable {
		compareSolvedConstraints("ExplicitMethodRegionParam", new String[] { "ExplicitMethodRegionParam" });
	}

	@Override
	protected String getTestDir() {
		return "22-ownership/";
	}

}
