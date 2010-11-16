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
public class TestConstraintSolver13 extends DPJInferencerTestCase {
	@Test
	public void testOne() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testTwo() throws Throwable {
		compareSolvedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "13-method-region-params/";
	}

}
