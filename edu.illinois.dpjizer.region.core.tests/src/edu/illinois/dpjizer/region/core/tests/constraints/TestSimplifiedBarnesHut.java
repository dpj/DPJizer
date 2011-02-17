/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.tests.constraints;

import org.junit.Test;

import edu.illinois.dpjizer.region.core.tests.testsetup.ConstraintCollectorTestCase;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class TestSimplifiedBarnesHut extends ConstraintCollectorTestCase {

	/**
	 * Expected method effect summaries:
	 * 
	 * SETV reads VR, reads Pi2[VR <- Pi3][idx1 <- ?], reads Pi3, writes
	 * Pi2[idx1 <- ?]
	 * 
	 * hackgrav reads BR, reads Pi5, invokes
	 * <>SETV(simplifiedbarneshut.Vector<Pi3>) with [reads VR[VR <- Pi4, SR <-
	 * SPi1], reads Pi2[VR <- Pi3][idx1 <- ?][VR <- Pi4, SR <- SPi1], reads
	 * Pi3[VR <- Pi4, SR <- SPi1], writes Pi2[idx1 <- ?][VR <- Pi4, SR <- SPi1]]
	 * 
	 * computegrav reads Pi7[idx3 <- ?], invokes
	 * hackgrav(simplifiedbarneshut.HGStruct<Pi5>) with [reads BR[BR <- Pi6[idx3
	 * <- ?]], reads HR[HR <- Pi5][HR <- Pi5][BR <- Pi6[idx3 <- ?]], invokes
	 * <>SETV(simplifiedbarneshut.Vector<Pi3>) with [reads VR[VR <- Pi3][VR <-
	 * Pi3][VR <- Pi4][SR <- SPi1][BR <- Pi6[idx3 <- ?]], reads VR[VR <- Pi4][SR
	 * <- SPi1][BR <- Pi6[idx3 <- ?]], writes Pi2[idx1 <- ?][VR <- Pi4][SR <-
	 * SPi1][BR <- Pi6[idx3 <- ?]], reads Pi2[VR <- Pi3][idx1 <- ?][VR <-
	 * Pi4][SR <- SPi1][BR <- Pi6[idx3 <- ?]]]], reads Root
	 * 
	 */
	@Test
	public void testSimplifiedBarnesHut() {
		collectConstraints();
	}
}
