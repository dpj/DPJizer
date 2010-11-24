package seven;

class C<region P> {
	int a in P;

	void m1() {
		a = 0;
	}

	void m2(C c) {
		C c1 = new C();
		C c2 = new C();

		c.m1();
		cobegin {
			c1.m1();
			c2.m1();
		}
	}
}
