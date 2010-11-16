/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import java.util.List;

import edu.illinois.dpjizer.region.core.types.RegionVar;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ListDisjointnessConstraint implements Constraint {

	List<RegionVar> regionVars1;

	List<RegionVar> regionVars2;

	public ListDisjointnessConstraint(List<RegionVar> regionVars1, List<RegionVar> regionVars2) {
		super();
		this.regionVars1 = regionVars1;
		this.regionVars2 = regionVars2;
	}

	@Override
	public String toString() {
		return regionVars1.toString() + " # " + regionVars2.toString();
	}

}
