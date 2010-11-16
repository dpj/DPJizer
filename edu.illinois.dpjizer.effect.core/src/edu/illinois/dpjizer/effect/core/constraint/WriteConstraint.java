/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.effect.core.constraint.MethodEnvironments.Prune;
import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 * 
 */
public class WriteConstraint extends ReadWriteConstraint {
	public WriteConstraint(String invokerMethod, RPL rpl) {
		super(invokerMethod, rpl);
		this.effect = new Effect.WriteEffect(null, RPLNomalizer.canonicalForm(rpl), false, false);
	}

	@Override
	public boolean equals(Object other) {
		boolean superEquals = super.equals(other);
		if (!superEquals)
			return false;
		if (other instanceof WriteConstraint) {
			return getRPL().equals(((WriteConstraint) other).getRPL());
		}
		return false;
	}

	@Override
	protected String getOperator() {
		return "writes";
	}

	@Override
	public Constraint invokerChangedTo(String invoker) {
		return new WriteConstraint(invoker, getRPL());
	}

	@Override
	public Constraint substituteRegions(List<RegionParameterSymbol> from, List<RPL> to) {
		WriteConstraint substitutedConstraint = new WriteConstraint(ownerMethod, RPLUtils.substForParams(getRPL(), from, to));
		// if (!substitutedConstraint.isValid())
		// throw new RuntimeException("Invalid WriteConstraint:" +
		// substitutedConstraint);
		return substitutedConstraint;
	}

	@Override
	protected Constraint substituteForThis(RPL receiver) {
		WriteConstraint substitutedConstraint = new WriteConstraint(ownerMethod, RPLUtils.substForThis(getRPL(), receiver));
		// if (!substitutedConstraint.isValid())
		// throw new RuntimeException("Invalid WriteConstraint:" +
		// substitutedConstraint);
		return substitutedConstraint;
	}

	@Override
	public Constraint inEnvironment(MethodEnvironments methodEnvs, String methodName) {
		RPL rplInEnv = methodEnvs.inEnvironment(getRPL(), methodName, Prune.PRUNE_LOCALS);
		if (rplInEnv == null)
			return null;
		else
			return new WriteConstraint(ownerMethod, rplInEnv);
	}
}
