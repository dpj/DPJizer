/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import java.util.Iterator;
import java.util.Map;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import edu.illinois.dpjizer.effect.core.constraint.MethodEnvironments.Prune;
import edu.illinois.dpjizer.effect.core.equality.NullEquality;
import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
public class InvokeConstraint extends Constraint {

	String invokedMethod;
	List<RegionParameterSymbol> regionParameters;
	List<RPL> regionActuals;
	Map<RegionParameterSymbol, RPL> substitution;
	RPL actualThis;

	public InvokeConstraint(String invokerMethod, String invokedMethod, List<RegionParameterSymbol> regionParameters, List<RPL> regionActuals,
			RPL actualThis) {
		super(invokerMethod);
		if (invokedMethod == null)
			throw new RuntimeException("Invalid method name.");
		this.invokedMethod = invokedMethod;

		if (regionParameters == null)
			this.regionParameters = List.nil();
		else
			this.regionParameters = regionParameters;
		this.regionActuals = RPLNomalizer.canonicalForm(regionActuals);
		this.actualThis = RPLNomalizer.canonicalForm(actualThis);
		if (this.actualThis != null && this.actualThis.size() == 1 && this.actualThis.elts.head.toString().equals("this"))
			this.actualThis = null;
	}

	// protected Map<RegionParameterSymbol, RPL>
	// makeSubstitution(List<RegionParameterSymbol> regionParameters, List<RPL>
	// regionActuals) {
	// Map<RegionParameterSymbol, RPL> substitution = new
	// HashMap<RegionParameterSymbol, RPL>();
	// if (regionActuals != null) {
	// Iterator<RPL> thisIter = regionActuals.iterator();
	// Iterator<RPL> otherIter = regionActuals.iterator();
	// while (thisIter.hasNext()) {
	// RPL thisRPL = thisIter.next();
	// RPL otherRPL = otherIter.next();
	// substitution.put(thisRPL, value)
	// }
	// }
	// return substitution;
	// }

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof InvokeConstraint))
			return false;

		InvokeConstraint otherInvokeConstraint = (InvokeConstraint) other;

		if (!basicEquals(otherInvokeConstraint))
			return false;

		if (actualThis != null)
			if (!(actualThis.equals(otherInvokeConstraint.actualThis)))
				return false;

		if (regionActuals != null)
			if (!(regionActuals.equals(otherInvokeConstraint.regionActuals)))
				return false;
		return true;
	}

	/**
	 * Compare this invoke constraint with another without taking the RPLs in
	 * regionActuals into account.
	 */
	public boolean basicEquals(InvokeConstraint otherInvokeConstraint) {
		if (!super.equals(otherInvokeConstraint))
			return false;

		if (!invokedMethod.equals(otherInvokeConstraint.invokedMethod))
			return false;

		if (NullEquality.onlyOneIsNull(actualThis, otherInvokeConstraint.actualThis)) {
			return false;
		}

		if (NullEquality.onlyOneIsNull(regionParameters, otherInvokeConstraint.regionParameters)) {
			return false;
		}

		if (regionParameters != null)
			if (!(regionParameters.equals(otherInvokeConstraint.regionParameters)))
				return false;

		if (NullEquality.onlyOneIsNull(regionActuals, otherInvokeConstraint.regionActuals)) {
			return false;
		}

		if (regionActuals != null)
			if (regionActuals.size() != otherInvokeConstraint.regionActuals.size())
				return false;

		return true;
	}

	@Override
	public boolean covers(Constraint otherConstriant) {
		if (!(otherConstriant instanceof InvokeConstraint))
			return false;
		InvokeConstraint otherInvokeConstraint = (InvokeConstraint) otherConstriant;

		if (!basicEquals(otherInvokeConstraint))
			return false;

		if (otherInvokeConstraint.actualThis != null) {
			if (!(RPLUtils.isIncludedIn(otherInvokeConstraint.actualThis, actualThis)))
				return false;
		}

		if (otherInvokeConstraint.regionActuals != null) {
			Iterator<RPL> thisIter = regionActuals.iterator();
			Iterator<RPL> otherIter = otherInvokeConstraint.regionActuals.iterator();
			while (thisIter.hasNext()) {
				RPL thisRPL = thisIter.next();
				RPL otherRPL = otherIter.next();
				if (!RPLUtils.isIncludedIn(otherRPL, thisRPL))
					return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + invokedMethod.hashCode();
	}

	@Override
	public int compareTo(Constraint o) {
		int superCompareTo = super.compareTo(o);
		if (superCompareTo == 0) {
			return toString().compareTo(((InvokeConstraint) o).toString());
		} else
			return superCompareTo;
	}

	@Override
	public String toString() {
		return ownerMethod + " invokes " + getClause();
	}

	private String regionMap() {
		StringBuffer regionMappingsBuf = new StringBuffer();
		if (actualThis != null) {
			regionMappingsBuf.append("this -> " + actualThis.toString());
			if (!regionActuals.isEmpty())
				regionMappingsBuf.append(", ");
		}

		if (regionActuals != null) {
			Iterator<RPL> classRegionActualsIter = regionActuals.iterator();

			for (Iterator<RegionParameterSymbol> classRegionParametersIter = regionParameters.iterator(); classRegionParametersIter.hasNext();) {
				RegionParameterSymbol regionParameterSymbol = classRegionParametersIter.next();
				RPL rpl = classRegionActualsIter.next();
				regionMappingsBuf.append(regionParameterSymbol.toString() + " -> " + rpl.toString());

				if (classRegionParametersIter.hasNext())
					regionMappingsBuf.append(", ");
			}
		}
		return regionMappingsBuf.toString();
	}

	@Override
	public String getClause() {
		StringBuffer clause = new StringBuffer();
		clause.append(invokedMethod);

		String regionMap = regionMap();
		if (regionMap.length() > 0)
			clause.append(" where { " + regionMap() + " }");
		return clause.toString();
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public Constraint invokerChangedTo(String invoker) {
		return new InvokeConstraint(invoker, invokedMethod, regionParameters, regionActuals, actualThis);
	}

	public boolean isParametric() {
		return regionActuals != null && regionParameters != null && regionActuals.nonEmpty() && regionParameters.nonEmpty();
	}

	@Override
	protected Constraint substituteRegions(List<RegionParameterSymbol> from, List<RPL> to) {
		ListBuffer<RPL> substitutedRPLs = ListBuffer.lb();
		for (RPL rpl : regionActuals) {
			substitutedRPLs.append(RPLUtils.substForParams(rpl, from, to));
		}

		RPL substitutedThisMapping = null;
		if (actualThis != null)
			substitutedThisMapping = RPLUtils.substForParams(actualThis, from, to);

		InvokeConstraint substitutedInvokeConstraint = new InvokeConstraint(ownerMethod, invokedMethod, regionParameters, substitutedRPLs.toList(),
				substitutedThisMapping);

		return substitutedInvokeConstraint;
	}

	/**
	 * 
	 * @return true if and only if whatever we are mapping to is valid in the
	 *         scope of the caller.
	 */
	@Override
	public boolean isValid() {
		MethodEnvironments methodEnvs = MethodEnvironments.instance();

		if (regionActuals != null) {
			for (RPL rpl : regionActuals) {
				if (!(rpl.equals(methodEnvs.inEnvironment(rpl, ownerMethod, Prune.KEEP_LOCALS))))
					return false;
			}
		}

		if (actualThis != null) {
			RPL receiverInEnv = methodEnvs.inEnvironment(actualThis, ownerMethod, Prune.KEEP_LOCALS);
			if (!(actualThis.equals(receiverInEnv)))
				return false;
		}

		return true;
	}

	/**
	 * "this" doesn't appear on the right-hand side of any region mapping. So,
	 * substitution of this on an invocation constraint doesn't make sense.
	 * However, we want to able to change what is RPL that "this" is mapped to.
	 * And, that's kept as a separate mapping in InvokeConstraint.
	 */
	@Override
	protected Constraint substituteForThis(RPL receiver) {
		return this;
	}

	public boolean isRecursive() {
		return ownerMethod.equals(invokedMethod);
	}

	public boolean isExpandingRecursive() {
		if (!isRecursive())
			return false;
		return isExpanding();
	}

	public boolean isExpanding() {
		if (!isParametric())
			return false;
		Iterator<RegionParameterSymbol> parameterIterator = regionParameters.iterator();
		Iterator<RPL> actualIterator = regionActuals.iterator();
		while (parameterIterator.hasNext()) {
			RegionParameterSymbol parameter = parameterIterator.next();
			RPL actual = actualIterator.next();
			if (RPLUtils.isExpanding(parameter, actual))
				return true;
		}
		return false;
	}

	/**
	 * For each expanding substitution such as "X/X:R", apply "X/X:R:*" on
	 * effects which don't have any stars.
	 */
	public InvokeConstraint summarized() {
		InvokeConstraint starAppendedInvoke = new InvokeConstraint(ownerMethod, invokedMethod, regionParameters, RPLUtils.starAppended(
				regionParameters, regionActuals), actualThis);
		if (isExpandingRecursive())
			// if (isExpanding())
			if (!covers(starAppendedInvoke))
				// regionActuals = RPLUtils.starAppended(regionParameters,
				// regionActuals);
				return starAppendedInvoke;
		return this;
	}

	@Override
	public Constraint inEnvironment(MethodEnvironments methodEnvs, String methodName) {
		if (regionActuals == null)
			return this;
		RPL[] regions = new RPL[regionActuals.size()];
		regionActuals.toArray(regions);
		RPL[] inEnvRegions = new RPL[regionActuals.size()];
		for (int i = 0; i < inEnvRegions.length; i++) {
			inEnvRegions[i] = methodEnvs.inEnvironment(regions[i], methodName, Prune.KEEP_LOCALS);
		}

		RPL inEnvReceiver = actualThis;

		if (actualThis != null)
			inEnvReceiver = methodEnvs.inEnvironment(actualThis, methodName, Prune.KEEP_LOCALS);

		return new InvokeConstraint(ownerMethod, invokedMethod, regionParameters, List.from(inEnvRegions), inEnvReceiver);
	}

	/**
	 * If m1 invokes m2 where {X/X:L} and m2 invokes m1 where {X/X:L} we should
	 * not add the derived constraint m1 invokes m2 where {X/X:L:L} to m1,
	 * otherwise an unbounded number of such invoke constraints will get
	 * generated. Instead, we just need to process the original invoke
	 * constraint multiple times to get all the effects.
	 * 
	 * @param otherInvokeConstraint
	 * @return true if multiple applications of this invoke constraint implies
	 *         the other one.
	 */
	public boolean derives(InvokeConstraint otherInvokeConstraint) {
		if (!this.basicEquals(otherInvokeConstraint))
			return false;

		// if (this.isExpanding() || otherInvokeConstraint.isExpanding())
		// System.out.println("****************************************");

		if (this.covers(otherInvokeConstraint))
			return true;

		int otherSubstitutionMaxLength = 2 * (otherInvokeConstraint.longestSubstitutionLength() + 1);

		// The invoke constraint obtained by applying the substitution of this
		// invoke constraint on this invoke constraint multiple times.
		Constraint repeatedInvokeConstraint = applyInvokeConstraint(this);
		for (int maxNumOfSubstitutionsToImply = 1; maxNumOfSubstitutionsToImply < otherSubstitutionMaxLength; ++maxNumOfSubstitutionsToImply) {
			if (repeatedInvokeConstraint.covers(otherInvokeConstraint))
				return true;
			repeatedInvokeConstraint = applyInvokeConstraint(repeatedInvokeConstraint);
		}
		return false;
	}

	protected Constraint applyInvokeConstraint(Constraint constraint) {
		return (Constraint) constraint.substituteRegions(regionParameters, regionActuals);
	}

	/**
	 * @return the length of the longest substitution X/P1:P2:...:Pn
	 */
	public int longestSubstitutionLength() {
		int maxLength = 0;
		for (RPL rpl : regionActuals) {
			if (rpl.elts.size() > maxLength)
				maxLength = rpl.elts.size();
		}
		return maxLength;
	}

}
