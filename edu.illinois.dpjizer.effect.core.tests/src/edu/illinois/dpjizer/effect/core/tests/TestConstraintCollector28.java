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
public class TestConstraintCollector28 extends DPJInferencerTestCase {
	@Test
	public void testMergeSort8() throws Throwable {
		compareCollectedConstraints("MergeSort8", new String[] { "DPJArrayInt", "DPJPartitionInt", "DPJUtils", "MergeSort", "MergeSort8", "Harness" });
	}

	@Override
	protected String getTestDir() {
		return "28-mergesort8/";
	}

}
