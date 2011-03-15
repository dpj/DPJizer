/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.dpjizer.constraints.Constraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class SubtypingConstraint implements Constraint {

	Type supertype;
	Type subtype;

	public SubtypingConstraint(Type supertype, Type subtype) {
		super();
		this.supertype = supertype;
		this.subtype = subtype;
	}

	@Override
	public String toString() {
		return subtype + " is a subtype of " + supertype;
	}

}
