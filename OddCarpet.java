/**
 * A carpet with an odd size. 
 * Here we treat each square of space on the carpet and each hunter as 2 so we don't have to deal 
 * with fractions.
 * @author David
 *
 */
public class OddCarpet extends Carpet{
	//0 is between 1-2, 1 is between 2-4, 2 is between 3-4 and 3 is between 1-3
	private int middleSpaces[];
	private boolean middleTaken;
	/**
	 * Constructor to create a carpet of size 'size'
	 * @param size
	 */
	public OddCarpet(int size) {
		this.size = size;
		this.quarterWeight = new long [4];
		this.quarterSpaces = new long [4];
		this.middleSpaces = new int [4];
		this.initEmptySpaces();
		middleTaken = false;
	}
	/**
	 * Deep copy constructor
	 * @param c OddCarpet to copy
	 */
	public OddCarpet(OddCarpet c) {
		this.size = c.size;
		this.middleTaken = c.middleTaken;
		quarterSpaces = new long [4];
		quarterWeight = new long [4];
		middleSpaces = new int [4];
		for (int i = 0; i < quarterSpaces.length; i++) {
			this.quarterSpaces[i] = c.quarterSpaces[i];
			this.quarterWeight[i] = c.quarterWeight[i];
			this.middleSpaces[i] = c.middleSpaces[i];
		}
	}

	@Override
	public void addBox(int xCoord, int yCoord) {
		int middle = (size + 1)/2;
		if(xCoord == yCoord && yCoord == middle) {
			middleTaken = true;
		}
		else {
			int index = this.findLocation(xCoord - 1, yCoord - 1);
			if(index == 10 || index == 13 || index == 20 || index == 23) {
				this.addMiddleBox(index);
				this.updateMiddleSpace(index);
			}
			else 
				this.quarterSpaces[index] = this.quarterSpaces[index] - 2;
		}

	}
	@Override
	public void addHunter(int xCoord, int yCoord) {
		int middle = (size + 1)/2;
		if(xCoord == yCoord && yCoord == middle) {
			middleTaken = true;
		}
		else {
			int index = this.findLocation(xCoord - 1, yCoord - 1);
			if(index == 10 || index == 13 || index == 20 || index == 23) {
				this.addMiddleHunter(index);
				this.updateMiddleSpace(index);
			}
			else {
				this.quarterSpaces[index] = this.quarterSpaces[index] - 2;
				this.quarterWeight[index] = this.quarterWeight[index] + 2; 
			}
		}
	}
	@Override
	public long weCanAdd() {
		if(!canBeBalaned())
			return -1;
		int result = this.balanceAndFillCarpet();
		if(result == -1)
			return -1;
		if(!middleTaken)
			result++;
		return result;
	}


	//private methods*************************************
	/**
	 * Finds where given coordinates (x,y) are on the carpet.
	 * 1 is top left, 2 is top right, 3 is bottom left, 4 is bottom right, 12 is between 1-2, 13 is between 1-3,
	 * 24 is between 2-4, 34 is between 3-4 and 0 is right in the middle. 
	 * @param xCoord
	 * @param yCoord
	 * @return
	 */
	private int findLocation(int xCoord, int yCoord) {
		int middle = (this.size-1)/2;
		int result;
		if(xCoord == middle || yCoord == middle) {
			result = this.handleMiddleIndex(xCoord, yCoord);
			return result;
		}
		else if(xCoord < middle){//x is in the left half
			if(yCoord < middle)//y is in the top half
				return 0;
			else return 2;
		}
		else {//x is in the right half
			if(yCoord < middle)//y is in top half
				return 1;
			else return 3;
		}
	}
	/**
	 * Given coordinates, finds in between what 2 quarters they belongs.
	 * @param xCoord
	 * @param yCoord
	 * @return Returns the a numbers representing 2 quarters:
	 * 10 is between quarter 1 and quarter 0
	 * 20 is between quarter 2 and 0 
	 * 13 is between quarter 1 and 3 
	 * 23 is between quarter 2 and 3
	 */
	private int handleMiddleIndex(int xCoord, int yCoord) {
		int middle = (this.size-1)/2;
		if(xCoord == middle) {
			if(yCoord > middle) //y is in bottom half
				return 23;
			else if(yCoord < middle)//y is in top half
				return 10;
			else 	//y also in the middle
				return 0;	
		}
		else {
			if(xCoord > middle) //y is in the middle and x in right half
				return 13;
			return 20;	//x in left half
		}
	}
	/**
	 * Adds a box to the space between 2 quarters, updates the 2 quarters affected
	 * @param index 10 is between quarter 1 and quarter 0 and so on.
	 */
	private void addMiddleBox(int index){
		int firstIndex = index%10;
		int secondIndex = index/10;
		this.quarterSpaces[firstIndex]--;
		this.quarterSpaces[secondIndex]--;
	}
	/**
	 * Adds a hunter to the space between 2 quarters, updates the 2 quarters affected
	 * @param index 10 is between quarter 1 and quarter 0 and so on.
	 */
	private void addMiddleHunter(int index) {
		int firstIndex = index%10;
		int secondIndex = index/10;
		this.quarterSpaces[firstIndex]--;
		this.quarterSpaces[secondIndex]--;
		this.quarterWeight[firstIndex]++;
		this.quarterWeight[secondIndex]++;
	}
	/**
	 * Tries to balance the carpet and fills the spaces left afterwards.
	 * @return If the carpet could be balanced, returns the number of hunters it added, returns -1 if not.
	 * 
	 */
	private int balanceAndFillCarpet() {
		OddCarpet second = new OddCarpet(this);
		OddCarpet third = new OddCarpet(this);
		OddCarpet fourth = new OddCarpet(this);
		//first try
		int firstResult = this.balanceNoMiddle_v1();
		firstResult = firstResult + this.balanceMiddle();
		firstResult = firstResult + this.fillCarpet_v1();
		if(!this.isBalanced())
			firstResult = -1;
		//second try
		//The only difference is that here we add 1 hunter less in each corner.
		int secondResult = second.balanceNoMiddle_v2();
		secondResult = secondResult + second.balanceMiddle();
		secondResult = secondResult + second.fillCarpet_v1();
		if(!second.isBalanced())
			secondResult = -1;
		//third try
		int thirdResult = third.fixRemainder();
		thirdResult = thirdResult + third.balanceNoMiddle_v1();
		thirdResult = thirdResult + third.balanceMiddle();
		thirdResult = thirdResult + third.fillCarpet_v1();
		if(!third.isBalanced())
			thirdResult = -1;
		//fourth try
		int fourthResult = fourth.fixRemainder();
		fourthResult = fourthResult + fourth.balanceNoMiddle_v1();
		fourthResult = fourthResult + fourth.balanceMiddle();
		fourthResult = fourthResult + fourth.fillCarpet_v2();
		if(!fourth.isBalanced())
			fourthResult = -1;
		int max = Math.max(firstResult, secondResult);
		max = Math.max(max, thirdResult); //take the maximum between the 4 results
		max = Math.max(max, fourthResult);
		return max;

	}

	/**
	 * Fixes the carpet so the the 2 opposite quarters will have the same remainder mod 2.
	 * @return Returns 1 if fixed and 0 otherwise
	 */
	private int fixRemainder() {
		if((quarterWeight[0]%2 != quarterWeight[3]%2 && quarterWeight[1]%2 != quarterWeight[2]%2)||
				(quarterSpaces[0]%2 != quarterSpaces[3]%2 && quarterSpaces[1]%2 != quarterSpaces[2]%2)) {
			for(int i = 0; i < 4; i ++) {
				if(middleSpaces[i] != 0 ) {
					switch (i) {
					case 0:
						this.addMiddleHunter(10);
						this.updateMiddleSpace(10);
						break;
					case 1:
						this.addMiddleHunter(13);
						this.updateMiddleSpace(13);
						break;
					case 2:
						this.addMiddleHunter(23);
						this.updateMiddleSpace(23);
						break;
					case 3:
						this.addMiddleHunter(20);
						this.updateMiddleSpace(20);
					default:
						break;
					}
					return 1;
				}
			}
		}
		return 0;
	}
	/**
	 * Fills the carpet with it staying balanced.
	 * @return Returns the number added.
	 */
	private int fillCarpet_v1() {
		int added = this.addOpposite();
		added = added + this.fillMiddleSpaces();
		return added;
	}
	/**
	 * Fills the carpet with it staying balanced.
	 * @return Returns the number added.
	 */
	private int fillCarpet_v2() {
		int added = this.fillMiddleSpaces();
		added = added + this.addOpposite();
		return added;
	}
	/**
	 * Fills hunters to 2 opposite quarter/middle spaces.
	 * For example: If 0 and 3 have free spaces, we can add to both of them and the carpet will stay balanced.
	 * @return	Returns the number of hunters added.
	 */
	private int addOpposite() {
		int topLeft =  (int) (quarterSpaces[0] - middleSpaces[0] -middleSpaces[3]);
		int topRight = (int) (quarterSpaces[1] - middleSpaces[0] - middleSpaces[1]);
		int botLeft = (int) (quarterSpaces[2] - middleSpaces[3] - middleSpaces[2]);
		int botRight = (int) (quarterSpaces[3] - middleSpaces[2] - middleSpaces[1]);
		int added = 0;
		if(topLeft > 0 && botRight > 0) { //if less than 0 means this quarter has no space that is not shared with other quarter.
			added = Math.min(topLeft, botRight);
			quarterWeight[0] = quarterWeight[0] + added;
			quarterWeight[3] = quarterWeight[3] + added;
			quarterSpaces[0] = quarterSpaces[0] - added;
			quarterSpaces[3] = quarterSpaces[3] - added;
		}
		int temp;
		if(topRight > 0 && botLeft > 0) {
			temp = Math.min(topRight, botLeft);
			added = added + temp;
			quarterWeight[1] = quarterWeight[1] + temp;
			quarterWeight[2] = quarterWeight[2] + temp;
			quarterSpaces[1] = quarterSpaces[1] - temp;
			quarterSpaces[2] = quarterSpaces[2] - temp;
		}
		temp = Math.min(middleSpaces[0], middleSpaces[2]);
		middleSpaces[0] = middleSpaces[0] - temp;
		middleSpaces[2] = middleSpaces[2] - temp;
		for(int i = 0; i < temp; i++) {
			this.addMiddleHunter(10);
			this.addMiddleHunter(23);
		}
		added = added + temp*2;
		temp = Math.min(middleSpaces[3], middleSpaces[1]);
		middleSpaces[3] = middleSpaces[3] - temp;
		middleSpaces[1] = middleSpaces[1] - temp;
		for(int i = 0; i < temp; i++) {
			this.addMiddleHunter(20);
			this.addMiddleHunter(13);
		}
		added = added + temp*2;
		return added;
	}
	private int fillMiddleSpaces() {
		int added = 0;
		if(middleSpaces[0] > 1) {
			int botRight = (int) (quarterSpaces[3] - middleSpaces[1] - middleSpaces[2]);
			int botLeft = (int)(quarterSpaces[2] - middleSpaces[3] - middleSpaces[2]);
			int toAdd = (int) Math.min(middleSpaces[0], botRight);
			toAdd = (int) Math.min(toAdd, botLeft);
			if(toAdd%2 != 0)
				toAdd--;
			quarterWeight[2] = quarterWeight[2] + toAdd;
			quarterWeight[3] = quarterWeight[3] + toAdd;
			quarterSpaces[2] = quarterSpaces[2] - toAdd;
			quarterSpaces[3] = quarterSpaces[3] - toAdd;
			middleSpaces[0] = middleSpaces[0] - toAdd;
			for(int i = 0; i < toAdd; i++)
				this.addMiddleHunter(10);
			added = added + toAdd*2;
		}
		if(middleSpaces[1] > 1) {
			int topLeft = (int) (quarterSpaces[0] - middleSpaces[3] - middleSpaces[0]);
			int botLeft = (int)(quarterSpaces[2] - middleSpaces[3] - middleSpaces[2]);
			int toAdd = (int) Math.min(middleSpaces[1], topLeft);
			toAdd = (int) Math.min(toAdd, botLeft);
			if(toAdd%2 != 0)
				toAdd--;
			quarterWeight[0] = quarterWeight[0] + toAdd;
			quarterWeight[2] = quarterWeight[2] + toAdd;
			quarterSpaces[0] = quarterSpaces[0] - toAdd;
			quarterSpaces[2] = quarterSpaces[2] - toAdd;
			middleSpaces[1] = middleSpaces[1] - toAdd;
			for(int i = 0; i < toAdd; i++)
				this.addMiddleHunter(13);
			added = added + toAdd*2;
		}
		if(middleSpaces[2] > 1) {
			int topLeft = (int) (quarterSpaces[0] - middleSpaces[3] - middleSpaces[0]);
			int topRight = (int)(quarterSpaces[1] - middleSpaces[0] - middleSpaces[1]);
			int toAdd = (int) Math.min(middleSpaces[2], topLeft);
			toAdd = (int) Math.min(toAdd, topRight);
			if(toAdd%2 != 0)
				toAdd--;
			quarterWeight[1] = quarterWeight[1] + toAdd;
			quarterWeight[0] = quarterWeight[0] + toAdd;
			quarterSpaces[1] = quarterSpaces[1] - toAdd;
			quarterSpaces[0] = quarterSpaces[0] - toAdd;
			middleSpaces[2] = middleSpaces[2] - toAdd;
			for(int i = 0; i < toAdd; i++)
				this.addMiddleHunter(23);
			added = added + toAdd*2;
		}
		if(middleSpaces[3] > 1) {
			int botRight = (int) (quarterSpaces[3] - middleSpaces[1] - middleSpaces[2]);
			int topRight = (int)(quarterSpaces[1] - middleSpaces[0] - middleSpaces[1]);
			int toAdd = (int) Math.min(middleSpaces[3], botRight);
			toAdd = (int) Math.min(toAdd, topRight);
			if(toAdd%2 != 0)
				toAdd--;
			quarterWeight[1] = quarterWeight[1] + toAdd;
			quarterWeight[3] = quarterWeight[3] + toAdd;
			quarterSpaces[1] = quarterSpaces[1] - toAdd;
			quarterSpaces[3] = quarterSpaces[3] - toAdd;
			middleSpaces[3] = middleSpaces[3] - toAdd;
			for(int i = 0; i < toAdd; i++)
				this.addMiddleHunter(20);
			added = added + toAdd*2;
		}
		return added;
	}
	/**
	 * Sets the number of empty spaces in each corner and in the spaces shared with 2 corners.
	 */
	private void initEmptySpaces() {
		int size = this.getSize();
		int quarterSpaces_ = (size*size - 1)/2; //each slot counts as 2, that's why not dividing by 4
		int middleSpaces = (size - 1)/2; //spaces shared with 2 quarters
		for (int i = 0; i < this.quarterSpaces.length; i++) {
			this.quarterSpaces[i] = quarterSpaces_;
			this.middleSpaces[i] = middleSpaces;
		}
	}
	/**
	 * Updates the number of spaces available between 2 quarters.
	 * 10 is between quarter 1 and quarter 0 and so on. 
	 * @param index
	 */
	private void updateMiddleSpace(int index) {
		switch (index) {
		case 10:
			this.middleSpaces[0]--;
			break;
		case 13:
			this.middleSpaces[1]--;
			break;
		case 23:
			this.middleSpaces[2]--;
			break;
		case 20:
			this.middleSpaces[3]--;
			break;
		default:
			break;
		}
	}
	/**
	 * Adds hunters to the quarters with missing weight without touching spaces between 2 quarters
	 * @return	returns the amount added
	 */
	private int balanceNoMiddle_v1() {
		int result;
		if(quarterWeight[0] > quarterWeight[3]) {
			int diff = (int)(quarterWeight[0] - quarterWeight[3]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff++;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[3] - this.middleSpaces[2] - this.middleSpaces[1]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[3] = quarterWeight[3] + added;
			quarterSpaces[3] = quarterSpaces[3] - added;
			result = added/2;
		}
		else {
			int diff = (int)(quarterWeight[3] - quarterWeight[0]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff++;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[0] - this.middleSpaces[0] - this.middleSpaces[3]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[0] = quarterWeight[0] + added;
			quarterSpaces[0] = quarterSpaces[0] - added;
			result = added/2;
		}
		if(quarterWeight[1] > quarterWeight[2]) {
			int diff = (int)(quarterWeight[1] - quarterWeight[2]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff++;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[2] - this.middleSpaces[3] - this.middleSpaces[2]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[2] = quarterWeight[2] + added;
			quarterSpaces[2] = quarterSpaces[2] - added;
			added = added/2;
			result = result + added;

		}
		else {
			int diff = (int)(quarterWeight[2] - quarterWeight[1]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff++;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[1] - this.middleSpaces[0] - this.middleSpaces[1]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[1] = quarterWeight[1] + added;
			quarterSpaces[1] = quarterSpaces[1] - added;
			added = added/2;
			result = result + added;
		}
		return result;
	}
	/**
	 * Adds hunters to the quarters with missing weight without touching spaces between 2 quarters
	 * @return	returns the amount added
	 */
	private int balanceNoMiddle_v2() {
		int result;
		if(quarterWeight[0] > quarterWeight[3]) {
			int diff = (int)(quarterWeight[0] - quarterWeight[3]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff--;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[3] - this.middleSpaces[2] - this.middleSpaces[1]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[3] = quarterWeight[3] + added;
			quarterSpaces[3] = quarterSpaces[3] - added;
			result = added/2;
		}
		else {
			int diff = (int)(quarterWeight[3] - quarterWeight[0]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff--;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[0] - this.middleSpaces[0] - this.middleSpaces[3]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[0] = quarterWeight[0] + added;
			quarterSpaces[0] = quarterSpaces[0] - added;
			result = added/2;
		}
		if(quarterWeight[1] > quarterWeight[2]) {
			int diff = (int)(quarterWeight[1] - quarterWeight[2]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff--;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[2] - this.middleSpaces[3] - this.middleSpaces[2]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[2] = quarterWeight[2] + added;
			quarterSpaces[2] = quarterSpaces[2] - added;
			added = added/2;
			result = result + added;

		}
		else {
			int diff = (int)(quarterWeight[2] - quarterWeight[1]); //sizes of odd carpets are much smaller
			if(diff%2 != 0)
				diff--;
			//first we'll fill the spaces not affecting any other quarters
			int spacesNoMiddle = (int)(this.quarterSpaces[1] - this.middleSpaces[0] - this.middleSpaces[1]);
			int added = Math.min(spacesNoMiddle, diff);
			quarterWeight[1] = quarterWeight[1] + added;
			quarterSpaces[1] = quarterSpaces[1] - added;
			added = added/2;
			result = result + added;
		}
		return result;
	}
	/**
	 * Tries to balance the carpet by adding to the spaces shared with 2 corners.
	 * @return Returns the number of hunters added.
	 */
	private int balanceMiddle() {
		int result = 0;
		int firstDiff = (int) (this.quarterWeight[0] - this.quarterWeight[3]);
		int secondDiff = (int)(this.quarterWeight[1] - this.quarterWeight[2]);
		if(firstDiff < 0 && secondDiff < 0) {
			firstDiff = -firstDiff;
			secondDiff = -secondDiff;
			int spaces = middleSpaces[0]; //middle spaces between the 0-1 quarters
			int min = Math.min(firstDiff, secondDiff);
			min = Math.min(spaces, min);
			result = result + min;	
			//update the quarters affected.
			middleSpaces[0] = middleSpaces[0] - min;
			quarterWeight[0] = quarterWeight[0] + min;
			quarterWeight[1] = quarterWeight[1] + min;
			quarterSpaces[0] = quarterSpaces[0] - min;
			quarterSpaces[1] = quarterSpaces[1] - min;
		}
		else if(firstDiff < 0 && secondDiff > 0) {
			firstDiff = -firstDiff;
			int spaces = middleSpaces[3];//middle spaces between the 0-2 quarters
			int min = Math.min(secondDiff, firstDiff);
			min = Math.min(spaces, min);
			result = result + min;
			//update the quarters affected.
			middleSpaces[3] = middleSpaces[3] - min;
			quarterWeight[0] = quarterWeight[0] + min;
			quarterWeight[2] = quarterWeight[2] + min;
			quarterSpaces[0] = quarterSpaces[0] - min;
			quarterSpaces[2] = quarterSpaces[2] - min;
		}
		else if(firstDiff > 0 && secondDiff > 0) {
			int spaces = middleSpaces[2];//middle spaces between the 0-2 quarters
			int min = Math.min(secondDiff, firstDiff);
			min = Math.min(spaces, min);
			result = result + min;
			//update the quarters affected.
			middleSpaces[2] = middleSpaces[2] - min;
			quarterWeight[2] = quarterWeight[2] + min;
			quarterWeight[3] = quarterWeight[3] + min;
			quarterSpaces[2] = quarterSpaces[2] - min;
			quarterSpaces[3] = quarterSpaces[3] - min;
		}
		else if(firstDiff > 0 && secondDiff < 0) {
			secondDiff = -secondDiff;
			int spaces = middleSpaces[1];//middle spaces between the 0-2 quarters
			int min = Math.min(secondDiff, firstDiff);
			min = Math.min(spaces, min);
			result = result + min;
			//update the quarters affected.
			middleSpaces[1] = middleSpaces[1] - min;
			quarterWeight[1] = quarterWeight[1] + min;
			quarterWeight[3] = quarterWeight[3] + min;
			quarterSpaces[1] = quarterSpaces[1] - min;
			quarterSpaces[3] = quarterSpaces[3] - min;
		}
		return result;
	}
	/**
	 * Checks if the carpet is balanced.
	 * @return	Returns true if balanced and false otherwise.
	 */
	private boolean isBalanced() {
		return (quarterWeight[0] == quarterWeight[3] && quarterWeight[1] == quarterWeight[2]);
	}
}
