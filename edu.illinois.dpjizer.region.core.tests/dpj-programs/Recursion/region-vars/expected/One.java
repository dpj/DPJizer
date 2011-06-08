package readwritedisjointness;

class Tree<region P> {
    
    Tree() {
        super();
    }
    int data1 in P;
    int data2 in P;
    region L;
    region R;
    readwritedisjointness.Tree<Pi1>[]<Pi2>#idx1 children in P;
    
    void visitAll() {
        data1 = data2 + 1;
		for (int k in 0, children.length) {
            children[k].visitAll();
        }
    }
}