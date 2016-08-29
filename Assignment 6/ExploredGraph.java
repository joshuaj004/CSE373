import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

/**
 * 
 */

/**
 * @author Josh Johnson
 * Extra Credit Options Implemented, if any:  (mention them here.)
 * 
 * Solution to Assignment 6 in CSE 373, Winter 2016
 * University of Washington.
 * 
 * Starter code v1.1b. By Steve Tanimoto, with modifications
 * by Kuikui Liu and S.J. Liu.
 *
 * The main differences between this version (1.1a) and version 1.1 are:
 *  1. The use of ArrayList rather than array for the pegs variable in class Vertex.
 *  2. Type information provided for both Function prototypes in class Operator.
 *
 * This code requires Java version 8 or higher.
 *
 */

// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
	}

	/**
	 * Creates new empty LinkedHashSets for the Ve and Ee.
	 */
	public void initialize() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
	}
	
	/**
	 * Returns the size of the vertices set.
	 * @return The size of the vertices set.
	 */
	public int nvertices() {
		return Ve.size();
	}
	
	/**
	 * Returns the size of the edges set.
	 * @return The size of the edges set.
	 */
	public int nedges() {
		return Ee.size();
	}    
	
	/**
	 * Completes a depth-first search. Picks a new vertex from an operation and tries until
	 * it reaches the end vertex or runs out of possibilities.
	 * @param vi The starting vertex.
	 * @param vj The finishing vertex.
	 */
	public void dfs(Vertex vi, Vertex vj) {
		Ve.add(vi);
		if (!vi.equals(vj)) {
			ArrayList<Operator> operatorList = new ArrayList<Operator>();
			operatorList.add(new Operator(0, 1));
			operatorList.add(new Operator(0, 2));
			operatorList.add(new Operator(1, 0));
			operatorList.add(new Operator(1, 2));
			operatorList.add(new Operator(2, 0));
			operatorList.add(new Operator(2, 1));
			for (int i = 0; i < operatorList.size(); i++) {
				if (operatorList.get(i).getPrecondition().apply(vi)) {
					Vertex vk = operatorList.get(i).getTransition().apply(vi);
					if (!Ve.contains(vk)) {
						dfs(vk, vj);
						Ee.add(new Edge(vi, vk));
					}					
				}
			}
		}		
	} 
	
	/**
	 * Completes a breadth-first search. Goes through all the vertices
	 * on the same level before descending to a deeper one. Keeps going
	 * until it reaches the end vertex.
	 * @param vi The starting vertex.
	 * @param vj The finishing vertex.
	 */
	public void bfs(Vertex vi, Vertex vj) {
		Queue<Vertex> tempQueue = new LinkedList<Vertex>();
		tempQueue.add(vi);
		Vertex current = vi;
		while (!tempQueue.isEmpty() && !current.equals(vj)) {
			if (!current.equals(vi)) {
				Ee.add(new Edge(current, tempQueue.peek()));
			}
			current = tempQueue.poll();			
			ArrayList<Operator> operatorList = new ArrayList<Operator>();
			operatorList.add(new Operator(0, 1));
			operatorList.add(new Operator(0, 2));
			operatorList.add(new Operator(1, 0));
			operatorList.add(new Operator(1, 2));
			operatorList.add(new Operator(2, 0));
			operatorList.add(new Operator(2, 1));
			for (int i = 0; i < operatorList.size(); i++) {
				if (operatorList.get(i).getPrecondition().apply(vi)) {
					Vertex vk = operatorList.get(i).getTransition().apply(vi);
					if (!Ve.contains(vk)) {
						tempQueue.add(vk);
					}					
				}
			}
		}
	}
	
	/**
	 * Iterates through the Vertices and returns the path.
	 * @param vi The start Vertex.
	 * @return Returns an ArrayList of vertices from the start until the end
	 * of a perviously conducted search.
	 */
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		Iterator<Vertex> itr = Ve.iterator();
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		while (itr.hasNext()) {
			path.add(itr.next());
		}
		return path;
	}
	
	/**
	 * Finds the shortest path using a breadth first search and returns an ArrayList of the path.  
	 * @param vi The start vertex.
	 * @param vj The end vertex.
	 * @return Returns an ArrayList of the shortest path. 
	 */
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		initialize();
		bfs(vi, vj);
		return retrievePath(vi);
	}
	
	public Set<Vertex> getVertices() {return Ve;} 
	public Set<Edge> getEdges() {return Ee;} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		// Test the vertex constructor: 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		System.out.println(v0);
		
		
		// Add your own tests here.
		// The autograder code will be used to test your basic functionality later.

	}
	
	class Vertex {
		ArrayList<Stack<Integer>> pegs; // Each vertex will hold a Towers-of-Hanoi state.
		// There will be 3 pegs in the standard version, but more if you do extra credit option A5E1.
		int[] topArray;
		ArrayList<ArrayList<String>> towers;
		
		// Constructor that takes a string such as "[[4,3,2,1],[],[]]":
		public Vertex(String vString) {
			this.towers = new ArrayList<ArrayList<String>>();
			this.topArray = new int[3];
			String[] parts = vString.split("\\],\\[");
			pegs = new ArrayList<Stack<Integer>>(3);
			for (int i=0; i<3;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					this.towers.add((ArrayList<String>) al);
					if (al.get(al.size()-1).equals("")) {
						this.topArray[i] = 0;
					} else {
						this.topArray[i] = Integer.parseInt(al.get(al.size()-1));
					}
					System.out.println("ArrayList al is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
	                        if (!item.equals("")) {
                                System.out.println("item is: "+item);
                                pegs.get(i).push(Integer.parseInt(item));
                        }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
		}
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
		
		public ArrayList getTowers() {
			return this.towers;
		}
		
		public int topPeg(int i) {
			return this.topArray[i];
		}
	}
	
	class Edge {
		Vertex vi;
		Vertex vj;
		
		public Edge(Vertex vi, Vertex vj) {
			this.vi = vi;
			this.vj = vj;
		}
		
		public Vertex getVi() {
			return this.vi;
		}
		
		public Vertex getVj() {
			return this.vj;
		}
		
		public String toString() {
			return "Edge from " + this.vi + " to " + this.vj;
		}
	}
	
	class Operator {
		private int i, j;

		public Operator(int i, int j) {
			this.i = i;
			this.j = j;
		}

		Function<Vertex, Boolean> getPrecondition() {
			/**
			 * Checks if a certain vertex can exist.
			 */
			return new Function<Vertex, Boolean>() {
				@Override
				public Boolean apply(Vertex vertex) {
					int firstPeg = vertex.topPeg(i);
					int secondPeg = vertex.topPeg(j);
					if (firstPeg == 0) { return false; }
					else if (secondPeg == 0) { return true; }
					else if (firstPeg < secondPeg) { return true; }
					return false; 
				}
					
			};
		}

		Function<Vertex, Vertex> getTransition() {
			/**
			 * Provided the precondition is true, the removal of one disk
			 * from one peg and the addition of that disk to another peg
			 * is completed and a new vertex is returned.
			 */
			return new Function<Vertex, Vertex>() {
				@Override
				public Vertex apply(Vertex vertex) {
					Function<Vertex, Boolean> f = getPrecondition();
					if (f.apply(vertex)) {
						ArrayList<ArrayList<String>> tempVertex = vertex.getTowers();
						String ring = tempVertex.get(i).get(tempVertex.get(i).size() - 1);
						tempVertex.get(i).remove(tempVertex.get(i).size() - 1);
						if (tempVertex.get(j).get(0).equals("")) {
							tempVertex.get(j).remove(0);
						}
						tempVertex.get(j).add(ring);
						return new Vertex(tempVertex + "");
					}
					return vertex;
				}
			};
		}

		/**
		 * Differentiates this operator from others.
		 */
		public String toString() {
			return "Disk moved from peg " + this.i + " to peg " + this.j;
		}
	}

}
