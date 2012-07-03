/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

/**
 * 
 * @author Mohsen Vakilian
 * @author Alexandros Tzannes
 * 
 */
public class IndexVarCounter {

	static VariableCounter variableCounter = new VariableCounter();
	static final String indexPrefix= "idx";
	
	public static String getNextIndexVarName() {
		return indexPrefix + variableCounter.nextCounter();
	}

}
