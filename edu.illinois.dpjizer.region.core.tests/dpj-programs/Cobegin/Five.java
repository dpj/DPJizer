package five;

class C<region P> {
	int a in P;

	void m1() {
		a = 0;
	}

	void m2(C c1, C c2) {
		cobegin {
			c1.m1();
			c2.m1();
		}
	}
}
