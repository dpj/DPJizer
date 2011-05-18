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
@Deprecated
public class SubtypingConstraint implements Constraint {

	Type supertype;
	Type subtype;

	public SubtypingConstraint(Type supertype, Type subtype) {
		this.supertype = supertype;
		this.subtype = subtype;
	}

	@Override
	public String toString() {
		return subtype + " is a subtype of " + supertype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
		result = prime * result + ((supertype == null) ? 0 : supertype.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubtypingConstraint other = (SubtypingConstraint) obj;
		if (subtype == null) {
			if (other.subtype != null)
				return false;
		} else if (!subtype.equals(other.subtype))
			return false;
		if (supertype == null) {
			if (other.supertype != null)
				return false;
		} else if (!supertype.equals(other.supertype))
			return false;
		return true;
	}

}
