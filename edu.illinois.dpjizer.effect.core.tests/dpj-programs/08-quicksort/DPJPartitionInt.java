/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/** WARNING:  THIS FILE IS AUTO-GENERATED */

/**
 * A templated implementation of DPJ Partition, i.e., an array of disjoint
 * subarrays
 * 
 * @author Rob Bocchino
 *
 */

package DPJRuntime;

public class DPJPartitionInt<region R2> {

	private final DPJArrayInt<R2> A;
	private final DPJArrayInt<this:[i]>[]<this:[i]>#i elts;
	public final int length;
	private final int stride;

	/**
	 * Chop an array 0..n-1 into two subarrays:
	 *
	 *   0..idx-1
	 *   idx..n-1
	 */
	public DPJPartitionInt(DPJArrayInt<R2> A, final int idx) 
	//	writes this:[?] 
	{
		this.A = A;
		this.length = 2;
		this.stride = 0;
		elts = new DPJArrayInt<this:[i]>[length]<this:[i]>#i;
		elts[0] = (DPJArrayInt<this:[0]>) A.subarray(0, idx);
		elts[1] = (DPJArrayInt<this:[1]>) A.subarray(idx, A.length - idx);
	}

	public DPJPartitionInt(DPJArrayInt<R2> A, final int idx, boolean open) 
	//	writes this:[?]
	{
		this.A = A;
		this.length = 2;
		this.stride = 0;
		this.elts = new DPJArrayInt<this:[i]>[length]<this:[i]>#i;
		elts[0] = (DPJArrayInt<this:[0]>) A.subarray(0, idx);
		if (open) {
			elts[1] = (DPJArrayInt<this:[1]>) A.subarray(idx + 1, A.length - idx - 1);
		} else {
			elts[1] = (DPJArrayInt<this:[1]>) A.subarray(idx, A.length - idx);
		}
	}

	/* 'strided' is a dead arg here: this is a bit of a hack to disambiguate
       this constructor from the other one that takes an array and a stride */
	private DPJPartitionInt(DPJArrayInt<R2> A, int stride, double strided) 
	//	pure 
	{
		this.A = A;
		this.stride = stride;
		this.length = (A.length / stride) + ((A.length % stride == 0) ? 0 : 1);
		this.elts = null;
	}
	
	public static <region R3>DPJPartitionInt<R3> 
	stridedPartition(DPJArrayInt<R3> A, int stride) /* pure */ {
		return new DPJPartitionInt<R3>(A, stride, 0.0);
	}

	/**
	 * Chop an array 0..n-1 into (idxs.length+1) subarrays:
	 *
	 *   0..idxs[0]-1
	 *   idxs[0]..idxs[1]-1
	 *   ...
	 *   idxs[idxs.length-1]-1..n-1
	 */
	public <region RA>DPJPartitionInt(DPJArrayInt<R2> A, int[]<RA> idxs) 
	//	reads RA writes this:[?] 
	{
		this.A = A;
		this.length = idxs.length+1;
		this.elts = new DPJArrayInt<this:[i]>[length]<this:[i]>#i;
		this.stride = 0;
		if (length == 1)
			elts[0] = (DPJArrayInt<this:[0]>) A;
		else {
			int i = 0, len = 0;
			elts[0] = (DPJArrayInt<this:[0]>) A.subarray(0, idxs[0]);
			for (i = 1; i < idxs.length; ++i) {
				len = idxs[i] - idxs[i-1];
				if (len < 0) 
					throw new ArrayIndexOutOfBoundsException();
				final int j = i;
				elts[j] = (DPJArrayInt<this:[j]>) A.subarray(idxs[j-1], len);	    
			}
			i = idxs[idxs.length - 1];
			len = A.length - i;
			final int length = idxs.length;
			elts[length] = (DPJArrayInt<this:[length]>) A.subarray(i, len);
		}
	}

	public DPJArrayInt<this : [idx] : *> get(final int idx) 
	//	reads this:[idx]
	{
		if (idx < 0 || idx > length-1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (elts != null)
			return elts[idx];
		else {
			int start = idx * stride;
			int segLength = (start + stride > A.length) ? (A.length - start) : stride;
			return (DPJArrayInt<this:[idx]:*>) A.subarray(start, segLength);
		}
	}
}
