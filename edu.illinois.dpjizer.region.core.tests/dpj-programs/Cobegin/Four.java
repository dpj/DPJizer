package four;

class C<region P> {
	int a in P;
	C c1 in P;
	C c2 in P;
	
	C m1() {
		a = 0;
		return this;
	}
	
	void m2() {
		c1 = new C();
		c2 = c1.m1();
		
		cobegin {
			c1.m1();
			c2.m1();
		}
	}
}
