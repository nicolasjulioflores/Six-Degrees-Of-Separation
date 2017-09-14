package cs10;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProcessFile {
	private static Map<String, String> actors;
	private static Map<String, String> movies;
	private static Map<String, List<String>> IDs;
	private static Graph<String, Set<String>> actorsGraph;
	
	public static Graph<String,Set<String>> makeGraphs(String actorsFile, String moviesFile, String movieActorsFile) {
		// create maps mapping IDs to actor names and IDs to movie names
		actors = readFile(actorsFile); // key is actorID, value is actor name
		movies = readFile(moviesFile); // key is movieID, value is movie name
		IDs = readFile2(movieActorsFile); // key is movieID, value is list of actorIDs
		
		actorsGraph = new AdjacencyMapGraph<String, Set<String>>();
		
		// build graph whose vertices are actor names 
		// edges labels are sets of movie names in which actors appear together
		
		// for each actor ID, get actor name corresponding to the ID, and add the actor to the graph
		for (String actorID : actors.keySet()) {
			String actorName = actors.get(actorID);
			actorsGraph.insertVertex(actorName);
		}
		
		// loop over keys in IDs graph to figure out which actors appeared in each movie
		for (String movieID : IDs.keySet()) {
		
			// get movie name corresponding to the movieID
			String movieName = movies.get(movieID);
			
			// get IDs of actor who appeared in the movie
			List<String> actorIDs = IDs.get(movieID);
			
			for (int i=0; i<actorIDs.size(); i++) {
				for (int j=actorIDs.size()-1; j>=0; j--) {
					if (j != i) {
						String actor1 = actors.get(actorIDs.get(i)), actor2 = actors.get(actorIDs.get(j));
				
						// an edge is created when two actors costar
						// the edge label is the set of movies that two actors appeared together in
						if (actorsGraph.hasEdge(actor1, actor2)){ 
							actorsGraph.getLabel(actor1, actor2).add(movieName); //add movie to set of co-star movies
						}
						else { // create new edge between actors 
							Set<String> costarMovies = new TreeSet<String>();
							costarMovies.add(movieName);
							actorsGraph.insertUndirected(actor1, actor2, costarMovies);
						}
					}
				}
			}
		}
		return actorsGraph;
	}
	
	/**
	 * Loads information from a pipe-separated values format file
	 * returns Map<String,String>
	 * 
	 * used to process actors.txt and movies.txt
	 */
	public static Map<String,String> readFile(String fileName) {

		BufferedReader input;
		TreeMap<String, String> map = new TreeMap<String, String>();
		
		// Open the file, if possible
        try {
    		input = new BufferedReader(new FileReader(fileName));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return map;
        }
        
    // Read the file
		try {
			// Line by line
			String line;
			int lineNum = 0;
			while ((line = input.readLine()) != null) {
				// Pipe-separated line
				String[] pieces = line.split("\\|");
				if (pieces.length != 2) {
					System.err.println("bad separation in line "+lineNum+":"+line);
				}
				else {
					// Add the value to the map, with ID as key
					try {
						map.put(pieces[0], pieces[1]);
					}
					catch (NumberFormatException e) {
						System.err.println("bad number in line "+lineNum+":"+line);
					}
				}
				lineNum++;
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}

		// Close the file, if possible
		try {
			input.close();
		}
		catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}

		return map;
	}
	
	
	/**
	 * Loads information from a pipe-separated values format file
	 * Returns Map<Stirng,List<String>>
	 * 
	 * used to process movie-actors.txt
	 */
	public static Map<String,List<String>> readFile2(String fileName) {

		BufferedReader input;
		
		//declare new map, each entry in the map is a list that will hold the actorIDs of actors who appear in the movie
		TreeMap<String, List<String>> map = new TreeMap<String, List<String>>();
		
		// Open the file, if possible
        try {
    		input = new BufferedReader(new FileReader(fileName));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return map;
        }
        
    // Read the file
		try {
			// Line by line
			String line;
			int lineNum = 0;
			while ((line = input.readLine()) != null) {
				// Pipe-separated line
				String[] pieces = line.split("\\|");
				if (pieces.length != 2) {
					System.err.println("bad separation in line "+lineNum+":"+line);
				}
				else {
					// Add the value to the map, with ID as key
					try {
						if (map.containsKey(pieces[0])){
							map.get(pieces[0]).add(pieces[1]);
						}
						else {
							List<String> actorIDs = new ArrayList<String>();
							actorIDs.add(pieces[1]);
							map.put(pieces[0], actorIDs);
						}
						
					}
					catch (NumberFormatException e) {
						System.err.println("bad number in line "+lineNum+":"+line);
					}
				}
				lineNum++;
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}

		// Close the file, if possible
		try {
			input.close();
		}
		catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}

		return map;
	}
	
	
	public static void main(String[] args) throws Exception {
		//List<Student> roster = readRoster("inputs/roster.csv");
		//PriorityQueue<Student> pq = new PriorityQueue<Student>();
		//pq.addAll(roster);
		//System.out.println("\nsorted roster:");
		//while (!pq.isEmpty()) System.out.println(pq.remove());
		makeGraphs("inputs/actorsTest.txt","inputs/moviesTest.txt","inputs/movie-actorsTest.txt");
		System.out.println(actors);
		System.out.println(movies);
		System.out.println(IDs);
		System.out.println(actorsGraph);
	}

}
