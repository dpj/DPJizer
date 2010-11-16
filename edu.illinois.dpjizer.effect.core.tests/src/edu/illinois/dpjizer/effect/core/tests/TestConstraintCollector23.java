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
public class TestConstraintCollector23 extends DPJInferencerTestCase {
	@Test
	public void testMontecarlo() throws Throwable {
		// fail(DEPRECATED_SYNTAX);
		compareCollectedConstraints("Montecarlo", new String[] { "AppDemo", "CallAppDemo", "DemoException", "JGFInstrumentor", "JGFMonteCarloBench",
				"JGFTimer", "MonteCarloPath", "PathId", "PriceStock", "RatePath", "ReturnPath", "ToInitAllTasks", "ToResult", "ToTask", "Universal",
				"Utilities" });
	}

	@Override
	protected String getTestDir() {
		return "23-montecarlo/";
	}

}
