package readwritedisjointness;

class C {
    
    C() {
        super();
    }
    readwritedisjointness.D<Pi1>[]<Pi2>#idx1 a;
    readwritedisjointness.D<Pi3>[]<Pi4>#idx2 b;
    readwritedisjointness.D<Pi5>[]<Pi6>#idx3 c;
    
    void m() {
        foreach (int i in 0, a.length) {
            if (a[0].e.f + b[0].e.f == 0) {
                c[i].e.f = 0;
            }
        }
        a[0].e = b[0].e;
    }
}
class D<region P> {
    
    D() {
        super();
    }
    readwritedisjointness.E<Pi7> e in P;
}
class E<region R> {
    
    E() {
        super();
    }
    int f in R;
}
