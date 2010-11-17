/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.google.inject.Inject;
import com.sun.tools.javac.tree.JCTree.DPJRegionPathList;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVar extends DPJRegionPathList {

	String name;

	@Inject
	public RegionVar(RegionVarCounter regionVarCounter) {
		super(null);
		name = regionVarCounter.getNextRegionVarName();
	}

	@Override
	public String toString() {
		return name;
	}

}
