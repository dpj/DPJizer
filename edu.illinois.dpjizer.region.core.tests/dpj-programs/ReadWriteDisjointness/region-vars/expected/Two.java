package readwritedisjointness;

class C1 {
    
    C1() {
        super();
    }
    readwritedisjointness.C2<Pi1> c;
    int[]<Pi2>#idx1 a;
    int[]<Pi3>#idx2 b;
    
    void m() {
        foreach (int i in 0, a.length) {
            a[i] = a[i] + b[i] + c.f;
        }
    }
}
class C2<region P> {
    
    C2() {
        super();
    }
    int f in P;
}
