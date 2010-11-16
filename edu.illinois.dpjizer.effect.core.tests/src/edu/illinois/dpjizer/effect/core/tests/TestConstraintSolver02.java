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
public class TestConstraintSolver02 extends DPJInferencerTestCase {
	// @Test
	// public void testRegionParams() throws Throwable {
	// compareSolvedConstraints();
	// }

	@Test
	public void testUninstantiated() throws Throwable {
		compareSolvedConstraints();
	}

	// @Test
	// public void testReadNestedRegionParams() throws Throwable {
	// compareSolvedConstraints();
	// }
	//
	// @Test
	// public void testWriteNestedRegionParams() throws Throwable {
	// compareSolvedConstraints();
	// }
	//
	// @Test
	// public void testCallChain() throws Throwable {
	// compareSolvedConstraints();
	// }

	@Override
	protected String getTestDir() {
		return "02-region-param/";
	}

}
