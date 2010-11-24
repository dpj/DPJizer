package five;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    void m2(five.C<Pi1> c1, five.C<Pi2> c2) {
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}
