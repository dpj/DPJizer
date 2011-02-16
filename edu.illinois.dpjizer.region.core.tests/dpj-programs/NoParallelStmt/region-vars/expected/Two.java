package two;

class C {
    
    C() {
        super();
    }
    region r;
    java.lang.Object a in r;
    
    void m1(java.lang.Object b) {
        a = b;
    }
    
    void m2() {
        m1(this);
    }
}
