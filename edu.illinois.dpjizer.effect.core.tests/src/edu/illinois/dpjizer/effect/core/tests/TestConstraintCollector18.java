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
public class TestConstraintCollector18 extends DPJInferencerTestCase {
	@Test
	public void testOne() throws Throwable {
		compareCollectedConstraints("One", new String[] { "One" });
	}

	@Test
	public void testTwo() throws Throwable {
		compareCollectedConstraints("Two", new String[] { "Two" });
	}

	@Test
	public void testThree() throws Throwable {
		compareCollectedConstraints("Three", new String[] { "Three" });
	}

	@Test
	public void testFour() throws Throwable {
		compareCollectedConstraints("Four", new String[] { "Four" });
	}

	@Test
	public void testFive() throws Throwable {
		compareCollectedConstraints("Five", new String[] { "Five" });
	}

	@Override
	protected String getTestDir() {
		return "18-method-overrides/";
	}

}
