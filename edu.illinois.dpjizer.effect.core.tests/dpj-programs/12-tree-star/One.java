/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Tree.print -> Tree.print where {P/P:L}
 * Tree.print -> Tree.print where {P/P:R}
 * 
 * Tree.print: P
 */

package one;

/**
 * 
 * @author Mohsen Vakilian
 * @author Danny Dig
 *
 */
class Tree<region P> {
	region L, R;
	int data in P;
	Tree<P:L> left in P:L;
	Tree<P:R> right in P:R;

	Tree() {}

	void print() {
		System.out.println(data);
		left.print();
		right.print();
	}
}