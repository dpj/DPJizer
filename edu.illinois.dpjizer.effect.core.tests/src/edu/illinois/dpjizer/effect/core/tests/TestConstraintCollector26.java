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
public class TestConstraintCollector26 extends DPJInferencerTestCase {
	@Test
	public void testCollisionTreeNontermination1() throws Throwable {
		compareCollectedConstraints("CollisionTreeNontermination1", new String[] { "CollisionTreeNontermination1" });
	}

	@Test
	public void testCollisionTreeNontermination2() throws Throwable {
		compareCollectedConstraints("CollisionTreeNontermination2", new String[] { "CollisionTreeNontermination2" });
	}

	@Test
	public void testCollisionTreeNontermination3() throws Throwable {
		compareCollectedConstraints("CollisionTreeNontermination3", new String[] { "CollisionTreeNontermination3" });
	}

	@Override
	protected String getTestDir() {
		return "26-collision-tree-nontermination/";
	}

}
