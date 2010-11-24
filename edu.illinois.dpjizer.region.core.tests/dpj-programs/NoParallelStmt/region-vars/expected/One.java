package one;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    void m2() {
        one.C<Pi1> c2 = new one.C<Pi2>();
        c2.m1();
    }
}
