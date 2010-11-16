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
public class TestConstraintSolver18 extends DPJInferencerTestCase {
	@Test
	public void testOne() throws Throwable {
		compareSolvedConstraints("One", new String[] { "One" });
	}

	@Test
	public void testTwo() throws Throwable {
		compareSolvedConstraints("Two", new String[] { "Two" });
	}

	@Test
	public void testThree() throws Throwable {
		compareSolvedConstraints("Three", new String[] { "Three" });
	}

	@Test
	public void testFour() throws Throwable {
		printDisabledTestMessage("Tries to replace Root by region symbol R1");
		// compareSolvedConstraints("Four", new String[] { "Four" });
	}

	@Test
	public void testFive() throws Throwable {
		compareSolvedConstraints("Five", new String[] { "Five" });
	}

	@Override
	protected String getTestDir() {
		return "18-method-overrides/";
	}

}
