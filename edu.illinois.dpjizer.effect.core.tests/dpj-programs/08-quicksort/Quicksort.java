/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Parallel Quicksort
 * @author Robert L. Bocchino Jr.
 * June 2008
 */

import DPJRuntime.*;

public class Quicksort {

	public final int seqLength;
	private final Partition partition;

	public Quicksort(int seqLength) {
		this.seqLength = seqLength;
		partition = new Partition(seqLength);
	}

	/**
	 * Find the index of the median of the elements in an array
	 */
	public <region R12> int median(DPJArrayInt<R12> A, int center) 
	//	writes R, R:[?] 
	{
		int p = seqPartitionFirst(A);
		if (p == center) {
			return p;
		} else if (p < center) {
			return p + 1 + median(A.subarray(p + 1, A.length - p - 1), 
					center - p - 1);
		} else {
			return median(A.subarray(0, p), center);
		}
	}

	public <region R13> int median(int[]<R13> A) {
		return median(new DPJArrayInt<R13>(A), A.length >> 1);
	}

	/**
	 * Shuffle elements of A and return index of pivot element.  Use
	 * the first element as the pivot.
	 */
	<region R14> int seqPartitionFirst(DPJArrayInt<R14> A) 
	//	writes R, R:[?] 
	{
		int left = 0, right = A.length-1;
		while (left != right) {
			if (A.get(left) > A.get(left+1)) {
				A.swap(left, left+1);
				++left;
			} else {
				A.swap(left+1, right);
				--right;
			}
		}
		return left;
	}

	public <region R15>int partitionFirst(DPJArrayInt<R15> A) 
	//	writes R:* 
	{
		if (A.length <= seqLength) {
			return seqPartitionFirst(A);
		}
		return partition.partition(A);
	}

	/**
	 * Same as partitionFirst, but use the median of the first log n
	 * elements as the pivot.
	 */
	<region R16>int partitionMedian(DPJArrayInt<R16> A)
	//	writes R:* 
	{
		int logSize = DPJUtils.log2(A.length);
		int p = median(A.subarray(0, logSize), logSize >> 1);
		A.swap(0, p);
		return partitionFirst(A);
	}

	public <region R17> void seqSort(DPJArrayInt<R17> A) 
	//	writes R:* 
	{
		if (A.length < 2) return;
		int p = seqPartitionFirst(A);
		DPJPartitionInt<R17> segs = 
			new DPJPartitionInt<R17>(A, p, true);
		seqSort(segs.get(0));
		seqSort(segs.get(1));
	}

	/**
	 * Parallel quicksort
	 */
	public <region R18>void sort(DPJArrayInt<R18> A)
	//	writes R, R:* 
	{
		if (A.length < 2) {
			return;
		} else {
			if (A.length <= seqLength)
				seqSort(A);
			else {
				int p = partition.partition(A);
				final DPJPartitionInt<R18> segs = 
					new DPJPartitionInt<R18>(A, p, true);
				cobegin {
					sort(segs.get(0));
					sort(segs.get(1));
				}
			}
		}
	}

	public <region R19> void sort(int[]<R19> A) {
		sort(new DPJArrayInt<R19>(A));
	}

}
