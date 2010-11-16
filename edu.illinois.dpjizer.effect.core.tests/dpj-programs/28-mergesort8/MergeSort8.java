/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
import DPJRuntime.*;
import java.util.Random;

/**
 * 8-way split version of merge sort
 */

public class MergeSort8 extends MergeSort {

	public MergeSort8(String[] args) {
		super("MergeSort8",args);
	}

	@Override
	public <region P1,P2 | P1:* # P2:*>
	void sort(DPJArrayInt<P1> A, DPJArrayInt<P2> B) {
		sort(A, B, true);
	}

	public <region P3,P4 | P3:* # P4:*>
	boolean sort(DPJArrayInt<P3> A, DPJArrayInt<P4> B, boolean parity) 
	//	writes P3:*, P4:* 
	{
		if (A.length <= QUICK_SIZE) {
			if(parity)
				quickSort(A);
			else
				quickSort(B);
			return parity;
		} else {
			int q = A.length/8;

			int[]<Local> idxs = new int[7]<Local>;
			idxs[0] = q;
			idxs[1] = 2*q;
			idxs[2] = 3*q;
			idxs[3] = 4*q;
			idxs[4] = 5*q;
			idxs[5] = 6*q;
			idxs[6] = 7*q;

			int[]<Local> quart_idxs = new int[3]<Local>;
			quart_idxs[0] = 2*q;
			quart_idxs[1] = 4*q;
			quart_idxs[2] = 6*q;

			final DPJPartitionInt<P3> A_eighths = new DPJPartitionInt<P3>(A, idxs);
			final DPJPartitionInt<P4> B_eighths = new DPJPartitionInt<P4>(B, idxs);
			final DPJPartitionInt<P3> A_quarters = new DPJPartitionInt<P3>(A, quart_idxs);
			final DPJPartitionInt<P3> A_halves = new DPJPartitionInt<P3>(A, 4*q);
			final DPJPartitionInt<P4> B_quarters = new DPJPartitionInt<P4>(B, quart_idxs);
			final DPJPartitionInt<P4> B_halves = new DPJPartitionInt<P4>(B, 4*q);

			boolean subparity;

			cobegin {
				subparity = sort(A_eighths.get(0), B_eighths.get(0), parity);
				sort(A_eighths.get(1), B_eighths.get(1), parity);
				sort(A_eighths.get(2), B_eighths.get(2), parity);
				sort(A_eighths.get(3), B_eighths.get(3), parity);
				sort(A_eighths.get(4), B_eighths.get(4), parity);
				sort(A_eighths.get(5), B_eighths.get(5), parity);
				sort(A_eighths.get(6), B_eighths.get(6), parity);
				sort(A_eighths.get(7), B_eighths.get(7), parity);
			}

			if (subparity) {
				MergeSort8.<region P3,P4>sort_quarters(A_eighths, B_quarters);
				MergeSort8.<region P3,P4>sort_halves(A_quarters, B_halves);
				merge(A_halves.get(0), A_halves.get(1), B);
			} else {
				MergeSort8.<region P4,P3>sort_quarters(B_eighths, A_quarters);
				MergeSort8.<region P4,P3>sort_halves(B_quarters, A_halves);
				merge(B_halves.get(0), B_halves.get(1), A);
			}

			return !subparity;
		}
	}

	private static <region P5,P6 | P5:* # P6:*>void 
	sort_quarters(final DPJPartitionInt<P5> A_eighths, final DPJPartitionInt<P6> B_quarters) 
	//	reads P1:* writes P2:* 
	{
		cobegin {
			merge(A_eighths.get(0), A_eighths.get(1), B_quarters.get(0));
			merge(A_eighths.get(2), A_eighths.get(3), B_quarters.get(1));
			merge(A_eighths.get(4), A_eighths.get(5), B_quarters.get(2));
			merge(A_eighths.get(6), A_eighths.get(7), B_quarters.get(3));
		}
	}

	private static <region P7,P8 | P7:*#P8:*>void
	sort_halves(final DPJPartitionInt<P7> quarters,	final DPJPartitionInt<P8> halves) 
	//	reads P1:* writes P2:* 
	{
		cobegin {
			merge(quarters.get(0), quarters.get(1),	halves.get(0));
			merge(quarters.get(2), quarters.get(3),	halves.get(1));
		}
	}

	public static void main(String[] args) {
		MergeSort8 ms = new MergeSort8(args);
		ms.run();
	}

}

