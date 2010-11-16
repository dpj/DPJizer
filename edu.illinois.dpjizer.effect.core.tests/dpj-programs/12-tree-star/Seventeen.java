/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */

/**
 * 
 * @author Mohsen Vakilian
 * 
 */
class Partition {
	public Partition() {
	}

	<region R1>int sequentialPartition(DPJArrayInt<R1> A) 
	//	writes R1 
	{
		A.swap(0, 1);
		return 0;
	}

	/*
	 * Inlining sequentialPartition() would cause the algorithm to terminate.
	 * It's important to have both metho invocations sequentialPartition() and  partitionHelperRight().
	 */
	public <region R2>int partition(DPJArrayInt<R2> data) {
		sequentialPartition(data);

		final DPJPartitionInt<R2> halves = new DPJPartitionInt<R2>();
//		DPJPartitionInt<halves:[0]> left_;
		DPJPartitionInt<halves:[1]> right_;
//		left_ = new DPJPartitionInt<halves:[0]>(halves.get(0), partition(halves.get(0)));
		right_ = this.<region halves:[1]>partitionHelperRight(halves.get(1), 0);
		return 0;
	}
	
	public <region R3>DPJPartitionInt<R3> partitionHelperRight(DPJArrayInt<R3> A, int pivot) 
	//	writes R3:* 
	{
		int oldValue = A.get(0);
		A.put(0, pivot);
		int pivotIndex = partition(A);
		A.put(pivotIndex, oldValue);
		if (oldValue < pivot) ++pivotIndex;
		final DPJPartitionInt<R3> result = new DPJPartitionInt<R3>(A, pivotIndex);
		return result;
	}
}

class DPJPartitionInt<region R4> {
	private final DPJArrayInt<R4> A = null;
	private final DPJArrayInt<this:[i]>[]<this:[i]>#i elts;

	public DPJPartitionInt() {
		elts = null;
	}

	public DPJPartitionInt(DPJArrayInt<R4> A, final int idx)  {
		int length = 0;
		elts = new DPJArrayInt<this:[i]>[length]<this:[i]>#i;
		elts[0] = new DPJArrayInt<this:[0]>();
		elts[1] = new DPJArrayInt<this:[1]>();
	}

	public DPJArrayInt<this : [idx]> get(final int idx) {
		return elts[idx];
	}

}

class DPJArrayInt<region R5> {
	protected final int[]<R5> elts in R5;
	public final int start in R5;

	public DPJArrayInt() {
		elts = null;
		this.start = 0;
	}

	public void swap(int i, int j) 
	//	writes R5
	{
		int tmp = elts[start+i];
		elts[start+i] = elts[start+j];
		elts[start+j] = tmp;
	}

	public int get(final int idx) 
	//	reads R5 
	{ 
		return elts[start+idx]; 
	}

	public void put(int idx, int val) 
	//	writes R5 
	{
		elts[start+idx] = val; 
	}

}