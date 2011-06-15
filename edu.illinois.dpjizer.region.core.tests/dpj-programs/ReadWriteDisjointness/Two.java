package readwritedisjointness;

class C1 {

	C2 c;

	int a[], b[];

	void m() {
		foreach (int i in 0, a.length) {
			a[i] = a[i] + b[i] + c.f;
		}
	}

}

class C2<region P> {

	int f in P;

}