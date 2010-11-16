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
public class TestConstraintSolver15 extends DPJInferencerTestCase {
	@Test
	public void testAB() throws Throwable {
		compareSolvedConstraints("AB", new String[] { "A", "B" });
	}

	@Override
	protected String getTestDir() {
		return "15-overrides-params/";
	}

}
