/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import static com.sun.tools.javac.code.Kinds.RPL_ELT;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.RPLElementSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Position;

/**
 * 
 * A class for region parameter symbols
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarEltSymbol extends RPLElementSymbol {

	/**
	 * The region's declaration position.
	 */
	public int pos = Position.NOPOS;

	public static int numIDs = 1;
	public int ID = 0;

	public RegionVarEltSymbol(long flags, Name name, Symbol owner) {
		super(RPL_ELT, flags, name, owner);
		this.ID = numIDs++;
	}

	/**
	 * Clone this symbol with new owner.
	 */
	public RegionVarEltSymbol clone(Symbol newOwner) {
		RegionVarEltSymbol v = new RegionVarEltSymbol(flags_field, name, newOwner);
		v.pos = pos;
		// System.err.println("clone " + v + " in " + newOwner);//DEBUG
		return v;
	}

	public String toString() {
		return name.toString();
	}

	public Symbol asMemberOf(Type site, Types types) {
		return new RegionVarEltSymbol(flags_field, name, owner);
	}

	// public ElementKind getKind() {
	// return ElementKind.REGION_PARAMETER;
	// }
}
