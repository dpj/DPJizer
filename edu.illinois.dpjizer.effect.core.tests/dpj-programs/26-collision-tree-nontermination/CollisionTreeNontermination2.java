/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
class CT<region P> {
	region L, R;

	protected CT<P:L> l in P:L;
	protected CT<P:R> r in P:R;

	public CT() {
	}

	public <region Q>void intersect(CT<Q> collisionTree) 
	{
		collisionTree.intersect(l);
		collisionTree.<region P:R>intersect(r);
	}
}
