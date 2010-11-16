/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import com.sun.tools.javac.code.RPL;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class InclusionConstraint implements Constraint {

	RPL contained;
	RPL container;

	public InclusionConstraint(RPL contained, RPL container) {
		super();
		this.contained = contained;
		this.container = container;
	}

	@Override
	public String toString() {
		return contained + " is in " + container;
	}

}
