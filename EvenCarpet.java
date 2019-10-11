/**
 * A carpet with an even size.
 * @author David
 *
 */
public class EvenCarpet extends Carpet{
	/**
	 * Creates a new carpet of size*size
	 * @param size	
	 */
	public EvenCarpet(int size) {
		this.size = size;
		this.quarterWeight = new long [4];
		this.quarterSpaces = new long [4];
		this.initEmptySpaces();
	}
	@Override
	public void addBox(int xCoord, int yCoord) {
		int index = this.findQuarter(xCoord -1, yCoord - 1);
		this.quarterSpaces[index]--;
	}
	@Override
	public void addHunter(int xCoord, int yCoord) {
		int index = this.findQuarter(xCoord - 1, yCoord - 1);
		this.quarterSpaces[index]--;
		this.quarterWeight[index]++;
	}
	@Override
	public long weCanAdd() {
		if(!canBeBalaned())
			return -1;
		long result = balanceCarpet();
		result = result + fillCarpet();
		return result;
	}
	
	//private methods***************************************************************
	
	/**
	 * Finds the quarter the point (x,y) is in
	 * 0 is top left, 1 is top right, 2 
	 * is bottom left and 3 is bottom right.
	 * @param xCoord
	 * @param yCoord
	 * @return	Returns the index of the quarter. 
	 */
	private int findQuarter(int xCoord, int yCoord) {
		if(xCoord < this.getSize()/2) {	//x is in left half
			if(yCoord < this.getSize()/2) {//y is in top half
				return 0;
			}
			else return 2;	//y is in bottom half
		}
		else {//x is in right half
			if(yCoord < this.getSize()/2) {	//y is in top half
				return 1;
			}
			return 3;	//y in bottom half
		}
	}
	/**
	 * Initiates the number of empty spaces in each corner of the carpet.
	 */
	private void initEmptySpaces() {
		long size = this.getSize();
		long spaces = size*size/4;
		for (int i = 0; i < this.quarterSpaces.length; i++) {
			this.quarterSpaces[i] = spaces;
		}
	}
	/**
	 * Balances the carpet
	 * @return	returns the number of hunters added
	 */
	private long balanceCarpet() {
		long result;
		if(quarterWeight[0] > quarterWeight[3]) {
			long diff = quarterWeight[0] - quarterWeight[3];
			quarterWeight[3] = quarterWeight[0];
			quarterSpaces[3] = quarterSpaces[3] - diff;
			result = diff;
		}
		else {
			long diff = quarterWeight[3] - quarterWeight[0];
			quarterWeight[0] = quarterWeight[3];
			quarterSpaces[0] = quarterSpaces[0] - diff;
			result = diff;
		}
		if(quarterWeight[1] > quarterWeight[2]) {
			long diff = quarterWeight[1] - quarterWeight[2];
			quarterWeight[2] = quarterWeight[1];
			quarterSpaces[2] = quarterSpaces[2] - diff;
			result = result + diff;
		}
		else {
			long diff = quarterWeight[2] - quarterWeight[1];
			quarterWeight[1] = quarterWeight[2];
			quarterSpaces[1] = quarterSpaces[1] - diff;
			result = result + diff;
		}
		return result;
	}
	/**
	 * If more hunters can be added with the carpet staying balanced, it adds them
	 * @return	returns the amount of hunters added
	 */
	private long fillCarpet() {
		long result;
		result = Math.min(quarterSpaces[0], quarterSpaces[3])*2; //*2 because we add to both sides
		result = result + Math.min(quarterSpaces[1], quarterSpaces[2])*2;
		return result;
	}
}
