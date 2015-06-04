package mitov.alexander;

public class Elements {
	private static String[] labels = new String[500];
	static
	{
		assignLabels();
	}
	public static String getLabelByPeriodicTableIndex(int index)
	{
		if(index < 1 || index > 20) throw new IllegalArgumentException();
		return labels[index-1];
	}
	private static void assignLabels()
	{
		labels[0] = "H";
		labels[1] = "He";
		labels[2] = "Li";
		labels[3] = "Be";
		labels[4] = "B";
		labels[5] = "C";
		labels[6] = "N";
		labels[7] = "O";
		labels[8] = "F";
		labels[9] = "Ne";
		labels[10] = "Na";
		labels[11] = "Mg";
		labels[12] = "Al";
		labels[13] = "Si";
		labels[14] = "P";
		labels[15] = "S";
		labels[16] = "Cl";
		labels[17] = "Ar";
		labels[18] = "K";
		labels[19] = "Ca";
	}
}
