/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Point class containing all the information corresponding to a single point
 * @author Rakesh Komuravelli
 *
 */

public class Point {

	/** Features of the point */
	float[] features;

	/** Number of features */
	int     numFeatures;

	/** membership center index */
	int     membership;

	/**
	 * Constructor
	 * @param numFeatures Number of features
	 */
	public Point(int numFeatures)
	//	none
	{
		this.numFeatures = numFeatures;
		this.features    = new float[numFeatures];
		for (int i=0;i<numFeatures;i++)
			features[i] = 0.0f;
	}

	/**
	 * Get the features
	 * @return the features
	 */
	public float[] getFeatures() 
	//	reads Root
	{
		return features;
	}

	/**
	 * Get the number of features
	 * @return the numFeatures
	 */
	public int getNumFeatures()
	//	none
	{
		return numFeatures;
	}

	/**
	 * Get the cluster number to which this point belongs to
	 * @return membership id
	 */
	public int getMembership()
	//	none
	{
		return membership;
	}

	/**
	 * Set the cluster number to which this point belongs to
	 * @param membership membership id
	 */
	public void setMembership(int membership)
	//	none
	{
		this.membership = membership;
	}

	/**
	 * Get the feature at position pos
	 * @param pos index
	 * @return feature at position pos
	 */
	public float getFeature(int pos)
	//	none
	{
		return features[pos];
	}

	/**
	 * Set the feature
	 * @param feature feature to set
	 * @param pos index into features array
	 */
	public void setFeature(float feature, int pos)
	//	none
	{
		features[pos] = feature;
	}

	/**
	 * Add the input features to the features of this point
	 * @param features
	 */
	public void addFeatures(float[] features)
	//	none
	{
		for (int i=0;i<numFeatures;i++)
			this.features[i] += features[i];
	}

	/**
	 * Copy method
	 * @return a copy of this point
	 */
	public Point copy()
	//	none
	{
		Point copyPoint = new Point(numFeatures);
		for(int i = 0; i < numFeatures; i++)
			copyPoint.setFeature(features[i], i);
		return copyPoint;
	}

	/**
	 * toString for Point
	 */
	public String toString()
	//	none
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < numFeatures; i++) {
			sb.append(features[i]);
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Update the membership
	 * @param index
	 * @return
	 */
	public boolean updateMembership(int index)
	//	none
	{
		if (membership != index) {
			membership = index;
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Clear the features of the point
	 */
	public void clear()
	//	none
	{
		for (int i=0;i<numFeatures;i++)
			features[i] = 0.0f;
	}
}