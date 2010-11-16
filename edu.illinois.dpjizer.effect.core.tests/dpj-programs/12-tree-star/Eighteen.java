/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class Node<region P> {
	region L, R;
	double mass in P;
	Node<L> left in L;
	Node<R> right in R;

	Node() {}
	
	void setMass(double mass) {
		this.mass = mass;
	}
	
	void setMassOfChildren(double mass) {
		cobegin {
			if (left != null) left.setMass(mass);
			if (right != null) right.setMass(mass);
		}
	}
}