/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/** WARNING:  THIS FILE IS AUTO-GENERATED */

/**
 * A templated implementation of DPJ Array
 * 
 * @author Rob Bocchino
 *
 */

package DPJRuntime;

public class DPJArrayInt<region R> {
	protected final int[]<R> elts in R;
	public final int start in R;
	public final int length in R;

	public DPJArrayInt(int length) 
	//	pure 
	{
		this.elts = new int[length]<R>;
		this.start = 0;
		this.length = length;
	}

	public DPJArrayInt(int[]<R> elts) 
	//	pure 
	{
		this.elts = elts;
		this.start = 0;
		this.length = elts.length;
	}

	protected DPJArrayInt(int[]<R> elts, int start, int length) 
	//	pure 
	{
		this.elts = elts;
		this.start = start;
		this.length = length;
	}

	public int get(final int idx) 
	//	reads R 
	{ 
		if (idx < 0 || idx > length-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return elts[start+idx]; 
	}

	public void put(int idx, int val) 
	//	writes R 
	{
		if (idx < 0 || idx > length-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		elts[start+idx] = val; 
	}

	public DPJArrayInt<R> subarray(int start, int length) 
	//	pure 
	{
		if (start < 0 || length < 0 || 
				start + length > this.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return new DPJArrayInt<R>(elts, this.start + start, length);
	}

	public int[]<R> toArray() 
	//	pure 
	{ return elts; }

	public String toString() 
	//	reads R
	{
		StringBuffer sb = new StringBuffer();
		if (length > 0) {
			sb.append(this.get(0));
			for (int i = 1; i < length; ++i) {
				sb.append(" ");
				sb.append(this.get(i));
			}
		}
		return sb.toString();
	}

	/**
	 * Swap two array elements
	 */
	public void swap(int i, int j) 
	//	writes R 
	{
		int tmp = elts[start+i];
		elts[start+i] = elts[start+j];
		elts[start+j] = tmp;
	}

}
