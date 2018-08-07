/**
 * A DijkstraNode represents the Dijkstra algorithm's best
 * estimate of how far a node is.
 * 
 * Stores the node's value <code>index</code> and the
 * <code>DijkstraNode</code>'s distance estimate
 * <code>distance</code>.
 * Is <code>Comparable</code> and sorts first by
 * <code>distance</code> then by <code>index</code>. 
 * 
 * @author Allen Cheng
 * 
 */
public class DijkstraNode implements Comparable<DijkstraNode> {

	private int index;
	private double distance;
	
	/**
	 * Class constructor specifying the node index and initial
	 * distance estimate.
	 * @param index The node index.
	 * @param distance Initial distance estimate.
	 */
	public DijkstraNode(int index, double distance) {
		this.index = index;
		this.distance = distance;
	}
	
	/**
	 * Class constructor that returns a copy of the input.
	 * @param orig The <code>DijkstraNode</code> to copy.
	 */
	public DijkstraNode(DijkstraNode orig){
		this.index = orig.index;
		this.distance = orig.index;
	}
	
	/**
	 * Implements <code>Comparable</code>.
	 * @param other The <code>DijkstraNode</code> to compare to.
	 * @return <code>0</code> if the two <code>DijkstraNode</code>s
	 * are identical, a positive number if <code>other</code> has
	 * a smaller distance or equal distance and same index, and
	 * a negative number otherwise.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DijkstraNode other) {

		if (distance == other.distance)
			return index - other.index;
		return (int) (distance - other.distance);

	}

	/**
	 * Returns string representation.
	 * @return A String representing the node and distance.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Index " + index + ", distance " + distance;
	}
	
	/**
	 * Gets the node number.
	 * @return Value of the <code>index</code> field.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets the node number.
	 * @param index the new value of <code>index</code>.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the distance estimate.
	 * @return Value of the <code>distance</code> field.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Sets the distance estimate.
	 * @param distance the new value of <code>distance</code>.
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

}