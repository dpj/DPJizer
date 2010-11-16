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
public class TestConstraintCollector19 extends DPJInferencerTestCase {
	@Test
	public void testKMeans() throws Throwable {
		compareCollectedConstraints("KMeans", new String[] { "Cluster", "CommonUtil", "KMeans", "Normal", "Point", "PointPool", "RandomType" });
	}

	@Override
	protected String getTestDir() {
		return "19-kmeans/";
	}

}
