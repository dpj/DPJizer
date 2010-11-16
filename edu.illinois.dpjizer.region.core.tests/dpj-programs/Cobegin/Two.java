package two;

class C<region P> {
	int a in P;
	
	void m1() {
		a = 0;
	}
	
	void m2() {
		C c1;
		C c2;
		
		c1 = new C();
		c2 = new C();
		
		cobegin {
			c1.m1();
			c2.m1();
		}
	}
}
