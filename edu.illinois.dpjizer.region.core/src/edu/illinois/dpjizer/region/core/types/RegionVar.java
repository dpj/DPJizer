/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.types;

import com.sun.tools.javac.tree.JCTree.DPJRegionPathList;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class RegionVar extends DPJRegionPathList {

	String name;

	public RegionVar() {
		super(null);
		name = RegionVarCounter.getNextRegionVarName();
	}

	@Override
	public String toString() {
		return name;
	}

}
