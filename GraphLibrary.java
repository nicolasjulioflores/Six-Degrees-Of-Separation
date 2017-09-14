package cs10;
import java.util.*;

/**
 * Cathy Li
 * CS 10
 * Lab 4
 * 
 * Library for graph analysis
 * 
 */
public class GraphLibrary {

	/*
	 *  BFS 
	 *  builds shortest path tree for a current center of the universe
	 *  from every vertex that can reach the center, back to the center
	 *  
	 *  the tree is a directed graph
	 *  every vertex points to its parent
	 *  parent is next vertex in shortest path to root
	 *  
	 *  returns BFS tree
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
		Queue<V> q = new LinkedList<V>();
		Graph<V,E> visited = new AdjacencyMapGraph<V,E>(); //keeps track of visited vertices
		
		q.add(source);
		visited.insertVertex(source);
		while (!q.isEmpty()) {
			V current = q.remove();
				for(V v : g.outNeighbors(current)) {
					if (!visited.hasVertex(v)) {
						q.add(v);
						visited.insertVertex(v);
						visited.insertDirected(v, current, g.getLabel(v, current));
					}
				}
		}
		return visited;
		
	}
	
	/*
	 *  Given a shortest path tree and a vertex, 
	 *  construct a path form the vertex back to the center of the universe
	 *  return the path
	 */
	public static <V,E> ArrayList<V> getPath(Graph<V,E> tree, V v) {
		ArrayList<V> path = new ArrayList<V>();
		path.add(v);
		path = getPathHelper(tree, v, path);
		return path;
	}
	
	/* Helper method for getPath
	 * recursively gets path from vertex to root
	 * return the path
	 */
	public static <V,E> ArrayList<V> getPathHelper(Graph<V,E> tree, V v, ArrayList<V> path) {
		V current = v;
		
		if (!tree.hasVertex(current)) {
			System.out.println("vertex unreachable by BFS tree");
		}

		else {
			for (V prev : tree.outNeighbors(current)) {
				path.add(prev);
				getPathHelper(tree,prev,path);

				}
			}
		
		return path;
	}
	
	
	/*
	 * Given a graph and a subgraph (shortest path tree),
	 * determine which vertices are in the graph but not the subgraph
	 * (not reached by BFS)
	 * 
	 * return set of vertices
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
		Set<V> missing = new TreeSet<V>();
		
		for (V v : graph.vertices()) {
			if (!subgraph.hasVertex(v)) {
				missing.add(v);
			}
		}
		
		return missing;
	}
	
	/*
	 * Find the average distance-from-root in a shortest path tree
	 * do without enumerating all paths (use tree recursion)
	 * 
	 * returns average distance
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		double distance = 0; //total distance

		for (V v:tree.vertices()) { // loop over all vertices in the tree
			distance += (GraphLibrary.getPath(tree, v).size()-1); //add the distance of the shortest path to total distance
		}
		return distance/(tree.numVertices()-1); //average distance is total distance divided by number of vertices
		
	}
	
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g, int low, int high) {
		List<V> vertices = new ArrayList<V>();;

		for (V vertex : g.vertices()) {
			if (g.inDegree(vertex)>=low && g.inDegree(vertex)<=high) {
					vertices.add(vertex);
			}
		}

		vertices.sort((V v1, V v2) -> g.inDegree(v2)-g.inDegree(v1));
		
		return vertices;
	}
	

}
