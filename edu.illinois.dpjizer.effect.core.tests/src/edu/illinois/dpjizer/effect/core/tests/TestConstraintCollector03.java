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
public class TestConstraintCollector03 extends DPJInferencerTestCase {
	@Test
	public void testC1() throws Throwable {
		compareCollectedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "03-packages/";
	}

}
