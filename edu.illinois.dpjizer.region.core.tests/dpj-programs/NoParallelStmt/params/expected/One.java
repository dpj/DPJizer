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
        C c2 = new C();
        c2.m1();
    }
}