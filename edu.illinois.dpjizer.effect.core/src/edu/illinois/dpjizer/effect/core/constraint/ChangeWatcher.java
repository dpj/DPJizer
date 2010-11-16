/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package edu.illinois.dpjizer.effect.core.constraint;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
public class ChangeWatcher {

	private Constraints snapshot1 = null;
	private Constraints snapshot2 = null;

	public void takeSnapshot1(Constraints snapshot1) {
		if (this.snapshot1 != null || this.snapshot2 != null)
			throw new RuntimeException("Take snapshot 1 then snapshot 2");
		this.snapshot1 = new Constraints();
		this.snapshot1.addAll(snapshot1);
	}

	public void takeSnapshot2(Constraints snapshot2) {
		if (this.snapshot1 == null || this.snapshot2 != null)
			throw new RuntimeException("Take snapshot 1 then snapshot 2");
		this.snapshot2 = new Constraints();
		this.snapshot2.addAll(snapshot2);
	}

	public boolean gotNewConstraints() {
		return snapshot2.size() > snapshot1.size();
	}

	public Constraints newConstraints() {
		if (snapshot1 == null || snapshot2 == null)
			throw new RuntimeException("Take snapshots then compute the differences");
		snapshot2.removeAll(snapshot1);
		Constraints newConstraints = new Constraints();
		newConstraints.addAll(snapshot2);
		return newConstraints;
	}
}
