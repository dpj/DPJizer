package four;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    four.C<Pi1> c1 in P;
    four.C<Pi2> c2 in P;
    
    four.C<Pi3> m1() {
        a = 0;
        return this;
    }
    
    void m2() {
        c1 = new four.C<Pi4>();
        c2 = c1.m1();
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}