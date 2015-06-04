package mitov.alexander;
import java.util.ArrayList;

public class Compound {
	private ArrayList<Atom> atoms = new ArrayList<Atom>();
	private ArrayList<Bond> bonds = new ArrayList<Bond>();
	public Compound(ArrayList<Atom> atoms, ArrayList<Bond> bonds)
	{
		if(atoms == null || bonds == null) throw new NullPointerException();
		this.atoms = atoms;
		this.bonds = bonds;
	}
	public int getAtomsCount()
	{
		return atoms.size();
	}
	public int getBondsCount()
	{
		return bonds.size();
	}
	public ArrayList<Atom> getAtoms()
	{
		return atoms;
	}
	public ArrayList<Bond> getBonds()
	{
		return bonds;
	}
}
