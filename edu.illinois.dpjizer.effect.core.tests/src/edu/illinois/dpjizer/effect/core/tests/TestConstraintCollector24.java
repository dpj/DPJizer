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
public class TestConstraintCollector24 extends DPJInferencerTestCase {
	@Test
	public void testBarnesHutExtraRegion() throws Throwable {
		// fail(DEPRECATED_SYNTAX);
		compareCollectedConstraints("BarnesHutExtraRegion", new String[] { "Body", "HGStruct", "Node", "Tree" });
	}

	@Override
	protected String getTestDir() {
		return "24-barnes-hut-extra-region/";
	}

}
