package mitov.alexander;

public class Bond {
	private enum BondType {
		SINGLE, DOUBLE, TRIPLE
	}
	private BondType bondType;
	private Atom firstAtom;
	private Atom secondAtom;
	public Bond(Atom firstAtom, Atom secondAtom, int bondType)
	{
		if(firstAtom == null || secondAtom == null) throw new NullPointerException();
		this.firstAtom = firstAtom;
		this.secondAtom = secondAtom;
		switch(bondType)
		{
		case 1:
			this.bondType = BondType.SINGLE;
			break;
		case 2:
			this.bondType = BondType.DOUBLE;
			break;
		case 3:
			this.bondType = BondType.TRIPLE;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}
	public Bond.BondType getBondType()
	{
		return bondType;
	}
	public Atom getFirstAtom()
	{
		return firstAtom;
	}
	public Atom getSecondAtom()
	{
		return secondAtom;
	}
}
