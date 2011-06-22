package readwritedisjointness;

class C {

	int a[], b[], c[];
	D d[];

	void m() {
		foreach (int i in 0, a.length) {
			if (a[0] + b[0] + d[0].f == 0) {
				c[i] = 0;
			}
		}
		a = b;
	}

}

class D<region P, Q> {
	int f in P;
}