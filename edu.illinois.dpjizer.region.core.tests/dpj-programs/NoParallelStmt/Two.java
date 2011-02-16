package two;

class C {
	
	region r;
	Object a in r;
	
	void m1(Object b) {
		a = b;
	}
	
	void m2() {
		m1(this);
	}
}
