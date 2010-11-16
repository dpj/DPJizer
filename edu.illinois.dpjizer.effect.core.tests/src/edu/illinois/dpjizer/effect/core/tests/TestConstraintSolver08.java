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
public class TestConstraintSolver08 extends DPJInferencerTestCase {
	@Test
	public void testQuickSort() throws Throwable {
		compareSolvedConstraints("QuickSort", new String[] { "Partition", "DPJUtils", "DPJArrayInt", "DPJPartitionInt", "Quicksort" });
	}

	@Override
	protected String getTestDir() {
		return "08-quicksort/";
	}

}
