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
public class TestConstraintCollector21 extends DPJInferencerTestCase {
	@Test
	public void testCollisionTree() throws Throwable {
		compareCollectedConstraints("CollisionTree", new String[] { "com/jme/bounding/BoundingBox", "com/jme/bounding/BoundingVolume",
				"com/jme/bounding/CollisionTree", "com/jme/intersection/Intersection", "com/jme/math/Matrix3f", "com/jme/math/Quaternion",
				"com/jme/math/Vector2f", "com/jme/math/Vector3f", "com/jme/scene/TriMesh", "com/jme/scene/shape/Sphere",
				"com/jme/util/ParallelArrayList", "com/jmex/effects/cloth/CollidingClothPatch", "jmetest/collisiontree/BenchmarkCollisionTree" });
	}

	@Override
	protected String getTestDir() {
		return "21-collision-tree/";
	}

}
