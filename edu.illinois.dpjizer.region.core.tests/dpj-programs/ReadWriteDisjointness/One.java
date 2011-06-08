package readwritedisjointness;

class C {
	
	int c;
	
	int a[], b[];
	
	void m() {
		foreach (int i in 0, a.length) {
			a[i] = a[i] + b[i] + c;
		}
		
		foreach (int j in 0, b.length) {
			b[j] = a[j] + b[j] + c;
		}
	}
	
}