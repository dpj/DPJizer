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
public class TestConstraintSolver26 extends DPJInferencerTestCase {
	@Test
	public void testCollisionTreeNontermination1() throws Throwable {
		compareSolvedConstraints("CollisionTreeNontermination1", new String[] { "CollisionTreeNontermination1" });
	}

	@Test
	public void testCollisionTreeNontermination2() throws Throwable {
		compareSolvedConstraints("CollisionTreeNontermination2", new String[] { "CollisionTreeNontermination2" });
	}

	@Test
	public void testCollisionTreeNontermination3() throws Throwable {
		compareSolvedConstraints("CollisionTreeNontermination3", new String[] { "CollisionTreeNontermination3" });
	}

	@Override
	protected String getTestDir() {
		return "26-collision-tree-nontermination/";
	}

}
