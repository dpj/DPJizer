package readwritedisjointness;

class C {

	int a[], b[], c[];

	void m() {
		foreach (int i in 0, a.length) {
			if (a[0] + b[0] == 0) {
				c[i] = 0;
			}
		}
		a = b;
	}

}
