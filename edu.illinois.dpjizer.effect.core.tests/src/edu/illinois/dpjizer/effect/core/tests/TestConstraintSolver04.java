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
public class TestConstraintSolver04 extends DPJInferencerTestCase {

	@Test
	public void testC1C2() throws Throwable {
		compareSolvedConstraints("C1C2", new String[] { "C1", "C2" });
	}

	@Override
	protected String getTestDir() {
		return "04-multi-files/";
	}
}
