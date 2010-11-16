/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
package oopsla2009;

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class Tree<region P> {
	region X, Y, L, R, Links;
	int x in P : X;
	int y in P : Y;
	Tree<P : L> left in Links;
	Tree<P : R> right in Links;
	Tree<*> link in Links;

	Tree() {}

	void updateNode() {
		this.x = this.link.y;
	}

	void updateTree() {
		updateNode();
		cobegin {
			if (left != null)
				left.updateTree();
			if (right != null)
				right.updateTree();
		}
	}
}