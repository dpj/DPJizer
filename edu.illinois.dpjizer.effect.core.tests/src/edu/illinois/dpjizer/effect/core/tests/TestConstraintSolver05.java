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
public class TestConstraintSolver05 extends DPJInferencerTestCase {
	@Test
	public void testSumArray() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testArrayRegions() throws Throwable {
		compareSolvedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "05-arrays/";
	}

}
