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
public class TestConstraintSolver10 extends DPJInferencerTestCase {
	@Test
	public void testMergeSort() throws Throwable {
		compareSolvedConstraints("MergeSort", new String[] { "DPJArrayInt", "DPJPartitionInt", "DPJUtils", "MergeSort", "Harness" });
	}

	@Override
	protected String getTestDir() {
		return "10-mergesort/";
	}

}
