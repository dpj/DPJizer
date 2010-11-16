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
public class TestConstraintCollector27 extends DPJInferencerTestCase {
	@Test
	public void testMergeSort4() throws Throwable {
		compareCollectedConstraints("MergeSort4", new String[] { "DPJArrayInt", "DPJPartitionInt", "DPJUtils", "MergeSort", "MergeSort4", "Harness" });
	}

	@Override
	protected String getTestDir() {
		return "27-mergesort4/";
	}

}
