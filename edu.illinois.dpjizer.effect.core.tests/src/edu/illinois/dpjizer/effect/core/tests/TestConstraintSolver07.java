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
public class TestConstraintSolver07 extends DPJInferencerTestCase {
	@Test
	public void testQuadTree() throws Throwable {
		compareSolvedConstraints("Quadtree", new String[] { "Harness", "DPJIndexedSet", "Quadtree", "DPJIterator" });
	}

	@Override
	protected String getTestDir() {
		return "07-quad-tree/";
	}

}
