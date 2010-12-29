/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.google.inject.Inject;
import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;

import edu.illinois.dpjizer.region.core.constraints.Constraints;
import edu.illinois.dpjizer.region.core.constraints.InclusionConstraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RPLWithSubstitution extends RPL {

	public static boolean isCapturingConstraints = true;

	SubstitutionChain substitution = SubstitutionChain.EMPTY;

	Constraints constraints;

	@Inject
	public RPLWithSubstitution(List<RPLElement> elts, Constraints constraints) {
		super(elts);
		this.constraints = constraints;
	}

	@Inject
	public RPLWithSubstitution(RPLElement singletonElement, Constraints constraints) {
		super(singletonElement);
		this.constraints = constraints;
	}

	@Inject
	public RPLWithSubstitution(List<RPLElement> elts, SubstitutionChain substitution, Constraints constraints) {
		this(elts, constraints);
		this.substitution = substitution;
	}

	public RPL substForParam(RegionParameterSymbol param, RPL rpl) {
		// if (!this.elts.head.equals(param)) {
		// return this;
		// }
		return new RPLWithSubstitution(elts, substitution.followedBy(new RegionSubstitution(param, rpl)), constraints);
	}

	// TODO: This is a hack to make every RPL included in RPLs that contain
	// region variables.
	@Override
	protected boolean endsWithStar() {
		return true;
	}

	// TODO: I need to figure out the rules for inclusion of RPLs that contain
	// region variables.
	@Override
	public boolean isIncludedIn(RPL that) {
		if (isCapturingConstraints) {
			constraints.add(new InclusionConstraint(this, that));
			return true;
		} else {
			return false;
		}
	}

	// TODO: I need to figure out the nesting rules for RPLs that contain region
	// variables.
	@Override
	public boolean isNestedUnder(RPL that) {
		return true;
	}

	// TODO: I probably need a proper equals method.
	@Override
	public boolean equals(Object other) {
		return (other instanceof RPL);
	}

	@Override
	public int hashCode() {
		return 91;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(substitution);
		return sb.toString();
	}

	@Override
	public RPL substIndex(VarSymbol from, JCExpression to) {
		return new RPLWithSubstitution(elts, substitution.followedBy(new IndexSubstitution(from, to)), constraints);
	}

}
