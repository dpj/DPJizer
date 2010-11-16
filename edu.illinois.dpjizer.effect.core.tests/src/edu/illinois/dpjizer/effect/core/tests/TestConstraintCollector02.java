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
public class TestConstraintCollector02 extends DPJInferencerTestCase {
	@Test
	public void testRegionParams() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testUninstantiated() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testReadNestedRegionParams() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testWriteNestedRegionParams() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testCallChain() throws Throwable {
		compareCollectedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "02-region-param/";
	}

}
