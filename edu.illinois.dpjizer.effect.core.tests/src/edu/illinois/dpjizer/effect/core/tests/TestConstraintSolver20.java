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
public class TestConstraintSolver20 extends DPJInferencerTestCase {
	@Test
	public void testBarnesHut() throws Throwable {
		compareSolvedConstraints("BarnesHut", new String[] { "Node", "BarnesHut", "Body", "Cell", "Constants", "HGStruct", "SlaveStart", "Tree",
				"Util", "Vector" });
	}

	@Override
	protected String getTestDir() {
		return "20-barnes-hut/";
	}

}
