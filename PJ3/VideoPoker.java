package PJ3;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses OneDeck and Card objects to implement video poker game.
 */



public class VideoPoker {

    // default constant values
    private static final int defaultBalance=100;
    private static final int numberCards=5;

    // default constant payout value and playerHand types
    private static final int[]    winningMultipliers={1,2,3,5,6,9,25,50,250};
    private static final String[] winningHands={ 
	  "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush",
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // use one deck of cards
    private final OneDeck thisDeck;

    // holding current player 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = defaultBalance */
    public VideoPoker()
    {
	this(defaultBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        thisDeck = new OneDeck();
    }

    /** This display the payout table based on winningMultipliers and 
      * winningHands arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = winningMultipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(winningHands[i]+"\t|\t"+winningMultipliers[i]);
	}
	System.out.println("\n\n---------------------------------------");
    }

    /** Check current playerHand using winningMultipliers and winningHands arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
		// implement this method
		//this.playerHand is arrayList of current hand
		//CHECK HANDS
		if (isRoyalFlush()) {
			System.out.println(winningHands[8] + "!");
		} else if (isStraightFlush()) {
			System.out.println(winningHands[7] + "!");
		} else if (isFourOfAKind()) {
			System.out.println(winningHands[6] + "!");
		} else if (isFullHouse()) {
			System.out.println(winningHands[5] + "!");
		} else if (isFlush()) {
			System.out.println(winningHands[4] + "!");
		} else if (isStraight()) {
			System.out.println(winningHands[3] + "!");
		} else if (isThreeOfAKind()) {
			System.out.println(winningHands[2] + "!");
		} else if (isTwoPair()) {
			System.out.println(winningHands[1] + "!");
		} else if (isHighPair()) {
			System.out.println(winningHands[0] + "!");
		} else {
			System.out.println("Sorry, you lost!");
		}
    }

    /*************************************************
     *   add additional private methods here ....
     *
     *************************************************/
	//Check if royal flush: 10, J, Q, K, A all same suit
	private boolean isRoyalFlush() {
		List<Integer> cardRanks = new ArrayList<Integer>();
		for(Card c : playerHand) {
			cardRanks.add(c.getRank());
		}
		Collections.sort(cardRanks);
		return isStraightFlush() && cardRanks.get(0).intValue() == 1;
	}

	//Check if straight flush: 5 consecutive numbers all same suit
	private boolean isStraightFlush() {
		return isFlush() && isStraight();
	}

	//Check if four of a kind: 4 cards of the same rank
	private boolean isFourOfAKind() {
		List<Integer> cardRanks = new ArrayList<Integer>();
		int streak = 0;

		for(Card c : playerHand) {
			cardRanks.add(c.getRank());
		}
		Collections.sort(cardRanks);

		for(int i = 1; i < cardRanks.size(); i++) {
			if(cardRanks.get(i) == cardRanks.get(i-1)) {
				streak++;
				if(streak >= 3) break;
			}
			else {
				streak = 0;
			}
		}
		return streak >= 3;
	}

	//Check if full house:
	private boolean isFullHouse() {
		//FIRST CARD
		int firstRank = playerHand.get(0).getRank();
		int sumFirstRank = 1;

		//LAST CARD
		int lastRank = playerHand.get(playerHand.size()-1).getRank();
		int sumLastRank = 1;

		//count cards that are similar to the first and last cards
		for (int i = 1; i < playerHand.size() - 1; i++) {
			if (playerHand.get(i).getRank() == firstRank) sumFirstRank++;
			if (playerHand.get(i).getRank() == lastRank) sumLastRank++;
		}

		if (sumFirstRank == 3 && sumLastRank == 2) return true;
		if (sumFirstRank == 2 && sumLastRank == 3) return true;
		return false;
	}

	//Check if flush: 5 same suit
	//ONLY checks if flush, could be straight
	private boolean isFlush() {
		int firstCardSuit = playerHand.get(0).getSuit();

		for(Card c : playerHand){
			if(c.getSuit() != firstCardSuit)
				return false;
		}

		return true;
	}

	//Check if straight: 5 consecutive numbers
	//note: will also return true for straight flush, ONLY checks if its straight
	private boolean isStraight() {
		List<Integer> cardRanks = new ArrayList<Integer>();

		for(Card c : playerHand) {
			cardRanks.add(c.getRank());
		}
		Collections.sort(cardRanks);

		if(cardRanks.get(0).intValue() == 1) {
			cardRanks.remove(0);
			cardRanks.add(new Integer(14));
		}

		for(int i = 0; i < cardRanks.size()-1; i++) {
			int curRank = cardRanks.get(i);
			int nextRank = cardRanks.get(i+1);
			if(curRank != nextRank-1) {
				return false;
			}
		}
		return true;
	}

	//Check if three of a kind: 3 cards of the same rank
	private boolean isThreeOfAKind() {
		List<Integer> cardRanks = new ArrayList<Integer>();
		int streak = 0;

		for(Card c : playerHand) {
			cardRanks.add(c.getRank());
		}
		Collections.sort(cardRanks);

		for(int i = 1; i < cardRanks.size(); i++) {
			if(cardRanks.get(i) == cardRanks.get(i-1)) {
				streak++;
				if(streak >= 2) break;
			}
			else {
				streak = 0;
			}
		}
		return streak >= 2;
	}

	//Check if there is a two pair: 3 cards of the same rank
	private boolean isTwoPair() {
		int numPairs = 0;
		int foundPairIndex = -1;
		for(int i = 0; i < playerHand.size(); i++) {
			for(int j = 0; j < playerHand.size(); j++) {
				int iRank = playerHand.get(i).getRank();
				int jRank = playerHand.get(j).getRank();
				if(iRank == jRank && i != j && j != foundPairIndex && i != foundPairIndex) {
					numPairs++;
					foundPairIndex = j;
					if(numPairs >= 2) return true;
				}
			}
		}
		return false;
	}

	//Check if there is a high pair, a pair of J, Q, K, or A
	private boolean isHighPair() {
		for(int i = 0; i < playerHand.size(); i++) {
			for(int j = 0; j < playerHand.size(); j++) {
				if(playerHand.get(i).getRank() == playerHand.get(j).getRank() && i != j && playerHand.get(i).getRank() > 9) {
					return true;
				}
			}
		}
		return false;
	}




	public void play()
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 		++	
     * 		show balance, get bet 
     *		verify bet value, update balance
     *		reset deck, shuffle deck, 
     *		deal cards and display cards
     *		ask for positions of cards to keep  
     *          get positions in one input line
     *		update cards
     *		check hands, display proper messages
     *		update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *			ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *			else : showPayoutTable() if user wants to see it
     *			goto ++
     */

		// implement this method
		Scanner in = new Scanner(System.in);

		//PRINT OUT PAYOUT TABLE
		showPayoutTable();

		while(true) {
			//SHOW BALANCE AND BET
			System.out.println("Balance: " + playerBalance);
			int bet = Integer.MAX_VALUE;
			while (bet > playerBalance) {
				System.out.print("Enter Bet: ");
				bet = in.nextInt();
				if (bet > playerBalance) {
					System.out.println("You cannot bet more money than you have!");
				}
			}
			playerBalance -= bet;

			//RESET AND SHUFFLE DECK
			thisDeck.reset();
			thisDeck.shuffle();

			//DEAL AND DISPLAY CARDS
			try {
				playerHand = thisDeck.deal(5);
			} catch (PlayingCardException e) {
				System.out.println("The deck has too little cards!");
			}
			System.out.println("Hand: " + playerHand.toString());

			//ASK USER WHICH CARDS TO KEEP AND UPDATE CARDS
			System.out.print("Enter position(s) of cards to keep (e.g. 1 4 5): ");
			in.nextLine(); //dummy read
			String input = "";
			//try catch is so that there is no error when player enters nothing, so it just does not change the hand.
			try {
				input = in.nextLine();
				String[] positions = input.split(" ");
				for(int i = 0; i < positions.length; i++) {
					try {
						playerHand.set(Integer.parseInt(positions[i])-1, thisDeck.deal(1).get(0));
					} catch (PlayingCardException e) {
						System.out.println("The deck has too little cards!");
					}
				}
			} catch(NumberFormatException e) {

			}
			System.out.println("Hand: " + playerHand.toString());

			//CHECK HANDS
			if (isRoyalFlush()) {
				bet *= winningMultipliers[8];
				playerBalance += bet;
				System.out.println(winningHands[8] + "!");
			} else if (isStraightFlush()) {
				bet *= winningMultipliers[7];
				playerBalance += bet;
				System.out.println(winningHands[7] + "!");
			} else if (isFourOfAKind()) {
				bet *= winningMultipliers[6];
				playerBalance += bet;
				System.out.println(winningHands[6] + "!");
			} else if (isFullHouse()) {
				bet *= winningMultipliers[5];
				playerBalance += bet;
				System.out.println(winningHands[5] + "!");
			} else if (isFlush()) {
				bet *= winningMultipliers[4];
				playerBalance += bet;
				System.out.println(winningHands[4] + "!");
			} else if (isStraight()) {
				bet *= winningMultipliers[3];
				playerBalance += bet;
				System.out.println(winningHands[3] + "!");
			} else if (isThreeOfAKind()) {
				bet *= winningMultipliers[2];
				playerBalance += bet;
				System.out.println(winningHands[2] + "!");
			} else if (isTwoPair()) {
				bet *= winningMultipliers[1];
				playerBalance += bet;
				System.out.println(winningHands[1] + "!");
			} else if (isHighPair()) {
				bet *= winningMultipliers[0];
				playerBalance += bet;
				System.out.println(winningHands[0] + "!");
			} else {
				System.out.println("Sorry, you lost!\n");
			}

			//END GAME OR ASK FOR ANOTHER ROUND
			if (playerBalance <= 0) {
				System.out.println("Your balance is 0\nBye!");
				break;
			} else {
				System.out.print("Your balance: $" + playerBalance + ", one more game (y or n)? :");
				String response = in.nextLine();
				if (!response.equalsIgnoreCase("y")) {
					break;
				}
				System.out.print("Want to see payout table (y or n)? :");
				response = in.nextLine();
				if(response.equalsIgnoreCase("y")) {
					showPayoutTable();
				}
			}
		}
    }


    /*************************************************
     *   do not modify methods below
     *   methods are used for testing your program.
     *
     *************************************************/

    /** testCheckHands is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 
    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(1,4));
		playerHand.add(new Card(10,4));
		playerHand.add(new Card(12,4));
		playerHand.add(new Card(11,4));
		playerHand.add(new Card(13,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(9,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(8,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(5,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(8,4));
		playerHand.add(new Card(8,1));
		playerHand.add(new Card(12,4));
		playerHand.add(new Card(8,2));
		playerHand.add(new Card(8,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(11,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(11,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(9,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Royal Pair
		playerHand.set(0, new Card(3,2));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// non Royal Pair
		playerHand.set(2, new Card(3,4));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /** testOneDeck() is used to test OneDeck class  
     *  testOneDeck() should execute OneDeck's main()
     */ 
    public void testOneDeck()
    {
    	OneDeck tmp = new OneDeck();
        tmp.main(null);
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
