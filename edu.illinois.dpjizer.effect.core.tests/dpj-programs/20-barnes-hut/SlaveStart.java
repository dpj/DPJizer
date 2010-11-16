/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * The driver class for each thread
 * @author Rakesh Komuravelli
 */

public class SlaveStart implements Runnable {

	/**
	 * Process ID
	 */
	private int processId;

	/**
	 * Tree object
	 */
	private Tree tree;

	/**
	 * Time elapsed
	 */
	private double tnow;

	/**
	 * Constructor
	 * @param processId
	 * @param tree
	 * @param tnow
	 */
	public SlaveStart(int processId, Tree tree, double tnow) {
		this.processId = processId;
		this.tree      = tree;
		this.tnow      = tnow;
	}

	/**
	 * Main thread method
	 */
	public void run() {
		int i = 0;
		while ((tnow < Constants.tstop + 0.1*Constants.dtime) && (i < Constants.NSTEPS)) {
			tree.stepsystem(processId, i); 
			tnow = tnow + Constants.dtime;
			assert(Util.chatting("tnow = %f sp = 0x%x\n", tnow, 0));
			i++;
		}
	}
}
