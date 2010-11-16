/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.rpl;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.RPLElement;
import com.sun.tools.javac.code.RPLElement.RPLParameterElement;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;
import com.sun.tools.javac.comp.Attr;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import edu.illinois.dpjizer.effect.core.constraint.MethodEnvironments.Prune;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RPLUtils {

	private static final RPL LOCAL_STAR = new RPL(List.of(RPLElement.LOCAL_ELEMENT, RPLElement.STAR));

	public static boolean isNestedUnder(RPL first, RPL right) {
		return first.isNestedUnder(right);
	}

	public static boolean isIncludedIn(RPL first, RPL right) {
		return first.isIncludedIn(right);
	}

	/**
	 * This method first translates the RPL into the given environment. If there
	 * is any local name in the RPL, the RPL gets translated to an RPL that is
	 * under "Local". When run in Prune.KEEP_LOCALS mode, the method returns the
	 * result of translation at this point. Otherwise, if the method is run in
	 * the Prune.PRUNE_LOCALS mode, it returns null.
	 */
	public static RPL inEnvironment(RPL rpl, Resolve rs, Env<AttrContext> env, Prune prune) {
		RPL rplinEnv = rpl.inEnvironment(rs, env, false);
		if (rplinEnv == null) {
			rplinEnv = LOCAL_STAR;
		}
		if (prune == Prune.PRUNE_LOCALS) {
			if (rplinEnv.isUnderLocal()) {
				rplinEnv = null;
			}
		}
		return rplinEnv;
	}

	public static boolean isExpanding(RegionParameterSymbol from, RPL to) {
		return isNestedUnder(to, toRPL(from)) && to.size() > 1;
	}

	public static boolean containsStar(List<RPLElement> rplElements) {
		for (RPLElement rplElement : rplElements) {
			if (RPLElement.STAR.isIncludedIn(rplElement))
				return true;
		}
		return false;
	}

	private static RPL starAppended(RPL rpl) {
		return new RPL(rpl.elts.append(RPLElement.STAR));
	}

	/**
	 * Replace each expanding pair such as "X/X:R" by "X/X:R:*".
	 */
	public static List<RPL> starAppended(List<RegionParameterSymbol> regionParameters, List<RPL> regionActuals) {
		RPL[] regionActualsArray = regionActuals.toArray(new RPL[regionActuals.size()]);
		for (int i = 0; i < regionActualsArray.length; i++) {
			if (RPLUtils.isExpanding(regionParameters.get(i), regionActualsArray[i])) {
				regionActualsArray[i] = starAppended(regionActualsArray[i]);
			}
		}
		return List.from(regionActualsArray);
	}

	public static RPL substForParams(RPL rpl, List<RegionParameterSymbol> from, List<RPL> to) {
		return rpl.substForParams(from, to);
	}

	/**
	 * This method is similar to
	 * com.sun.tools.javac.code.RPL.substForThis(VarSymbol) except that
	 * substitutes "this" by an RPL rather than a single name.
	 * 
	 * @param rpl
	 *            The RPL which is going to get transformed
	 * @param insteadOfThis
	 *            The RPL which is going to replace "this"
	 * @return A copy of rpl in which "this" is replaced by insteadOfThis
	 */
	public static RPL substForThis(RPL rpl, RPL insteadOfThis) {
		// if (!(rpl.elts.head instanceof RPLElement.VarRPLElement))
		// return rpl;
		// VarRegionSymbol vrs = (VarRegionSymbol) rpl.elts.head;
		// if (!vrs.vsym.name.toString().equals("this"))
		// return rpl;
		// return new RPL(insteadOfThis.elts.appendList(rpl.elts.tail));
		return rpl.substForThis(insteadOfThis);
	}

	public static RPL toRPL(JCExpression exprTree, Attr attr) {
		return attr.exprToRPL(exprTree);
	}

	// public static RPL toRPL(VarSymbol vsym) {
	// if (vsym == null)
	// return null;
	// return new RPL(List.<RPLElement> of(new RPLElement.VarRPLElement(vsym)));
	// }

	public static RPL toRPL(RegionParameterSymbol regionSymbol) {
		if (regionSymbol == null)
			return null;
		return new RPL(new RPLParameterElement(regionSymbol));
	}

	public static List<RPL> toRPLs(List<RegionParameterSymbol> regionSymbols) {
		if (regionSymbols == null)
			return null;

		ListBuffer<RPL> buf = ListBuffer.lb();
		for (RegionParameterSymbol regionSymbol : regionSymbols) {
			buf.append(toRPL(regionSymbol));
		}
		return buf.toList();
	}

	public static List<RegionParameterSymbol> toRegionParameterSymbols(List<RPL> rpls) {
		if (rpls == null)
			return null;

		ListBuffer<RegionParameterSymbol> buf = ListBuffer.lb();
		for (RPL rpl : rpls) {
			if (rpl.elts.size() != 1)
				throw new RuntimeException("Unexpected RPL");

			buf.append(((RPLParameterElement) rpl.elts.head).sym);
		}
		return buf.toList();
	}
}
