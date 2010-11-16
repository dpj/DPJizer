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
public class TestConstraintCollector25 extends DPJInferencerTestCase {
	@Test
	public void testMontecarlo() throws Throwable {
		compareCollectedConstraints("CollisionTreeExtraRegion", new String[] { "CollisionTreeExtraRegion" });
	}

	@Override
	protected String getTestDir() {
		return "25-collision-tree-extra-region/";
	}

}
