package mitov.alexander;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.Collection;
import java.util.Set;
import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;

public class Solution {
	public static final String inputFileName = "molecule.txt";
	public static final String outputFileName = "moleculeout.txt";
	public static void main(String[] args) throws FileNotFoundException
	{
		Parser parser = new Parser();
		System.out.println("Reading inputed compound from file: " + inputFileName);
		parser.parse(inputFileName);
		System.out.println("Input parsed");
		ArrayList<Atom> atoms = parser.getAtoms();
		ArrayList<Bond> bonds = parser.getBonds();
		for(int i = 0; i < atoms.size(); ++i)
		{
			indexInInput.put(atoms.get(i), i + 1); //assigning the index in output for each atom
		}
		compound = new Compound(atoms, bonds);
		
		System.out.println("Getting atoms count...");
		int atomsCount = atoms.size();
		System.out.println("Done.");
		
		System.out.println("Getting bonds count...");
		int bondsCount = bonds.size();
		System.out.println("Done.");
		
		System.out.println("Getting atom labels...");
		List<String> atomLabels = new LinkedList<String>();
		for(Atom atom : atoms)
		{
			atomLabels.add(atom.getLabel());
		}
		System.out.println("Done.");
		
		System.out.println("Computing shortest path between the first and last atom...");
		List<Atom> shortestPath = getShortestPath(atoms.get(0), atoms.get(atomsCount-1));
		System.out.println("Done.");
		
		System.out.println("Computing shortest path between the first and last atom...");
		List<Atom> longestPath = getLongestPath(atoms.get(0), atoms.get(atomsCount-1));
		System.out.println("Done.");
		
		System.out.println("Finding elementary cycles in the compound...");
		Collection<LinkedList<Atom>> elementaryCycles = findElementaryCycles();
		System.out.println("Done.");
		
		System.out.println("Sorting atoms in the found elementary cycles...");
		for(LinkedList<Atom> cycle : elementaryCycles)
		{
			Collections.sort(cycle, new CustomComparator<Atom>(indexInInput));
		}
		System.out.println("Done.");
		
		System.out.println("Generating output in file: " + outputFileName + "...");
		OutputGenerator outputGenerator = new OutputGenerator(atomsCount, bondsCount, atomLabels, shortestPath, longestPath, elementaryCycles, indexInInput);
		outputGenerator.generateFile(outputFileName);
		System.out.println("Done.");
		System.out.println("Terminating program.");
	}
	private static Compound compound;
	/**
	 * The graph representation. Basically a mapping of each vertex to a list with adjacent vertexes
	 */
	private static Map<Atom, ArrayList<Edge>> graph = new HashMap<Atom, ArrayList<Edge>>();
	private static Map<Atom, Integer> indexInInput = new HashMap<Atom, Integer>();
	//creates a mapping for each vertex to an adjacency list with the adjacent vertexes
	private static void buildGraph()
	{
		graph.clear();
		for(Atom atom : compound.getAtoms())
		{
			graph.put(atom, new ArrayList<Edge>());
		}
		for(Bond bond : compound.getBonds())
		{
			Atom first = bond.getFirstAtom();
			Atom second = bond.getSecondAtom();
			graph.get(first).add(new Edge(second));
			graph.get(second).add(new Edge(first));
		}
	}
	//implementing a shortest path finding algorithm on an unweighted graph using BFS in O(|V| + |E|) time
	public static List<Atom> getShortestPath(Atom from, Atom to)
	{
		if(from == null || to == null) throw new NullPointerException();
		if(!compound.getAtoms().contains(from) || !compound.getAtoms().contains(to)) throw new IllegalArgumentException();
		buildGraph();
		
		Set<Atom> visited = new HashSet<Atom>();
		Map<Atom, Atom> previous = new HashMap<Atom, Atom>();
		Map<Atom, Integer> distance = new HashMap<Atom, Integer>();
		visited.add(from);
		distance.put(from, new Integer(0));
		Queue<Atom> Q = new LinkedList<Atom>();
		Q.add(from);
		while(!Q.isEmpty())
		{
			Atom current = Q.remove();
			for(Edge edge : graph.get(current))
			{
				Atom child = edge.getTo();
				if(!visited.contains(child))
				{
					distance.put(child, distance.get(current) + 1);
					visited.add(child);
					previous.put(child, current);
					Q.add(child);
				}
			}
		}
		Stack<Atom> shortestPath = new Stack<Atom>();
		Atom current = to;
		while(true)
		{
			shortestPath.push(current);
			if(current == from) break;
			current = previous.get(current);
		}
		List<Atom> result = new LinkedList<Atom>();
		while(!shortestPath.isEmpty())
		{
			result.add(shortestPath.pop());
		}
		return result;
	}
	public static List<Atom> getLongestPath(Atom from, Atom to)
	{
		if(from == null || to == null) throw new NullPointerException();
		if(!compound.getAtoms().contains(from) || !compound.getAtoms().contains(to)) throw new IllegalArgumentException();
		buildGraph();
		
		Stack<Atom> path = new Stack<Atom>();
		Set<Atom> visited = new HashSet<Atom>();
		ArrayList<Atom> longestPath = new ArrayList<Atom>();
		visited.add(from);
		getLongestPath(from, to, path, visited, longestPath); //finding the optimal path
		return longestPath;
	}
	//finding the longest path in a graph is a NP-Hard problem
	//the following code backtracks through all possible simple paths and chooses the longest one
	private static void getLongestPath(Atom current, Atom to, Stack<Atom> path, Set<Atom> visited, ArrayList<Atom> longestPath)
	{
		if(current == null || to == null || path == null || longestPath == null) throw new NullPointerException();
		if(current == to)
		{
			if(path.size() >= longestPath.size())
			{
				longestPath.clear();
				longestPath.addAll(path);
				longestPath.add(current);
			}
			return;
		}
		
		for(Edge edge : graph.get(current))
		{
			Atom child = edge.getTo();
			if(!visited.contains(child))
			{
				visited.add(child);
				path.push(current);
				getLongestPath(child, to, path, visited, longestPath);
				visited.remove(child);
				path.pop();
			}
		}
	}
	public static Collection<LinkedList<Atom>> findElementaryCycles()
	{
		buildGraph();
		Set<Atom> visited = new HashSet<Atom>();
		Collection<LinkedList<Atom>> cycles = new LinkedList<LinkedList<Atom>>();
		Map<Atom, Integer> dfsNum = new HashMap<Atom, Integer>();
		Map<Atom, Atom> previous = new HashMap<Atom, Atom>();
		findElementaryCycles(compound.getAtoms().get(0), visited, cycles, dfsNum, previous, 0);
		return cycles;
	}
	/*
	 * We are finding the set of fundamental cycles. Every other cycle in the graph can be
	 * derived from the fundamental set.
	 * References:
	 * 1) http://en.wikipedia.org/wiki/Spanning_tree#Fundamental_cycles
	 * 2) http://stackoverflow.com/a/2882600/1113314
	 * 3) http://en.wikipedia.org/wiki/Cycle_basis
	 */
	private static void findElementaryCycles(Atom current, Set<Atom> visited, Collection<LinkedList<Atom>> cycles, Map<Atom, Integer> dfsNum, Map<Atom, Atom> previous, int counter)
	{
		if(current == null || visited == null || cycles == null || dfsNum == null || previous == null) throw new NullPointerException();
		
		visited.add(current);
		dfsNum.put(current, counter);
		for(Edge edge : graph.get(current))
		{
			Atom child = edge.getTo();
			if(!visited.contains(child))
			{
				previous.put(child, current);
				findElementaryCycles(child, visited, cycles, dfsNum, previous, counter + 1);
			}
			else if(previous.get(current) != child && dfsNum.get(current) > dfsNum.get(child))
			{
				//cycle found
				LinkedList<Atom> cycle = new LinkedList<Atom>();
				Atom atTheMoment = current;
				Atom end = child;
				while(true)
				{
					cycle.add(atTheMoment);
					if(atTheMoment == end) break;
					atTheMoment = previous.get(atTheMoment);
				}
				cycles.add(cycle);
			}
		}
	}
	public static Collection<LinkedList<Atom>> findStronglyConnectedComponents()
	{
		buildGraph();
		Stack<Atom> stack = new Stack<Atom>();
		Set<Atom> inStack = new HashSet<Atom>();
		Set<Atom> visited = new HashSet<Atom>();
		Map<Atom, Integer> dfsNum = new HashMap<Atom, Integer>();
		Map<Atom, Integer> dfsLow = new HashMap<Atom, Integer>();
		Collection<LinkedList<Atom>> cycleGroups = new LinkedList<LinkedList<Atom>>();
		for(Atom atom : compound.getAtoms())
		{
			if(!visited.contains(atom)) tarjanSCC(atom, stack, inStack, visited, dfsNum, dfsLow, cycleGroups, 0);
		}
		return cycleGroups;
	}
	//implementation of Tarjan's Strongly connected components algorithms in O(|V| + |E|) time
	private static void tarjanSCC(Atom root, Stack<Atom> S, Set<Atom> inStack, Set<Atom> visited, Map<Atom, Integer> dfsNum, Map<Atom, Integer> dfsLow, Collection<LinkedList<Atom>> result, int counter)
	{
		if(root == null || S == null || inStack == null || visited == null || dfsNum == null || dfsLow == null || result == null) throw new NullPointerException();
		visited.add(root);
		inStack.add(root);
		dfsNum.put(root, counter);
		dfsLow.put(root, counter);
		S.push(root);
		
		for(Edge edge : graph.get(root))
		{
			Atom child = edge.getTo();
			if(!visited.contains(child))
			{
				tarjanSCC(child, S, inStack, visited, dfsNum, dfsLow, result, counter + 1);
				int minimum = Math.min(dfsLow.get(root), dfsLow.get(child));
				dfsLow.put(root, minimum);
			}
			else if(inStack.contains(child))
			{
				int minimum = Math.min(dfsLow.get(root), dfsNum.get(child));
				dfsLow.put(root, minimum);
			}
		}
		
		if(dfsLow.get(root).equals(dfsNum.get(root)))
		{
			LinkedList<Atom> currentComponent = new LinkedList<Atom>();
			Atom currentNode = null;
			while(currentNode != root)
			{
				currentNode = S.pop();
				inStack.remove(currentNode);
				currentComponent.add(currentNode);
			}
			result.add(currentComponent);
		}
	}
	private static class Edge
	{
		private Atom to;
		public Edge(Atom to)
		{
			this.to = to;
		}
		public Atom getTo()
		{
			return to;
		}
	}
}
