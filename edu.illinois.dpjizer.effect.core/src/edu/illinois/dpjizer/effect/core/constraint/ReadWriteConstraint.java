/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

import com.sun.tools.javac.code.Effect;
import com.sun.tools.javac.code.RPL;

import edu.illinois.dpjizer.effect.core.constraint.MethodEnvironments.Prune;
import edu.illinois.dpjizer.effect.core.effect.EffectUtils;
import edu.illinois.dpjizer.effect.core.rpl.RPLUtils;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
public abstract class ReadWriteConstraint extends Constraint {
	private RPL rpl;
	protected Effect effect;

	public ReadWriteConstraint(String invokerMethod, RPL rpl) {
		super(invokerMethod);
		this.rpl = RPLNomalizer.canonicalForm(rpl);
	}

	@Override
	public boolean equals(Object other) {
		boolean superEquals = super.equals(other);
		if (!superEquals)
			return false;
		if (other instanceof ReadWriteConstraint) {
			return getRPL().equals(((ReadWriteConstraint) other).getRPL());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getRPL().hashCode();
	}

	@Override
	public int compareTo(Constraint o) {
		int superCompareTo = super.compareTo(o);
		if (superCompareTo == 0) {
			return getRPL().toString().compareTo(((ReadWriteConstraint) o).getRPL().toString());
		} else
			return superCompareTo;
	}

	@Override
	public String toString() {
		return ownerMethod + " " + getOperator() + " " + getRPL();
	}

	@Override
	public String getClause() {
		return getRPL().toString();
	}

	public RPL getRPL() {
		return rpl;
	}

	// @Override
	// public boolean isLocal() {
	// return RPLUtils.isLocal(rpl);
	// }

	// public Effect getEffect() {
	// return effect;
	// }

	abstract protected String getOperator();

	public boolean containsStar() {
		return RPLUtils.containsStar(getRPL().elts);
	}

	/**
	 * 
	 * @return true if and only if the RPL is valid in the scope of the caller.
	 */
	@Override
	@Deprecated
	public boolean isValid() {
		MethodEnvironments methodEnvs = MethodEnvironments.instance();

		if (!(getRPL().equals(methodEnvs.inEnvironment(getRPL(), ownerMethod, Prune.PRUNE_LOCALS))))
			return false;

		return true;
	}

	@Override
	public boolean covers(Constraint otherConstriant) {
		if (!(otherConstriant instanceof ReadWriteConstraint))
			return false;
		ReadWriteConstraint readWriteConstraint = (ReadWriteConstraint) otherConstriant;
		Effect thisEffect = EffectUtils.toEffect(this);
		Effect otherEffect = EffectUtils.toEffect(readWriteConstraint);
		return EffectUtils.isSubeffectOf(otherEffect, thisEffect);
	}

}
