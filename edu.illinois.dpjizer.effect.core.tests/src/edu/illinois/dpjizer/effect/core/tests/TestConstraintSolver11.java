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
public class TestConstraintSolver11 extends DPJInferencerTestCase {
	@Test
	public void testStringMatching() throws Throwable {
		compareSolvedConstraints("StringMatching", new String[] { "DPJArrayInt", "DPJPartitionInt", "DPJArrayChar", "DPJPartitionChar", "DPJHashSet",
				"DPJPair", "DPJSet", "DPJSparseArray", "DPJSetWrapper", "DPJIterator", "DPJUtils", "StringMatching", "Harness" });
	}

	@Override
	protected String getTestDir() {
		return "11-string-matching/";
	}

}
