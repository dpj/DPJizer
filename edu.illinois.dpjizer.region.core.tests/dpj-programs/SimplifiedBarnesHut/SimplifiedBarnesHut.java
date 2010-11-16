package simplifiedbarneshut;

class HGStruct<region HR> {
	Vector acc0 in HR;
}

class Vector<region VR> {
	double[] elts in VR = new double[3];

	void SETV(Vector u) {
		for (int m = 0; m < 3; m++) {
			elts[m] = u.elts[m];
		}
	}
}

class Body<region BR> {
	Vector vel in BR = new Vector();

	void hackgrav(HGStruct hg) {
		vel.SETV(hg.acc0);
	}
}

class Tree {
	Body[] bodies;

	void computegrav() {
		foreach (int j in 0, bodies.length) {
			HGStruct hg = new HGStruct();
			bodies[j].hackgrav(hg);
		}
	}
}
