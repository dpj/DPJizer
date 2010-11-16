/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
public abstract class Node {
	region r;
	
	public Node() { }

	protected abstract <region R5>boolean subdivp(Node p, HGStruct<R5> hg) ;
}