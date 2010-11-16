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
public class TestConstraintSolver21 extends DPJInferencerTestCase {
	@Test
	public void testCollisionTree() throws Throwable {
		compareSolvedConstraints("CollisionTree", new String[] { "com/jme/bounding/BoundingBox", "com/jme/bounding/BoundingVolume",
				"com/jme/bounding/CollisionTree", "com/jme/intersection/Intersection", "com/jme/math/Matrix3f", "com/jme/math/Quaternion",
				"com/jme/math/Vector2f", "com/jme/math/Vector3f", "com/jme/scene/TriMesh", "com/jme/scene/shape/Sphere",
				"com/jme/util/ParallelArrayList", "com/jmex/effects/cloth/CollidingClothPatch"/*
																							 * ,
																							 * "jmetest/collisiontree/BenchmarkCollisionTree"
																							 */});
	}

	@Test
	public void testCollisionTreeMath() throws Throwable {
		compareSolvedConstraints("CollisionTreeMath", new String[] { "com/jme/math/Matrix3f", "com/jme/math/Quaternion", "com/jme/math/Vector2f",
				"com/jme/math/Vector3f" });
	}

	@Test
	public void testCollisionTreeIntersection() throws Throwable {
		compareSolvedConstraints("CollisionTreeIntersection", new String[] { "com/jme/math/Matrix3f", "com/jme/math/Quaternion",
				"com/jme/math/Vector2f", "com/jme/math/Vector3f", "com/jme/intersection/Intersection" });
	}

	@Test
	public void testCollisionTreeScene() throws Throwable {
		compareSolvedConstraints("CollisionTreeScene", new String[] { "com/jme/math/Matrix3f", "com/jme/math/Quaternion", "com/jme/math/Vector2f",
				"com/jme/math/Vector3f", "com/jme/scene/shape/Sphere", "com/jme/scene/TriMesh" });
	}

	@Test
	public void testCollisionTreeBounding() throws Throwable {
		compareSolvedConstraints("CollisionTreeBounding", new String[] { "com/jme/bounding/BoundingVolume", "com/jme/bounding/BoundingBox",
				"com/jme/math/Matrix3f", "com/jme/math/Quaternion", "com/jme/math/Vector2f", "com/jme/math/Vector3f", "com/jme/scene/shape/Sphere",
				"com/jme/scene/TriMesh", "com/jme/util/ParallelArrayList" });
	}

	@Override
	protected String getTestDir() {
		return "21-collision-tree/";
	}

}
