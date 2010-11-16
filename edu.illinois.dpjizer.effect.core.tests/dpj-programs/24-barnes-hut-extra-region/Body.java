/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
public class Body<region R1> extends Node {

	public Body() 
	{}

	void hackgrav(HGStruct<R1> hg, Node root)
	//    reads r writes R
	{	
		walksub(root, hg);
	}

	protected void walksub(Node p, HGStruct<R1> hg)
	//    reads r writes R
	{
		p.subdivp(p, hg);
	}

	@Override
	protected <region R2> boolean subdivp(Node p, HGStruct<R2> hg) {
		return hg.a == 0;
	}
}
