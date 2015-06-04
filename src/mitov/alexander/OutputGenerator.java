package mitov.alexander;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OutputGenerator {
	private int atomsCount;
	private int bondsCount;
	private List<String> atomLabels;
	private List<Atom> shortestPath;
	private List<Atom> longestPath;
	private Collection<LinkedList<Atom>> elementaryCycles;
	private Map<Atom, Integer> atomsOrdering;
	public OutputGenerator(int atomsCount, int bondsCount, List<String> atomLabels, List<Atom> shortestPath, List<Atom> longestPath, Collection<LinkedList<Atom>> elementaryCycles, Map<Atom, Integer> atomsOrdering)
	{
		if(atomLabels == null || shortestPath == null || longestPath == null || elementaryCycles == null || atomsOrdering == null) throw new NullPointerException();
		if(atomsCount < 1 || bondsCount < 0) throw new IllegalArgumentException();
		this.atomsCount = atomsCount;
		this.bondsCount = bondsCount;
		this.atomLabels = atomLabels;
		this.shortestPath = shortestPath;
		this.longestPath = longestPath;
		this.elementaryCycles = elementaryCycles;
		this.atomsOrdering = atomsOrdering;
	}
	public void generateFile(String outputFileName)
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(Paths.get("").toAbsolutePath() + "/" + outputFileName, "UTF-8");
			writer.println("atomcount: " + atomsCount);
			writer.println("bondcount: " + bondsCount);
			writer.print("atomlabel: ");
			Iterator<String> labelsIterator = atomLabels.iterator();
			while(labelsIterator.hasNext())
			{
				String currentLabel = labelsIterator.next();
				writer.print(currentLabel);
				if(labelsIterator.hasNext()) writer.print(", ");
				else writer.println();
			}
			writer.print("spath: ");
			Iterator<Atom> atomsIterator = shortestPath.iterator();
			while(atomsIterator.hasNext())
			{
				Atom currentAtom = atomsIterator.next();
				writer.print(atomsOrdering.get(currentAtom));
				if(atomsIterator.hasNext()) writer.print(", ");
				else writer.println();
			}
			writer.print("lpath: ");
			Iterator<Atom> atomsIterator2 = longestPath.iterator();
			while(atomsIterator2.hasNext())
			{
				Atom currentAtom = atomsIterator2.next();
				writer.print(atomsOrdering.get(currentAtom));
				if(atomsIterator2.hasNext()) writer.print(", ");
				else writer.println();
			}
			writer.println("cycles: " + elementaryCycles.size());
			writer.print("cycleslist: ");
			Iterator<LinkedList<Atom>> cyclesIterator = elementaryCycles.iterator();
			while(cyclesIterator.hasNext())
			{
				LinkedList<Atom> currentCycle = cyclesIterator.next();
				Iterator<Atom> atomsIterator3 = currentCycle.iterator();
				while(atomsIterator3.hasNext())
				{
					Atom currentAtom = atomsIterator3.next();
					writer.print(atomsOrdering.get(currentAtom));
					if(atomsIterator3.hasNext()) writer.print(", ");
				}
				if(cyclesIterator.hasNext()) writer.print("; ");
				else writer.println();
			}
		} catch(Exception e) {
			System.out.println("An error occured while generating output. Please, try again.");
		}
		finally {
			writer.close();
		}
	}
}
