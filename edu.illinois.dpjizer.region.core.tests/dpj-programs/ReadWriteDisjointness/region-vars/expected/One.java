package readwritedisjointness;

class C {
    
    C() {
        super();
    }
    int c;
    int[]<Pi1>#idx1 a;
    int[]<Pi2>#idx2 b;
    
    void m() {
        foreach (int i in 0, a.length) {
            a[i] = a[i] + b[i] + c;
        }
        foreach (int j in 0, b.length) {
            b[j] = a[j] + b[j] + c;
        }
    }
}
