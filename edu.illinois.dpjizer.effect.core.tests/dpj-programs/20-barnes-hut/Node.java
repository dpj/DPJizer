/**
 * This file is licensed under the University of Illinois/NCSA Open Source License. See LICENSE.TXT for details.
 */
/**
 * Represents a node in the Barnes-Hut tree
 * @author Robert L. Bocchino Jr.
 * @author Rakesh Komuravelli
 */

public abstract class Node {

    region r;
    /**
     * Total mass of node
     */
    public double mass in r;

    /**
     * Position of node
     */
    public  Vector<r> pos in r = new Vector<r>();

    /**
     * Cost for cost zone analysis
     */
    //public int cost;

    /**
     * Constructor
     */
    public Node() {}

    /**
     * Copy Constructor
     * @param node
     */
    public Node(Node node) {
        this.mass = node.mass;
        this.pos.SETV(node.pos);
    }

    /**
     * Descend tree finding center-of-mass coordinates.
     */
    public abstract double hackcofm();

    /**
     *  Decide if a node should be opened.
     * @param p Node of interest
     * @param dsq
     * @param tolsq
     * @param hg Object holding intermediate computations and other required info
     * @return
     */
    protected abstract<region R> boolean subdivp(Node p, double dsq, double tolsq, HGStruct<R> hg) /* reads r writes R */;
}
