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
        cobegin {
            new one.C<Pi1>().m1();
            new one.C<Pi2>().m1();
        }
    }
}