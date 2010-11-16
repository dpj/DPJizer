/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * An implementation of DPJ sparse array, with subarrays and
 * partitioning
 * 
 * @author Rob Bocchino
 *
 * @param <T>
 */

package DPJRuntime;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;

public class DPJSparseArray<type T extends Comparable<T>, region R> {
	protected final SortedMap<Integer,T> elts in R;
	public final int start in R;
	public final int length in R;
	public final T zero in R;

	public DPJSparseArray(int length, T zero) 
	//    pure 
	{
		this.elts = new TreeMap<Integer,T>();
		this.start = 0;
		this.length = length;
		this.zero = zero;
	}

	protected DPJSparseArray(SortedMap<Integer,T> elts, int start, 
			int length, T zero) 
	//	pure 
	{
		this.elts = elts;
		this.start = start;
		this.length = length;
		this.zero = zero;
	}

	public void clear() {
		elts.clear();
	}

	public T get(int idx) 
	//	reads R, R:[start + idx] 
	{ 
		if (idx < 0 || idx > length-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		T result = elts.get(idx);
		return (result == null) ? zero : result;
	}

	public void put(int idx, T val) 
	//	reads R writes R:[start + idx] 
	{
		if (idx < 0 || idx > length-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (val != zero && (zero == null || val.compareTo(zero) != 0)) {
			elts.put(start+idx, val);
		}
	}

	public DPJSparseArray<T,R> subarray(int start, int length) 
	//	reads R 
	{
		if (start < 0 || length < 0 || 
				start + length > this.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return new DPJSparseArray<T,R>(elts, this.start + start, 
				length, zero);
	}

	public DPJSet<Integer,R> nonZeroIndices() 
	//	reads R 
	{
		java.util.Set<Integer> keys = 
			elts.subMap(start, start+length).keySet();
		return new DPJSetWrapper<Integer,R>(keys);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("DPJSparseArray[" + start + ".." + (start+length-1)
				+ ",zero="+zero+"] ");
		for (DPJIterator<Integer,R> I = nonZeroIndices().iterator(); I.hasNext(); ) {
			int i = I.next();
			sb.append(i);
			sb.append("=");
			sb.append(this.get(i));
			sb.append(" ");
		}
		return sb.toString();
	}


	public static void main(String[] args) {
		DPJSparseArray<Integer> A1= new DPJSparseArray<Integer>(10, 0);
		A1.put(0,5);
		A1.put(5,10);
		A1.put(9,0);
		System.out.println(A1);
		DPJSparseArray<Integer> seg = A1.subarray(0,5);
		System.out.println(seg);
	}
}