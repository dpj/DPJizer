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
public class TestConstraintCollector09 extends DPJInferencerTestCase {
	@Test
	public void testListRanking() throws Throwable {
		compareCollectedConstraints("ListRanking", new String[] { "DPJUtils", "ListRanking", "Harness" });
	}

	@Override
	protected String getTestDir() {
		return "09-list-ranking/";
	}

}
