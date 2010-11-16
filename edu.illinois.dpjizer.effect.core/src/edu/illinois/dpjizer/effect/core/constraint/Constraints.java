/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
public class Constraints implements Iterable<Constraint> {
	protected Set<Constraint> constraints = new HashSet<Constraint>();

	public boolean remove(Constraint c) {
		return constraints.remove(c);
	}

	public boolean removeAll(Constraints cs) {
		return constraints.removeAll(cs.constraints);
	}

	public boolean removeAllCoveredBy(Constraint ct) {
		Set<Constraint> covered = new HashSet<Constraint>();

		for (Constraint c : constraints) {
			if (!c.equals(ct) && ct.covers(c))
				covered.add(c);
		}

		return constraints.removeAll(covered);
	}

	/**
	 * 
	 * @param c
	 * @return true if and only if c was actually added to the collection.
	 */
	public boolean add(Constraint c) {
		if (!hasEquivalent(c) && !isCovered(c)) {
			constraints.add(c);
			removeAllCoveredBy(c);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param cs
	 * @param addedConstraints
	 *            if not null, gets the set of constraints in cs that get added
	 *            to this collection of constraints.
	 * @return true if and only if some constraints get added to the collection.
	 */
	public boolean addAll(Constraints cs, Constraints addedConstraints) {
		boolean added = false;
		for (Constraint c : cs) {
			if (add(c)) {
				if (addedConstraints != null)
					addedConstraints.add(c);
				added = true;
			}
		}
		return added;
	}

	public boolean addAll(Constraints cs) {
		return addAll(cs, null);
	}

	public boolean hasEquivalent(Constraint ct) {
		for (Constraint c : constraints) {
			if (c.equals(ct) || (c.covers(ct) && ct.covers(c)))
				return true;
		}
		return false;
	}

	public boolean isCovered(Constraint ct) {
		for (Constraint c : constraints) {
			if (c.covers(ct))
				return true;
		}
		return false;
	}

	public int size() {
		return constraints.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<Constraint> iterator() {
		return constraints.iterator();
	}

	public Constraints ownersChangedTo(String invoker) {
		Constraints newConstraints = new Constraints();
		for (Constraint c : constraints) {
			newConstraints.add(c.invokerChangedTo(invoker));
		}
		return newConstraints;
	}

	public Constraints substituteRegions(List<RegionParameterSymbol> from, List<RPL> to) {
		Constraints newConstraints = new Constraints();
		for (Constraint c : constraints) {
			Constraint substitutedRegions = c.substituteRegions(from, to);
			newConstraints.add(substitutedRegions);
		}
		return newConstraints;
	}

	public Constraints substituteForThis(RPL receiver) {
		Constraints newConstraints = new Constraints();
		for (Constraint c : constraints) {
			Constraint substitutedRegions = c.substituteForThis(receiver);
			newConstraints.add(substitutedRegions);
		}
		return newConstraints;
	}

	public Constraints inEnvironment(MethodEnvironments methodEnvs, String methodName) {
		Constraints newConstraints = new Constraints();
		for (Constraint c : constraints) {
			Constraint cInEnv = c.inEnvironment(methodEnvs, methodName);
			if (cInEnv != null)
				newConstraints.add(cInEnv);
		}
		return newConstraints;
	}

	public Constraints invalids() {
		Constraints invalidConstraints = new Constraints();
		for (Constraint c : constraints) {
			if (!c.isValid())
				invalidConstraints.add(c);
		}
		return invalidConstraints;
	}

	public Constraints nonRecursiveInvokes() {
		Constraints nonRecursives = new Constraints();
		for (Constraint c : constraints) {
			if (!((InvokeConstraint) c).isExpandingRecursive())
				nonRecursives.add(c);
		}
		return nonRecursives;
	}

	/**
	 * From the list of read/write constraints, return those that don't contain
	 * stars.
	 */
	public Constraints starFree() {
		Constraints withoutStars = new Constraints();
		for (Constraint c : constraints) {
			if (!((ReadWriteConstraint) c).containsStar()) {
				withoutStars.add(c);
			}
		}
		return withoutStars;
	}

	public Constraint[] toArray(Constraint[] cs) {
		return constraints.toArray(cs);
	}

	@Override
	public String toString() {
		return constraints.toString();
	}

}
