import java.util.Arrays;

/**
 * A class to test the <code>DijkstraNode</code> class
 * and its speed on a small sample graph.
 * @author Allen Cheng
 *
 */
public class DijkstraTest {

	public static void main(String[] args) {

		/*
		 * Let's test a Dijkstra on a small graph and
		 * also test for our heap implementation's speed.
		 * Correct Distances:
		 * [ 0.0,  4.0,  3.0,  3.0, 17.0, 10.0, 15.0]
		 * [ 4.0,  0.0,  5.0,  2.0, 13.0,  7.0, 13.0]
		 * [ 3.0,  5.0,  0.0,  3.0, 14.0,  7.0, 12.0]
		 * [ 3.0,  2.0,  3.0,  0.0, 15.0,  8.0, 14.0]
		 * [17.0, 13.0, 14.0, 15.0,  0.0,  7.0,  5.0]
		 * [10.0,  7.0,  7.0,  8.0,  7.0,  0.0,  6.0]
		 * [15.0, 13.0, 12.0, 14.0,  5.0,  6.0,  0.0]
		 */
		
		// Initializes the Dijkstra object
		int N = 7;
		Dijkstra dij = new Dijkstra(N);

		dij.addEdge(0, 1, 4, true);
		dij.addEdge(0, 2, 3, true);
		dij.addEdge(0, 3, 3, true);
		dij.addEdge(1, 3, 2, true);
		dij.addEdge(1, 4, 13, true);

		dij.addEdge(1, 5, 7, true);
		dij.addEdge(2, 3, 3, true);
		dij.addEdge(2, 5, 7, true);
		dij.addEdge(2, 6, 12, true);
		dij.addEdge(3, 5, 8, true);

		dij.addEdge(4, 5, 7, true);
		dij.addEdge(4, 6, 5, true);
		dij.addEdge(5, 6, 6, true);
		
		System.out.println("Pairwise compare each point");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(dij.shortestDist(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("Use heaping for all distances from one vertex at a time:");
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(dij.allDistancesFromOneHeap(i)));
		}
		System.out.println();
		
		// run trials over the graph
		double trials = 1e5;
		System.out.println("Let's time " + trials + " iterations of running everything");
		long start = System.nanoTime();
		for(int t = 0; t < trials; t++){
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					dij.shortestDist(i, j);
				}
			}
		}
		System.out.format("Custom heap, two points at a time: %.4f s\n", (System.nanoTime() - start) * 1e-9);

		start = System.nanoTime();
		for(int i = 0; i < trials; i++){
			for (int j = 0; j < N; j++) {
				dij.allDistancesFromOne(j);
			}
		}
		System.out.format("Java's PriorityQueue, all distances from one point: %.4f s\n", (System.nanoTime() - start) * 1e-9);
		
		start = System.nanoTime();
		for(int i = 0; i < trials; i++){
			for (int j = 0; j < N; j++) {
				dij.allDistancesFromOneHeap(j);
			}
		}
		System.out.format("Custom heap, all distances from one point: %.4f s", (System.nanoTime() - start) * 1e-9);

	}
}