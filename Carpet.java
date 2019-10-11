/**
 * For the carpet to be balanced, 0,3 and 1,2 should have the same value.
 * The value in each index is the number of hunters in that quarter.
 * @author David
 * 
 * */
public abstract class Carpet {
	protected int size;
	//0 is top left, 1 is top right, 2 is bottom left and 3 is bottom right.
	protected long quarterWeight[];
	protected long quarterSpaces[];

	protected int getSize() {
		return this.size;
	}
	/**
	 * Checks if the carpet can be balanced
	 * @return	returns true if yes, and false otherwise
	 */
	protected boolean canBeBalaned() {
		//if the the amount of hunters needed to be added to balance is bigger than the empty spaces, it can't
		//be balanced
		if(quarterWeight[0] > quarterWeight[3] && (quarterWeight[0] - quarterWeight[3]) > quarterSpaces[3]) 
			return false;
		if(quarterWeight[0] < quarterWeight[3] && quarterSpaces[0] < (quarterWeight[3] - quarterWeight[0])) 
			return false;
		if(quarterWeight[1] > quarterWeight[2] && (quarterWeight[1] - quarterWeight[2]) > quarterSpaces[2])
			return false;
		if(quarterWeight[1] < quarterWeight[2] && quarterSpaces[1] < (quarterWeight[2] - quarterWeight[1]))
			return false;
		
		return true;
	}
	/**
	 * Adds a box to the corresponding quarter
	 * @param xCoord
	 * @param yCoord
	 */
	protected abstract void addBox(int xCoord, int yCoord);
	/**
	 * Adds a hunter to the corresponding quarter
	 * @param xCoord
	 * @param yCoord
	 */
	protected abstract void addHunter(int xCoord, int yCoord);
	/**
	 * Calculates the number of hunters that can be added with the carpet staying balanced.
	 * @return	Returns the number that can be added or -1 if it can't be balanced.
	 */
	protected abstract long weCanAdd();
}
