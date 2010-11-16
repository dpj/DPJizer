package three;

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
    
    <region P1_c1, P2_c1, P1_c2, P2_c2>void m2(C1 c1, C2 c2) {
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