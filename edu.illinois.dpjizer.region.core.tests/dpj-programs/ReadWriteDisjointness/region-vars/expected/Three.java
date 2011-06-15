package readwritedisjointness;

class C {
    
    C() {
        super();
    }
    int[]<Pi1>#idx1 a;
    int[]<Pi2>#idx2 b;
    int[]<Pi3>#idx3 c;
    
    void m() {
        foreach (int i in 0, a.length) {
            if (a[0] + b[0] == 0) {
                c[i] = 0;
            }
        }
        a = b;
    }
}
