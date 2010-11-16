/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarCounter {

	static VariableCounter variableCounter = new VariableCounter();

	static String getNextRegionVarName() {
		return "Pi" + variableCounter.nextCounter();
	}

}
