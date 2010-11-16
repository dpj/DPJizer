/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class SubstitutionChain {

	List<Substitutions> chain = new ArrayList<Substitutions>();

	public static SubstitutionChain EMPTY = new SubstitutionChain();

	public void add(Substitutions substitutions) {
		chain.add(substitutions);
	}

	public void add(List<Substitutions> substitutions) {
		chain.addAll(substitutions);
	}

	public SubstitutionChain followedBy(Substitution substitution) {
		return followedBy(new Substitutions(substitution));
	}

	public SubstitutionChain followedBy(Substitutions substitutions) {
		SubstitutionChain substitutionChain = new SubstitutionChain();
		substitutionChain.add(chain);
		substitutionChain.add(substitutions);
		return substitutionChain;
	}

	public SubstitutionChain followedBy(SubstitutionChain substitutionChain) {
		SubstitutionChain newSubstitutionChain = new SubstitutionChain();
		newSubstitutionChain.add(chain);
		newSubstitutionChain.add(substitutionChain.chain);
		return newSubstitutionChain;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Substitutions substitutions : chain) {
			sb.append(substitutions);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chain == null) ? 0 : chain.hashCode());
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
		SubstitutionChain other = (SubstitutionChain) obj;
		if (chain == null) {
			if (other.chain != null)
				return false;
		} else if (!chain.equals(other.chain))
			return false;
		return true;
	}

}
