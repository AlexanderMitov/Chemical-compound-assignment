package mitov.alexander;

public class Atom {
	private int periodicTableIndex;
	private byte charge;
	public Atom(int periodicTableIndex, byte charge)
	{
		if(periodicTableIndex < 1) throw new IllegalArgumentException();
		this.periodicTableIndex = periodicTableIndex;
		this.charge = charge;
	}
	public int getPeriodicTableIndex()
	{
		return periodicTableIndex;
	}
	public byte getCharge()
	{
		return charge;
	}
	public String getLabel()
	{
		return Elements.getLabelByPeriodicTableIndex(periodicTableIndex);
	}
}
