/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class IndexVarCounter {

	static VariableCounter variableCounter = new VariableCounter();

	public static String getNextIndexVarName() {
		return "idx" + variableCounter.nextCounter();
	}

}
