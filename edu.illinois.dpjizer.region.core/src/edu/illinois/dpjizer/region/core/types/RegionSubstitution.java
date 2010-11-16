/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.sun.tools.javac.code.RPL;
import com.sun.tools.javac.code.Symbol.RegionParameterSymbol;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionSubstitution implements Substitution {

	RegionParameterSymbol regionParamSym;

	RPL rpl;

	public RegionSubstitution(RegionParameterSymbol regionParamSym, RPL rpl) {
		super();
		this.regionParamSym = regionParamSym;
		this.rpl = rpl;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(regionParamSym.toString());
		sb.append(" <- ");
		sb.append(rpl.toString());
		return sb.toString();
	}

}
