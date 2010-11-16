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
public class TestConstraintCollector22 extends DPJInferencerTestCase {
	@Test
	public void testThisWithNoRegionParam() throws Throwable {
		compareCollectedConstraints("ThisWithNoRegionParam", new String[] { "ThisWithNoRegionParam" });
	}

	@Test
	public void testThisOutOfScope() throws Throwable {
		compareCollectedConstraints("ThisOutOfScope", new String[] { "ThisOutOfScope" });
	}

	@Test
	public void testCompilerRegionInferenceBug() throws Throwable {
		compareCollectedConstraints("CompilerRegionInferenceBug", new String[] { "CompilerRegionInferenceBug" });
	}

	@Test
	public void testExplicitMethodRegionParam() throws Throwable {
		compareCollectedConstraints("ExplicitMethodRegionParam", new String[] { "ExplicitMethodRegionParam" });
	}

	@Override
	protected String getTestDir() {
		return "22-ownership/";
	}

}
