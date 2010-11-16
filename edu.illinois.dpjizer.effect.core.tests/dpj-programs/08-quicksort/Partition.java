/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Parallel Partition for Quicksort
 * @author Robert L. Bocchino Jr.
 * August 2008
 */

import DPJRuntime.*;
import java.util.*;

public class Partition {

	public final int seqLength;

	public Partition(int seqLength) {
		this.seqLength = seqLength;
	}

	/**
	 * Shuffle elements of A and return index of pivot element.  Use
	 * the first element as the pivot.
	 */
	<region R9>int sequentialPartition(DPJArrayInt<R9> A) 
	//    writes R 
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

	public <region R10>int partition(DPJArrayInt<R10> data) 
	//    writes R:* 
	{
		if (data.length == 1) return 0;
		if (data.length <= seqLength)
			return sequentialPartition(data);
		int pivotValue = data.get(0);
		int pivotIndex;
		final DPJPartitionInt<R10> halves = new DPJPartitionInt<R10>(data, data.length/2);
		DPJPartitionInt<halves:[0]:*> left_;
		DPJPartitionInt<halves:[1]:*> right_;
		cobegin {
			left_ = new DPJPartitionInt<halves:[0]:*>(halves.get(0), 
					partition(halves.get(0)));
			right_ = this.<region halves:[1]:*>partitionHelperRight(halves.get(1), 
					pivotValue);
		}
		final DPJPartitionInt<halves:[0]:*> left = left_;
		final DPJPartitionInt<halves:[1]:*> right = right_;
		pivotIndex = left.get(0).length + right.get(0).length;
		int[]<Local:[i]>#i tmp = new int[left.get(1).length]<Local:[i]>#i;
		foreach (int i in 0, left.get(1).length)
		tmp[i] = left.get(1).get(i);
		int[]<Local> arr = new int[2]<Local>;
		arr[0] = left.get(0).length;
		arr[1] = left.get(0).length + right.get(0).length;
		final DPJPartitionInt<R10> tmpp = 
			new DPJPartitionInt<R10>(data, arr);
		foreach (int i in 0, right.get(0).length) {
			tmpp.get(1).put(i, right.get(0).get(i));
		}
		foreach (int i in 0, left.get(1).length) {
			tmpp.get(2).put(i, tmp[i]);
		}
		return pivotIndex;
	}

	public <region R11>DPJPartitionInt<R11> 
	partitionHelperRight(DPJArrayInt<R11> A, int pivot) 
	//	writes R1:* 
	{
		int oldValue = A.get(0);
		A.put(0, pivot);
		int pivotIndex = partition(A);
		A.put(pivotIndex, oldValue);
		if (oldValue < pivot) ++pivotIndex;
		final DPJPartitionInt<R11> result = new DPJPartitionInt<R11>(A, pivotIndex);
		return result;
	}

}