/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
public class Tree {

	public Node root;
	public Body<[i]>[]#i bodies;

	public Tree() {}

	void computegrav() {
		HGStruct<[0]> hg = new HGStruct<[0]>();
		bodies[0].hackgrav(hg, root);
	}

}

