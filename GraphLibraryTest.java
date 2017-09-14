package cs10;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GraphLibraryTest {
	
	/*
	 * test functions of Graph Library on simple graph
	 * hard-code addition of vertices and edges
	 * 
	 */
	public static void main(String [] args) {
		Graph<String, String> test = new AdjacencyMapGraph<String, String>();

		test.insertVertex("Kevin Bacon");
		test.insertVertex("Alice");
		test.insertVertex("Bob");
		test.insertVertex("Charlie");
		test.insertVertex("Dartmouth");
		test.insertVertex("Nobody");
		test.insertVertex("Nobody's Friend");

		test.insertUndirected("Kevin Bacon", "Bob", "A Movie");
		test.insertUndirected("Kevin Bacon", "Alice", "A Movie, E Movie");
		test.insertUndirected("Bob", "Alice", "A Movie");
		test.insertUndirected("Bob", "Charlie", "C Movie");
		test.insertUndirected("Alice", "Charlie", "D Movie");
		test.insertUndirected("Charlie", "Dartmouth", "B Movie");
		test.insertUndirected("Nobody", "Nobody's Friend", "F Movie");
	
		System.out.println("The graph:");
		System.out.println(test);

		
		
		for (String v : test.vertices()) {
			System.out.println(v);
			Graph<String,String> BFSTree = GraphLibrary.bfs(test, v);
			System.out.println(BFSTree);
			Double avgPathLength = GraphLibrary.averageSeparation(BFSTree, v);
			System.out.println(avgPathLength);
		}
		/*for (String v:test.vertices()) {
			System.out.println("\n"+"BFS with "+v+" as center of universe:");
			Graph<String,String> tree = GraphLibrary.bfs(test, v);
			List<String> path = GraphLibrary.getPath(tree, "Charlie");
			System.out.println(path);
			String current = "Charlie";
			for (int j=1;j<path.size();j++) { //String next:path) {
				String next = path.get(j);
				System.out.println(current+" appeared in "+tree.getLabel(current, next)+" with "+next);
				current=next;
			}
		}
		*/

		
		System.out.println("\nVertices from highest to lowest in-degree: " + GraphLibrary.verticesByInDegree(test,1,4));

	}
}
		