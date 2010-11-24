package six;

class C1<region P1, P2> {
    
    C1() {
        super();
    }
    int a in P1;
    int b in P2;
    
    void m1() {
        a = 0;
        b = 0;
    }
    
    void m2(six.C1<Pi1,Pi2> c1, six.C2<Pi3,Pi4> c2) {
        cobegin {
            c1.m1();
            c2.m1();
        }
    }
}
class C2<region P1, P2> {
    
    C2() {
        super();
    }
    int a in P1;
    int b in P2;
    
    void m1() {
        a = 0;
        b = 0;
    }
}
