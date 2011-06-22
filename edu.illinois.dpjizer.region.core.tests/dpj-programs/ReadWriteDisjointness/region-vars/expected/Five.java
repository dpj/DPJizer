package readwritedisjointness;

class C {
    
    C() {
        super();
    }
    int[]<Pi1>#idx1 a;
    int[]<Pi2>#idx2 b;
    int[]<Pi3>#idx3 c;
    readwritedisjointness.D<Pi4,Pi5>[]<Pi6>#idx4 d;
    
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
    
    D() {
        super();
    }
    int f in P;
}
