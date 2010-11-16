/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.tests;

import org.junit.Test;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class TestConstraintCollector01 extends DPJInferencerTestCase {
	@Test
	public void testSingleMethodClass() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testSimpleMethodEffects() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testTwoFields() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testRecursion() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testCallChain() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testAliasedField() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testAssignmentToSubfield() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testUseBeforeDecl() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testMultipleInvocations() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testDirectRecursion() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testLocalEffects() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testReadsReceiver() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testLocalRegions() throws Throwable {
		compareCollectedConstraints();
	}

	@Test
	public void testLocalRegionInclusion() throws Throwable {
		printDisabledTestMessage("Local is deprecated. See https://github.com/dpj/DPJ/issues/1");
		// compareCollectedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "01-flat-regions/";
	}

}
