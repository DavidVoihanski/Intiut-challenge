
import java.io.IOException;

public class EntryPoint {

	public static void main(String[] args) {
		try {
			//insert path to output file here
			CarpetParser.initBuffWriter("D:\\Intuit\\out2.txt");
			//insert path to test case file here
			int numOfQ = CarpetParser.startNewTestCase("D:\\Intuit\\task-2.txt");
			for(int qNum = 0; qNum < numOfQ; qNum++) {
				Carpet carpet = CarpetParser.createCarpet();
				//please note that this carpet might not be 
				//balanced after running the weCanAdd(), 
				//the result is legit, I'm creating temp carpets in the function
				long result = carpet.weCanAdd();
				String outString = "Case#" + (qNum+1) + ":" + result;
				System.out.println(outString);
				CarpetParser.saveLineToFile(outString);
			}
			CarpetParser.closeBuffReader();
			CarpetParser.closeBuffWriter();
		}
		catch(IOException ex) {
			ex.printStackTrace();
			ex.getCause();
		}
	}
}
