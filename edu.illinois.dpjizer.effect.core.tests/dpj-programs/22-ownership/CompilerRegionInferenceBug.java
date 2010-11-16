/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * @author Mohsen Vakilian
 */
class C<region P1> {
	int a in this;

	public C()
	//	pure
	{

	}

	public <region P2>void m1()
	//	writes P2 : *
	{
		m2(new C<P2>());
	}

	private <region P3>void m2(final C<P3> c)
	//	writes c
	{
		c.a = 1;
	}
}