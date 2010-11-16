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
public class TestConstraintSolver16 extends DPJInferencerTestCase {
	@Test
	public void testIDEACrypt() throws Throwable {
		// fail(DEPRECATED_SYNTAX);
		compareSolvedConstraints("IDEACrypt", new String[] { "DPJArrayByte", "DPJPartitionByte", "IDEATest", "JGFCryptBench", "JGFInstrumentor",
				"JGFSection2", "JGFTimer" });
	}

	@Override
	protected String getTestDir() {
		return "16-idea-crypt/";
	}

}
