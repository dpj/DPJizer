/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.region.core.constraints;

import edu.illinois.dpjizer.region.core.types.RegionVar;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class BinaryDisjointnessConstraint {

	RegionVar regionVar1;

	RegionVar regionVar2;

	public BinaryDisjointnessConstraint(RegionVar regionVar1, RegionVar regionVar2) {
		super();
		this.regionVar1 = regionVar1;
		this.regionVar2 = regionVar2;
	}

	@Override
	public String toString() {
		return regionVar1.toString() + " # " + regionVar2.toString();
	}

}
