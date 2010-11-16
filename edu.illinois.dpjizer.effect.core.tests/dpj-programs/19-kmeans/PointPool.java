/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

import java.util.concurrent.ConcurrentHashMap;

public class PointPool {

	private ConcurrentHashMap<Point, Boolean> new_centers;
	Point sumFeature;

	PointPool(int nfeatures, int capacity, float loadfactor, int concurrencyLevel)
	//	none
	{
		new_centers = new ConcurrentHashMap<Point, Boolean>(capacity, loadfactor, concurrencyLevel);
		sumFeature = new Point(nfeatures);
	}

	void putPoint(Point point)
	//	none
	{
		new_centers.put(point, true);
	}

	void getObjectSum()
	//	none
	{
		for (Point p : new_centers.keySet()) {
			sumFeature.addFeatures(p.getFeatures());
		}
	}

	/**
	 * @return the sumFeature
	 */
	public Point getSumFeature()
	//	none
	{
		return sumFeature;
	}

	public int size()
	//	none
	{
		return new_centers.size();
	}

	public void clear()
	//	none
	{
		sumFeature.clear();
		new_centers.clear();
	}
}
