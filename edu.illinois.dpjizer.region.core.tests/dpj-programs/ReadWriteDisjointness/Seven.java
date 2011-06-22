package readwritedisjointness;

class C {

	D a[];
	D b[];
	D c[];

	void m() {
		foreach (int i in 0, a.length) {
			if (a[0].e.f + b[0].e.f == 0) {
				c[i].e.f = 0;
			}
		}
		a[0].e = b[0].e;
	}

}

class D<region P> {
	E e in P;
}

class E<region R> {
	int f in R;
}