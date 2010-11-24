package three;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    three.C<Pi1> c1 in P;
    three.C<Pi2> c2 in P;
    
    void m1() {
        a = 0;
    }
    
    void m2() {
        cobegin {
            c1.c2.m1();
            c2.c1.m1();
        }
    }
}