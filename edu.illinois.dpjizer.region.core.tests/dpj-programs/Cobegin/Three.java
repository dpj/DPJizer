package three;

class C<region P> {
	int a in P;
	C c1 in P;
	C c2 in P;
	
	void m1() {
		a = 0;
	}
	
	void m2() {
		cobegin {
			c1.c2.m1();
			c2.c1.m1();
		}
	}
}
