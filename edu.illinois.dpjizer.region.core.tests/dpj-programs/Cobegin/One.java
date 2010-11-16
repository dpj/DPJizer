package one;

class C<region P> {
	int a in P;
	
	void m1() {
		a = 0;
	}
	
	void m2() {
		cobegin {
			new C().m1();
			new C().m1();
		}
	}
}
