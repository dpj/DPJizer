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
public class TestConstraintCollector17 extends DPJInferencerTestCase {
	@Test
	public void testMethodOverloads() throws Throwable {
		compareCollectedConstraints("MethodOverloads", new String[] { "C1", "C2" });
	}

	@Override
	protected String getTestDir() {
		return "17-method-overloads/";
	}

}
