package seven;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    void m2(seven.C<Pi1> c) {
        seven.C<Pi2> c1 = new seven.C<Pi3>();
        seven.C<Pi4> c2 = new seven.C<Pi5>();
        c.m1();
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}
