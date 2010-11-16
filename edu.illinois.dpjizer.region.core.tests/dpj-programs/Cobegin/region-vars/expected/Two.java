package two;

class C<region P> {
    
    C() {
        super();
    }
    int a in P;
    
    void m1() {
        a = 0;
    }
    
    void m2() {
        two.C<Pi1> c1;
        two.C<Pi2> c2;
        c1 = new two.C<Pi3>();
        c2 = new two.C<Pi4>();
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}