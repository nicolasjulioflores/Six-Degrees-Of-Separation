package cs10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/*
 * In the Kevin Bacon game, the vertices are actors 
 * and the edge relationship is "appeared together in a movie"
 * 
 * Uses command-line interface
 */
public class KevinBaconGame {
	
	private static final String actorsFile = "inputs/actors.txt";
	private static final String moviesFile = "inputs/movies.txt";
	private static final String movieActorsFile = "inputs/movie-actors.txt";
	
	/*
	 * called when user inputs "u <name>"
	 * 
	 * Change the center of the universe to a valid actor
	 * and find the average path length over all actors who are connected to the center actor
	 */
	private static String u(String[] terms, Graph<String, Set<String>> graph) throws Exception{
		String name = terms[1]; // get name from user input
		try {
			for (int i=2;i<terms.length;i++) { // names can be one or more words
				name += " " + terms[i];
			}
			Graph<String,Set<String>> BFSTree = GraphLibrary.bfs(graph, name);
			//print out response
			System.out.println(name+" is now the center of the universe, connected to "+BFSTree.numVertices()+"/"
			+graph.numVertices()+" actors with average separation "+GraphLibrary.averageSeparation(BFSTree, name)+"\n");
			
		}
		catch (Exception e) {
			System.err.println("Not a valid actor name. Please select a different center of the universe.");
		}
		return name;
	}
	
	/*
	 * called when user inputs "p <name>"
	 * 
	 * finds the shortest path to an actor from the current center of the universe
	 */
	private static void p(String[] terms, Graph<String, Set<String>> graph, String center) throws Exception{
		Graph<String,Set<String>> BFSTree = GraphLibrary.bfs(graph, center);
		try {
			String name = terms[1]; // get name from user input; name can be more than one word
			for (int i=2;i<terms.length;i++) {
				name += " " + terms[i];
			}
			if (!graph.hasVertex(name)) {
				System.err.println("Not a valid actor name. Please try aagin");
			}
				
			else {
				ArrayList<String> path = GraphLibrary.getPath(BFSTree, name); // shortest path from actor to center
				System.out.println(name+"'s number is "+(path.size()-1));
				
				// get and print out path from actor to center
				String current = name;
				for (int j=1;j<path.size();j++) { 
					String next = path.get(j);
					System.out.println(current+" appeared in "+BFSTree.getLabel(current, next)+" with "+next);
					current=next;
				}				
			}
		}
		catch (Exception e) {
			System.err.println("Not a valid actor name. Please try again");
		}
	}

	/*
	 * called when user inputs "d <# low> <# high>
	 * 
	 * list actors sorted by degree, with degree between low and high
	 */
	public static void d(String[] terms, Graph<String, Set<String>> graph) throws Exception{

		try {
			int low = Integer.parseInt(terms[1]), high = Integer.parseInt(terms[2]);
			if (low > high) {
				System.err.println("Enter the lower bound first, followed by the upper bound");
			}
			List<String> actors = GraphLibrary.verticesByInDegree(graph, low, high);

			System.out.println("Actors sorted from highest to lowest in-degree, between "+low+" and "+high+": "+actors);
		}
		catch (Exception e) {
			System.err.println("Please enter 2 numbers, separated by a space. Enter the lower bound first, followed by the upper bound");
		}
	}
	
	/*
	 * called when user inputs "i"
	 * 
	 * list actors with infinite separation from the current center
	 * no path between them and the center of the universe
	 */
	public static void i(String[] terms, Graph<String, Set<String>> graph, String center) throws Exception{
		Graph<String,Set<String>> BFSTree = GraphLibrary.bfs(graph, center);
		try {
			Set<String> missingActors = GraphLibrary.missingVertices(graph, BFSTree);
			System.out.println("Actors with infinite separation from "+center+": "+missingActors);
		}
		catch (Exception e) {
			System.err.println("Error");
		}
		
	}
	
	/*
	 * called when user inputs "s <# low> <# high>
	 * 
	 * find actors who have a path (connected by some number of steps) to the current center
	 */
	public static void s(String[] terms, Graph<String, Set<String>> graph, String center) throws Exception{
		Graph<String,Set<String>> BFSTree = GraphLibrary.bfs(graph, center);
		List<String> actors = new ArrayList<String>(); //list of actors who are connected to the center
		try {
			int low = Integer.parseInt(terms[1]), high = Integer.parseInt(terms[2]);
			if (low > high) {
				System.err.println("Enter the lower bound first, followed by the upper bound");
			}
			for (String actor:BFSTree.vertices()) {
				int pathLength = GraphLibrary.getPath(BFSTree, actor).size()-1; //get length of path from actor to center
				if (pathLength >= low && pathLength <= high) { // if path length is within the user-inputed bounds
					actors.add(actor); // add actor to the list of actors
				}
			}
			// sort list of actors by path length, from longest to shortest
			actors.sort((String actor1, String actor2)->GraphLibrary.getPath(BFSTree, actor1).size()-GraphLibrary.getPath(BFSTree, actor2).size());
			
			// print out names and number of actors separated from the center by the user-inputed bounds
			System.out.println("Names of actors separated from "+center+" by "+low+" to "+high+" steps: "+actors);
			System.out.println("Number of actors separated from "+center+" by "+low+" to "+high+" steps: "+actors.size());
		}
		catch (Exception e) {
			System.err.println("Please enter 2 numbers, separated by a space. Enter the lower bound first, followed by the upper bound");
		}
	}
	
	/*
	 * Called when user inputs "c <#>"
	 * 
	 * list top (positive number) or bottom (negative) <#> centers of the universe, 
	 * sorted by average separation
	 */

	public static void c(String[] terms, Graph<String, Set<String>> graph) {
		Graph<String,Set<String>> BFSTree = GraphLibrary.bfs(graph, "Kevin Bacon"); // only consider actors who are connected to Kevin Bacon
		Map<String, Double> centersMap = new TreeMap<String, Double>(); // Map actors to their average separations
		try {
			int num = Integer.parseInt(terms[1]);
			
			//loop over all actors 
			//consider each one as the center of the universe, and find average path length
			for (String actor:BFSTree.vertices()) {
				Double averagePath = GraphLibrary.averageSeparation(GraphLibrary.bfs(graph, actor), actor);
				centersMap.put(actor, averagePath); // add to map
			}
				
	
			//list of actors, sorted by average separation
			ArrayList<String> centers = new ArrayList<String>();
			for (String key:centersMap.keySet()) {
				Double pathLength = centersMap.get(key);
				int p = centers.size();
				while (p>0 && centersMap.get(centers.get(p-1)) >= pathLength) {
					p--;
				}
				centers.add(p,key);
			}

			List<String> centerslist = new ArrayList<String>();
			int i=Math.abs(num);

			while (i>0) {
				System.out.println(i);
				if (num>0) {
					centerslist.add(centers.remove(centers.size()-1));
				}
				else {
					centerslist.add(centers.remove(0));
				}
				i--;
			}
			System.out.println("The "+num+" highest centers of the univserse, sorted by average separation (low to high), are: "+centerslist);
			
		}
		catch (Exception e) {
			System.err.println("Please enter a valid number");
		}
	}

	
	/*
	 * calls methods based on user input
	 */
	public static void main(String[] args) throws Exception {
		
		Graph<String, Set<String>> graph = ProcessFile.makeGraphs(actorsFile, moviesFile, movieActorsFile);
		//System.out.println(graph);
		String center="";

		//use Scanner to get a line of input from the console
		Scanner in = new Scanner(System.in);
		while (true) {
			if (graph.hasVertex(center)) {
				System.out.println("\n"+center+" game >");
			}
			else {
				System.out.println("\n"+">");
			}
			String line = in.nextLine();
			String[] terms = line.split(" ");
			
			//list top (positive number) or bottom (negative) <#> centers of the universe, 
			//sorted by average separation
			if (terms[0].equals("c")) {
				c(terms, graph);
			}
			
			//list actors sorted by degree, with degree between low and high
			else if (terms[0].equals("d")) {
				d(terms, graph);
			}
		
			//list actors with infinite separation from the current center
			//no path between them and the center of the universe
			else if (terms[0].equals("i")) {
				if (center=="") {
					System.err.println("Please select a center of the universe first.");
				}
				else if (!graph.hasVertex(center)) {
					System.err.println("A valid center of the universe has not been chosen yet.");
				}
				else { 
					i(terms, graph, center);
				}
			}
		
			//find path from <name> to current center of the universe
			else if (terms[0].equals("p")) {
				if (center=="") {
					System.err.println("Please select a center of the universe first.");
				}
				else if (!graph.hasVertex(center)) {
					System.err.println("A valid center of the universe has not been chosen yet.");
				}
				else { 
					p(terms, graph, center);
				}
			}
		
			//list actors sorted by non-infinite separation from the current center,
			//with separation between low and high
			else if (terms[0].equals("s")) {
				if (!graph.hasVertex(center)) {
					System.err.println("A valid center of the universe has not been chosen yet.");
				}
				else {
					s(terms, graph, center);
				}
			}
		
			//make <name> the center of the universe
			else if (terms[0].equals("u")) {
				center = u(terms, graph);
			}
		
			//quit game
			else if (terms[0].equals("q")) {
				System.exit(0);
				in.close();
			}
			
			else {
				System.err.println("Not a valid command");
			}
		
		}		
	}

}
