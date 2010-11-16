package two;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    <region P_c1, P_c2>void m2(C c1, C c2) {
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}