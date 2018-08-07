import java.util.PriorityQueue;

/**
 * A class that computes Dijkstra graph shortest distances
 * using both Java's PriorityQueue and a faster, custom
 * heap priority queue implementation.
 * @author Allen Cheng
 *
 */

public class Dijkstra {
	/**
	 * The neighbors of each vertex.
	 */
	private int[][] edges;
	/**
	 * The weights going out from each vertex.
	 */
	private double[][] weights;
	/**
	 * The number of neighbors of each vertex. <code>inds[x]</code>
	 * is the effective length of <code>edges[x]</code> and
	 * <code>weights[x]</code>.
	 */
	private int[] inds;
	/**
	 * The number of vertices in the graph.
	 */
	private int N;
	/**
	 * Remaining number of vertices to process.
	 */
	private int len;

	/**
	 * Class constructor for an empty graph with a set
	 * number of nodes.
	 * @param n The number of nodes in the graph.
	 */
	public Dijkstra(int n) {

		N = n;
		len = N;
		edges = new int[N][N];
		weights = new double[N][N];
		inds = new int[N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				edges[i][j] = -1;
			}
		}

	}
	
	/**
	 * Adds an edge to the graph.
	 * @param a The starting vertex.
	 * @param b The ending vertex.
	 * @param d The weight/length of the edge.
	 * @param undirected Whether the edge is undirected. If
	 * <code>true</code>, then another edge is added going from
	 * <code>b</code> to <code>a</code>.
	 */
	public void addEdge(int a, int b, double d, boolean undirected) {

		edges[a][inds[a]] = b;
		weights[a][inds[a]++] = d;
		if (undirected) {
			edges[b][inds[b]] = a;
			weights[b][inds[b]++] = d;
		}

	}
	
	/**
	 * Uses self-implemented heap-based priority queue to implement
	 * Dijkstra's algorithm.
	 * @param a The starting vertex index.
	 * @param b The ending vertex index.
	 * @return The shortest distance from <code>a</code> to 
	 * <code>b</code> if they are connected,
	 * <code>Double.MAX_VALUE</code> otherwise.
	 */
	public double shortestDist(int a, int b) {

		// Stores the current distance estimates to each vertex
		double[] dists = new double[N];
		for (int i = 0; i < dists.length; i++) {
			dists[i] = Double.MAX_VALUE;
		}
		len = N;
		
		// Heap-based priority queue
		DijkstraNode[] pq = new DijkstraNode[N + 1];
		
		// Adds each vertex to pq and remembers where in pq we put it
		int[] locs = new int[N];
		locs[a] = 1;
		pq[1] = new DijkstraNode(a, 0);
		for (int i = 0; i < edges.length; i++) {
			if (i != a) {
				int ind = i + 1;
				if (i < a)
					ind++;
				pq[ind] = new DijkstraNode(i, Double.MAX_VALUE);
				locs[i] = ind;
			}
		}
		
		// Iterates through the nearest vertex, of which we have at most N
		for (int v = 0; v < N; v++) {
			DijkstraNode node = dqMin(pq, locs);
			if (node.getIndex() == b)
				return node.getDistance();
			dists[node.getIndex()] = Math.min(dists[node.getIndex()], node.getDistance());

			for (int i = 0; i < N && edges[node.getIndex()][i] >= 0; i++) {
				int neighbor = edges[node.getIndex()][i];
				// Stops if we've already visited
				if (locs[neighbor] < 1)
					continue;
				// If need be, update and reheap
				if (dists[node.getIndex()] + weights[node.getIndex()][i] < pq[locs[neighbor]].getDistance()) {
					downHeap(pq, neighbor, dists[node.getIndex()] + weights[node.getIndex()][i], locs);
				}
			}
		}
		
		return dists[b];
	}
	
	/**
	 * Efficiently computes all distances between vertices
	 * using Dijkstra's algorithm.
	 * @return A matrix <code>m</code> of all distances between
	 * two points. <code>m[a][b]</code> is the distance from 
	 * <code>a</code> to <code>b</code>.
	 */
	public double[][] allDistances() {

		double[][] ans = new double[N][N];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = allDistancesFromOneHeap(i);
			for (int j = 0; j < i; j++) {
				ans[i][j] = ans[j][i];
			}
		}
		return ans;
	}
	
	/**
	 * Uses Dijkstra's algorithm and the Java-default priority
	 * queue to compute all distances from one vertex.
	 * @param a The source vertex.
	 * @return An array of shortest distances from
	 * <code>a</code> to all other vertices.
	 */
	public double[] allDistancesFromOne(int a) {

		// Java-default priority queue
		PriorityQueue<DijkstraNode> pq = new PriorityQueue<DijkstraNode>();
		// Stores the current distance estimates to each vertex
		double[] dists = new double[edges.length];
		for (int i = 0; i < dists.length; i++) {
			dists[i] = Double.MAX_VALUE;
		}
		
		// Adds to priority queue
		for (int i = 0; i < edges.length; i++) {
			DijkstraNode node = new DijkstraNode(i, Double.MAX_VALUE);
			if (i == a)
				node.setDistance(0);
			pq.add(node);
		}
		
		int count = 0;
		
		// Iterates through the priority queue
		while (pq.size() > 0  && count <= edges.length - a) {
			DijkstraNode node = pq.remove();
			// Discards vertices we've already processed
			if (dists[node.getIndex()] < node.getDistance())
				continue;
			if(node.getIndex() >= a){
				count++;
			}
			// Stops if we've reached all connected vertices
			if (node.getDistance() == Double.MAX_VALUE)
				break;
			dists[node.getIndex()] = node.getDistance();
			
			// Updates distances in the priority queue
			for (int i = 0; i < N && edges[node.getIndex()][i] >= 0; i++) {
				int neighbor = edges[node.getIndex()][i];
				dists[neighbor] = dists[node.getIndex()] + weights[node.getIndex()][i];
				pq.add(new DijkstraNode(neighbor, dists[neighbor]));
			}
		}
		
		return dists;

	}

	/**
	 * Uses Dijkstra's algorithm and a custom heap-based priority
	 * queue to compute all distances from one vertex.
	 * @param a The source vertex.
	 * @return An array of shortest distances from
	 * <code>a</code> to all other vertices.
	 */
	public double[] allDistancesFromOneHeap(int a) {

		// Stores the current distance estimates to each vertex
		double[] dists = new double[N];
		for (int i = 0; i < dists.length; i++) {
			dists[i] = Double.MAX_VALUE;
		}
		len = N;
		
		// Self-built heap-based priority queue
		DijkstraNode[] pq = new DijkstraNode[N + 1];
		int[] locs = new int[N];
		locs[a] = 1;
		pq[1] = new DijkstraNode(a, 0);
 
		// Adds each vertex to pq and remembers where in pq we put it
		for (int i = 0; i < edges.length; i++) {
			if (i != a) {
				int ind = i + 1;
				if (i < a)
					ind++;
				pq[ind] = new DijkstraNode(i, Double.MAX_VALUE);
				locs[i] = ind;
			}
		}

		// Iterates through the nearest vertex, of which we have at most N
		for (int v = 0; v < N; v++) {
			DijkstraNode node = dqMin(pq, locs);
			dists[node.getIndex()] = Math.min(dists[node.getIndex()], node.getDistance());

			for (int i = 0; i < N && edges[node.getIndex()][i] >= 0; i++) {
				int neighbor = edges[node.getIndex()][i];
				// Stops if we've already visited
				if (locs[neighbor] < 1)
					continue;
				// If need be, update and reheap
				if (dists[node.getIndex()] + weights[node.getIndex()][i]
						< pq[locs[neighbor]].getDistance()) {
					downHeap(pq, neighbor, dists[node.getIndex()] + weights[node.getIndex()][i], locs);
				}
			}
		}

		return dists;

	}
	
	/**
	 * Changes heap index i to have distance d then downheaps input heap,
	 * tracking result in given array.
	 * @param pq Heap to be downheaped.
	 * @param i Vertex index to be changed.
	 * @param d New distance for index <code>i</code>.
	 * @param inds Array tracking vertex locations in heap.
	 */
	private void downHeap(DijkstraNode[] pq, int i, double d, int[] inds) {

		int ind = inds[i];
		pq[inds[i]].setDistance(d);
		
		while (ind / 2 > 0 && d < pq[ind / 2].getDistance()) {
			int b = ind / 2;
			
			int i_a = pq[ind].getIndex();
			double d_a = pq[ind].getDistance();
			
			pq[ind].setIndex(pq[b].getIndex());
			pq[ind].setDistance(pq[b].getDistance());
			pq[b].setIndex(i_a);
			pq[b].setDistance(d_a);
			inds[pq[b].getIndex()] = b;
			inds[pq[ind].getIndex()] = ind;

			ind = b;
		}

	}
	
	
	/**
	 * Upheaps input heap starting at position <code>i</code>.
	 * @param pq Heap to be upheaped.
	 * @param i Upheap starting position.
	 * @param inds Array tracking vertex locations in heap.
	 */
	private void upHeap(DijkstraNode[] pq, int i, int[] inds) {

		int ind = i;

		while (ind * 2 <= len) {
			int b = ind * 2;
			if (b + 1 <= len && pq[b + 1].getDistance() < pq[b].getDistance())
				b++;
			if (pq[b].getDistance() > pq[ind].getDistance())
				break;
			int i_a = pq[ind].getIndex();
			double d_a = pq[ind].getDistance();
			pq[ind].setIndex(pq[b].getIndex());
			pq[ind].setDistance(pq[b].getDistance());
			pq[b].setIndex(i_a);
			pq[b].setDistance(d_a);

			inds[pq[b].getIndex()] = b;
			inds[pq[ind].getIndex()] = ind;
			
			ind = b;
		}

	}

	/**
	 * Pop the first element of the priority queue, tracking vertex locations.
	 * @param pq Priority queue to be popped.
	 * @param inds V
	 * @return Array tracking vertex locations in heap.
	 */
	private DijkstraNode dqMin(DijkstraNode[] pq, int[] inds) {

		DijkstraNode min = pq[1];
		pq[1] = pq[len--];
		upHeap(pq, 1, inds);
		inds[min.getIndex()] = -1;
		return min;

	}

}