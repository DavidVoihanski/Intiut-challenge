import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CarpetParser {
	private static File file;
	private static BufferedReader br;
	private static BufferedWriter bw;

	private CarpetParser() {} //never used

	/**
	 * Defines our File, BufferedReader and parses the number of test in the given test case file
	 * @param pathToFile
	 * @return	Returns the number of tests in the given file
	 * @throws IOException
	 */
	public static int startNewTestCase(String pathToFile) throws IOException {
		file = new File(pathToFile);
		br = new BufferedReader(new FileReader(file));
		int numOfQ = Integer.parseInt(br.readLine());
		return numOfQ;
	}
	/**
	 * Creates the next carpet in line
	 * @return	Returns the carpet created
	 * @throws IOException
	 */
	public static Carpet createCarpet() throws IOException {
		String init = br.readLine();
		String []n_b_h = init.split(" ");
		int size = Integer.parseInt(n_b_h[0]);
		int numOfBoxes = Integer.parseInt(n_b_h[1]);//#of boxes
		int numOfHunters = Integer.parseInt(n_b_h[2]);//#of hunters
		Carpet carpet;
		if(size%2 == 0)
			carpet = new EvenCarpet(size);
		else 
			carpet = new OddCarpet(size);
		addBoxes(numOfBoxes, carpet);
		addHunters(numOfHunters, carpet);
		return carpet;
	}
	/**
	 * Creates a new bufferedWriter to the file given.
	 * @param pathToFile
	 * @throws IOException
	 */
	public static void initBuffWriter(String pathToFile) throws IOException {
		bw = new BufferedWriter(new FileWriter(pathToFile));
	}
	/**
	 * Saves a line of text to the file.
	 * @param lineToSave
	 * @throws IOException
	 */
	public static void saveLineToFile(String lineToSave) throws IOException {
		bw.write(lineToSave);
		bw.newLine();
	}
	/**
	 * Closes the bufferedWriter
	 * @throws IOException
	 */
	public static void closeBuffWriter() throws IOException {
		bw.close();
	}
	/**
	 * Closes the BufferedReader
	 * @throws IOException
	 */
	public static void closeBuffReader() throws IOException {
		br.close();
	}

	//private methods************************************************************
	
	/**
	 * Parses and adds the boxes input carpet
	 * @param numOfBoxes Number of boxes to add
	 * @param carpet Carpet to add boxes to
	 * @throws IOException
	 */
	private static void addBoxes(int numOfBoxes, Carpet carpet) throws IOException {
		String coordsString, coordsStringArr[];
		int xCoord, yCoord;
		for(int i = 0; i < numOfBoxes; i++) {
			coordsString = br.readLine();
			coordsStringArr = coordsString.split(" ");
			xCoord = Integer.parseInt(coordsStringArr[0]);
			yCoord = Integer.parseInt(coordsStringArr[1]);
			carpet.addBox(xCoord, yCoord);
		}
	}
	/**
	 * Parses and adds the hunters to input carpet
	 * @param numOfHunters Number of hunters to add
	 * @param carpet Carpet to add the hunters to
	 * @throws IOException
	 */
	private static void addHunters(int numOfHunters, Carpet carpet) throws IOException {
		String coordsString, coordsStringArr[];
		int xCoord, yCoord;
		for(int i = 0; i < numOfHunters; i++) {
			coordsString = br.readLine();
			coordsStringArr = coordsString.split(" ");
			xCoord = Integer.parseInt(coordsStringArr[0]);
			yCoord = Integer.parseInt(coordsStringArr[1]);
			carpet.addHunter(xCoord, yCoord);
		}
	}
}
