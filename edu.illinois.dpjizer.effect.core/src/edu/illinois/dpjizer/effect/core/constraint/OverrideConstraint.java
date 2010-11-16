/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.Iterator;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class OverrideConstraint extends Constraint {

	/**
	 * Fully qualified name of the overridden method.
	 */
	String overriddenMethod;
	List<RegionParameterSymbol> parentClassRegionParameters;
	List<RPL> childClassRegionParameters;
	List<RegionParameterSymbol> parentMethodRegionParameters;
	List<RegionParameterSymbol> childMethodRegionParamaters;

	public OverrideConstraint(String overriderMethod, String overriddenMethod, List<RegionParameterSymbol> parentRegionParameters,
			List<RPL> childRegionParamaters, List<RegionParameterSymbol> overriddenRegionParameters,
			List<RegionParameterSymbol> overriderRegionParamaters) {
		super(overriderMethod);

		if (overriddenMethod == null)
			throw new RuntimeException("Invalid method name");

		this.overriddenMethod = overriddenMethod;
		if (!doListsMatch(parentRegionParameters, childRegionParamaters))
			throw new RuntimeException("Mismatched class region parameters");
		this.parentClassRegionParameters = parentRegionParameters;
		this.childClassRegionParameters = childRegionParamaters;

		if (!doListsMatch(overriddenRegionParameters, overriderRegionParamaters))
			throw new RuntimeException("Mismatched method region parameters");
		this.childMethodRegionParamaters = overriderRegionParamaters;
		this.parentMethodRegionParameters = overriddenRegionParameters;
	}

	private <T1, T2> boolean doListsMatch(List<T1> list1, List<T2> list2) {
		if (list1 == null && list2 == null)
			return true;
		return list1.size() == list2.size();
	}

	// private void removeIrreversibleMappings() {
	// ListBuffer<RegionParameterSymbol> parentRegions = ListBuffer.lb();
	// ListBuffer<RPL> childRegions = ListBuffer.lb();
	//
	// Iterator<RegionParameterSymbol> parentIter =
	// parentRegionParameters.iterator();
	// Iterator<RPL> childIter = childRegionParameters.iterator();
	// }

	@Override
	public boolean equals(Object other) {
		boolean superEquals = super.equals(other);
		if (!superEquals)
			return false;
		if (other instanceof OverrideConstraint) {
			return overriddenMethod.equals(((OverrideConstraint) other).overriddenMethod);
		}
		return false;
	}

	@Override
	public int compareTo(Constraint o) {
		int superCompareTo = super.compareTo(o);
		if (superCompareTo == 0) {
			return overriddenMethod.compareTo(((OverrideConstraint) o).overriddenMethod);
		} else
			return superCompareTo;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + overriddenMethod.hashCode();
	}

	@Override
	public String toString() {
		return ownerMethod + " overrides " + getClause();
	}

	private String regionMap() {
		StringBuffer regionMappingsBuf = new StringBuffer();

		// List class region parameters
		if (childClassRegionParameters != null) {
			Iterator<RPL> chlidIter = childClassRegionParameters.iterator();

			for (Iterator<RegionParameterSymbol> parentIter = parentClassRegionParameters.iterator(); parentIter.hasNext();) {
				RegionParameterSymbol parentSymbol = parentIter.next();
				RPL childSymbol = chlidIter.next();
				regionMappingsBuf.append(parentSymbol.toString() + " -> " + childSymbol.toString());

				if (parentIter.hasNext())
					regionMappingsBuf.append(", ");
			}
		}

		// List method region parameters
		if (childMethodRegionParamaters != null) {
			Iterator<RegionParameterSymbol> chlidIter = childMethodRegionParamaters.iterator();

			if (childClassRegionParameters != null && childClassRegionParameters.nonEmpty())
				regionMappingsBuf.append(", ");

			for (Iterator<RegionParameterSymbol> parentIter = parentMethodRegionParameters.iterator(); parentIter.hasNext();) {
				RegionParameterSymbol parentSymbol = parentIter.next();
				RegionParameterSymbol childSymbol = chlidIter.next();
				regionMappingsBuf.append(parentSymbol.toString() + " -> " + childSymbol.toString());

				if (parentIter.hasNext())
					regionMappingsBuf.append(", ");
			}
		}

		return regionMappingsBuf.toString();
	}

	@Override
	public String getClause() {
		StringBuffer clause = new StringBuffer();
		clause.append(overriddenMethod);

		if (isParametric()) {
			String regionMap = regionMap();
			if (regionMap.length() > 0)
				clause.append(" where { " + regionMap() + " }");
		}

		return clause.toString();
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public Constraint invokerChangedTo(String invoker) {
		throw new RuntimeException("subclassResponsibility");
	}

	public boolean isParametric() {
		return isMethodParametric() || isClassParametric();
	}

	public boolean isMethodParametric() {
		return childMethodRegionParamaters != null && childMethodRegionParamaters.nonEmpty() && parentMethodRegionParameters != null
				&& parentMethodRegionParameters.nonEmpty();
	}

	public boolean isClassParametric() {
		return childClassRegionParameters != null && parentClassRegionParameters != null && childClassRegionParameters.nonEmpty()
				&& parentClassRegionParameters.nonEmpty();
	}

	@Override
	protected Constraint substituteRegions(List<RegionParameterSymbol> from, List<RPL> to) {
		throw new RuntimeException("subclassResponsibility");
	}

	@Override
	protected Constraint substituteForThis(RPL receiver) {
		throw new RuntimeException("subclassResponsibility");
	}

	@Override
	public Constraint inEnvironment(MethodEnvironments methodEnvs, String methodName) {
		throw new RuntimeException("subclassResponsibility");
	}

	@Override
	public boolean isValid() {
		// FIXME:
		return true;
	}

	@Override
	public boolean covers(Constraint otherConstriant) {
		return this.equals(otherConstriant);
	}
}
