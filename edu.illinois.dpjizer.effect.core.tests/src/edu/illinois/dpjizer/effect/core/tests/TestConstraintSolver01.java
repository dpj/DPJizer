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
public class TestConstraintSolver01 extends DPJInferencerTestCase {
	@Test
	public void testSingleMethodClass() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testSimpleMethodEffects() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testTwoFields() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testRecursion() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testCallChain() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testAliasedField() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testAssignmentToSubfield() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testUseBeforeDecl() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testMultipleInvocations() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testDirectRecursion() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testLocalEffects() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testReadsReceiver() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testLocalRegions() throws Throwable {
		compareSolvedConstraints();
	}

	@Test
	public void testLocalRegionInclusion() throws Throwable {
		printDisabledTestMessage("Local is deprecated. See https://github.com/dpj/DPJ/issues/1");
		// compareSolvedConstraints();
	}

	@Override
	protected String getTestDir() {
		return "01-flat-regions/";
	}

}
