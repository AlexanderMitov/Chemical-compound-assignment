package mitov.alexander;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	private ArrayList<Atom> atoms = new ArrayList<Atom>();
	private ArrayList<Bond> bonds = new ArrayList<Bond>();
	public void parse(String fileName) throws FileNotFoundException
	{
		InputStream initialStream = new FileInputStream(new File(Paths.get("").toAbsolutePath() + "/" + fileName));
		Scanner input = new Scanner(initialStream);
		
		try {
			//reading the input file
			input.nextLine(); //skipping the "atoms:" part
			StringBuffer currentLine = new StringBuffer(input.nextLine()); //reading the first atom
			//reading the rest of the rows
			while(!currentLine.toString().startsWith("bonds"))
			{
				//parsing current line
				for(int i = 0; i < currentLine.length(); ++i)
				{
					if(currentLine.charAt(i) == '_') currentLine.setCharAt(i, ' ');
				}
				InputStream stringInputStream = new ByteArrayInputStream(currentLine.toString().getBytes());
				Scanner stringInput = new Scanner(stringInputStream);
				int atomIndexInPeriodicTable = stringInput.nextInt();
				byte atomCharge = stringInput.nextByte();
				atoms.add(new Atom(atomIndexInPeriodicTable, atomCharge));
				stringInput.close();
				//current line parsed
				currentLine = new StringBuffer(input.nextLine());
			}
			//now reading the bonds
			while(input.hasNextLine())
			{
				currentLine = new StringBuffer(input.nextLine());
				//parse bond line
				for(int i = 0; i < currentLine.length(); ++i)
				{
					if(currentLine.charAt(i) == '_') currentLine.setCharAt(i, ' ');
				}
				InputStream stringInputStream = new ByteArrayInputStream(currentLine.toString().getBytes());
				Scanner stringInput = new Scanner(stringInputStream);
				int firstAtom = stringInput.nextInt() - 1;
				int secondAtom = stringInput.nextInt() - 1;
				char bondType = currentLine.charAt(currentLine.length()-1);
				int intType = 1;
				if(bondType == '=') intType = 2;
				else if(bondType == '#') intType = 3;
				bonds.add(new Bond(atoms.get(firstAtom), atoms.get(secondAtom), intType));
				stringInput.close();
				//parsed bond line
			}
		}
		catch(Exception e) {
			System.out.println("An Exception occured. Please, try again.");
			throw e;
		}
		finally {
			input.close();
		}
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
