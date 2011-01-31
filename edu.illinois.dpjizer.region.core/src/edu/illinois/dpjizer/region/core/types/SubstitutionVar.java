/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

/**
 * 
 * RPLs of types of the actual arguments of methods should be inside those of
 * the formal parameters. The resulting RPL inclusion constraint will contain an
 * unknown substitution that is represented by an instance of this class.
 * 
 * @author Mohsen Vakilian
 * 
 */
public class SubstitutionVar /*implements Substitution*/ {

	private static class SubstitutionVarCounter {
		static final VariableCounter variableCounter = new VariableCounter();

		public static String getNextSubstitutionVarName() {
			return "sigma" + variableCounter.nextCounter();
		}
	}

	private final String name;

	public SubstitutionVar() {
		super();
		this.name = SubstitutionVarCounter.getNextSubstitutionVarName();
	}

	@Override
	public String toString() {
		return name;
	}

}
