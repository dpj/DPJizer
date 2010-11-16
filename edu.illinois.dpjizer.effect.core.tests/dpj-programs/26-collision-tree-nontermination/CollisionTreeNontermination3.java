/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
class CT<region P1, P2> {
	region L, R;

	protected CT<P1:L, P2> l in P1:L;
	protected CT<P1:R, P2> r in P1:R;

	public CT() {
	}

	public <region Q>void intersect1(CT<Q, Q> ct) {
		this.<region Q, Q>intersect2(ct);
	}
	
	public <region S1, S2>void intersect2(CT<S1, S2> ct) 
	{
		l = null;
		r = null;
		ct.intersect2(l);
		ct.<region P1:R, P2>intersect2(r);
	}
	
}
