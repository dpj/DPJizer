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
public class TestConstraintSolver17 extends DPJInferencerTestCase {
	@Test
	public void testMethodRegionParams() throws Throwable {
		compareSolvedConstraints("MethodOverloads", new String[] { "C1", "C2" });
	}

	@Override
	protected String getTestDir() {
		return "17-method-overloads/";
	}

}
