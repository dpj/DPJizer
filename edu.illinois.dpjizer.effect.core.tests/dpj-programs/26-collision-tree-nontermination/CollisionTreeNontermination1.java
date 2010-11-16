/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
class CT<region P> {
	region L;

	protected CT<P:L> l in P:L;

	public CT() {
	}

	public <region R>boolean intersect(CT<R> collisionTree) 
	{
		return collisionTree.intersect(l);
	}
}
