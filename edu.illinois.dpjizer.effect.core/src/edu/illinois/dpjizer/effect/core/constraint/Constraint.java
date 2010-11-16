/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public abstract class Constraint implements Comparable<Constraint> {
	protected String ownerMethod;

	public Constraint(String invokerMethod) {
		this.ownerMethod = invokerMethod;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (other instanceof Constraint) {
			Constraint otherConstraint = (Constraint) other;
			return (ownerMethod.equals(otherConstraint.ownerMethod));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ownerMethod.hashCode();
	}

	@Override
	public int compareTo(Constraint o) {
		return ownerMethod.compareTo(o.ownerMethod);
	}

	@Override
	public String toString() {
		return "dummy constraint";
	}

	public String getClause() {
		throw new RuntimeException("subclassResponsibility");
	}

	@Deprecated
	public boolean isLocal() {
		return false;
	}

	public abstract Constraint invokerChangedTo(String invoker);

	protected abstract Constraint substituteRegions(List<RegionParameterSymbol> from, List<RPL> to);

	protected abstract Constraint substituteForThis(RPL receiver);

	public abstract Constraint inEnvironment(MethodEnvironments methodEnvs, String methodName);

	public abstract boolean isValid();

	// public static Collection<Constraint> ownersChangedTo(String invoker,
	// final Collection<Constraint> constraintsCollection,
	// Collection<Constraint> changedConstraintsCollection) {
	//
	// for (Constraint constraint : constraintsCollection) {
	// changedConstraintsCollection.add(constraint.invokerChangedTo(invoker));
	// }
	//
	// return changedConstraintsCollection;
	// }
	//
	// public static Collection<Constraint> substituteRegions(final
	// Collection<Constraint> constraintsCollection, List<RegionParameterSymbol>
	// from,
	// List<RPL> to, Collection<Constraint> substitutedConstraints) {
	// for (Constraint constraint : constraintsCollection) {
	// Constraint substitutedRegions = constraint.substituteRegions(from, to);
	// substitutedConstraints.add(substitutedRegions);
	// }
	// return substitutedConstraints;
	// }
	//
	// public static Collection<Constraint> substituteForThis(final
	// Collection<Constraint> constraintsCollection, RPL receiver,
	// Collection<Constraint> substitutedConstraints) {
	// for (Constraint constraint : constraintsCollection) {
	// Constraint substitutedRegions = constraint.substituteForThis(receiver);
	// substitutedConstraints.add(substitutedRegions);
	// }
	// return substitutedConstraints;
	// }
	//
	// public static Collection<Constraint> inEnvironment(MethodEnvironments
	// methodEnvs, String methodName, final Collection<Constraint> constraints,
	// final Collection<Constraint> inEnvConstraints) {
	// for (Constraint constraint : constraints) {
	// Constraint constraintInEnv = constraint.inEnvironment(methodEnvs,
	// methodName);
	// if (constraintInEnv != null)
	// inEnvConstraints.add(constraintInEnv);
	// }
	// return inEnvConstraints;
	// }
	//
	// public static Collection<Constraint> invalids(final
	// Collection<Constraint> constraints, final Collection<Constraint>
	// invalidConstraints) {
	// for (Constraint constraint : constraints) {
	// if (!constraint.isValid())
	// invalidConstraints.add(constraint);
	// }
	// return invalidConstraints;
	// }

	public abstract boolean covers(Constraint otherConstriant);
}
