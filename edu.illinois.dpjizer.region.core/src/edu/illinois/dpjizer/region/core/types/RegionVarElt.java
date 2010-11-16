/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.sun.tools.javac.code.RPLElement;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVarElt extends RPLElement {

	public RegionVarEltSymbol sym;

	@Deprecated
	public RegionVarElt() {
	}

	public RegionVarElt(RegionVarEltSymbol sym) {
		super();
		this.sym = sym;
	}

	@Override
	public String toString() {
		return sym.toString();
	}

}
