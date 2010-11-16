package one;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    <region P_c>void m2(C c) {
        C c1 = new C();
        C c2 = new C();
        c.m1();
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}