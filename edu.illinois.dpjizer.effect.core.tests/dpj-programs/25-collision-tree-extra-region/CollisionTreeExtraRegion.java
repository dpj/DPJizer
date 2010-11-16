/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
abstract class BoundingVolume<region RVolume> {

	Object center in RVolume;

	public BoundingVolume() {}

}

class BoundingBox<region RBox> extends BoundingVolume<RBox> {
	public BoundingBox() {}

	public void MyBoundingBox()
	{
		this.center = null;
	}
}

