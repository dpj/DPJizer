package readwritedisjointness;

class Tree<region P> {
	int data1 in P;
	int data2 in P;
	region L, R;
	Tree[] children in P;
	
	void visitAll() {
		data1 = data2 + 1;
		for (int k in 0, children.length) {
			children[k].visitAll();
		}
	}
}