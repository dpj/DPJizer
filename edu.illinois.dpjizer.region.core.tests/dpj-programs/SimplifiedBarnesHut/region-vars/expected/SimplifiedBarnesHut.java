package simplifiedbarneshut;

class HGStruct<region HR> {
    
    HGStruct() {
        super();
    }
    simplifiedbarneshut.Vector<Pi1> acc0 in HR;
}
class Vector<region VR> {
    
    Vector() {
        super();
    }
    double[]<Pi2>#idx1 elts in VR = new double[3]<Pi8>#idx2;
    
    <region SR>void SETV(simplifiedbarneshut.Vector<Pi3> u) {
        for (int m = 0; m < 3; m++) {
            elts[m] = u.elts[m];
        }
    }
}
class Body<region BR> {
    
    Body() {
        super();
    }
    simplifiedbarneshut.Vector<Pi4> vel in BR = new simplifiedbarneshut.Vector<Pi9>();
    
    void hackgrav(simplifiedbarneshut.HGStruct<Pi5> hg) {
        vel.<region Pi10>SETV(hg.acc0);
    }
}
class Tree {
    
    Tree() {
        super();
    }
    simplifiedbarneshut.Body<Pi6>[]<Pi7>#idx3 bodies;
    
    void computegrav() {
        foreach (int j in 0, bodies.length) {
            simplifiedbarneshut.HGStruct<Pi11> hg = new simplifiedbarneshut.HGStruct<Pi12>();
            bodies[j].hackgrav(hg);
        }
    }
}
