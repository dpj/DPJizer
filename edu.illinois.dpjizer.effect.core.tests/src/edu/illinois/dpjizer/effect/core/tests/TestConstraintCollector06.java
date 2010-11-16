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
public class TestConstraintCollector06 extends DPJInferencerTestCase {
	@Test
	public void testSumReduce() throws Throwable {
		compareCollectedConstraints("SumReduce", new String[] { "Harness", "SumReduce", "DPJArrayInt" });
	}

	@Override
	protected String getTestDir() {
		return "06-sum-reduce/";
	}

}
