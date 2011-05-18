/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.sun.tools.javac.code.dpjizer.constraints.RegionVarEltSymbol;

/**
 * 
 * TODO: I need to replace
 * {@link edu.illinois.dpjizer.region.core.types.RegionVarEltSymbol.numIDs} by
 * an instance of this class.
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarCounter {

	VariableCounter variableCounter;

	public RegionVarCounter() {
		this.variableCounter = new VariableCounter();
	}

	String getNextRegionVarName() {
		return "Pi" + variableCounter.nextCounter();
	}

	public void reset() {
		variableCounter.reset();
	}

}
