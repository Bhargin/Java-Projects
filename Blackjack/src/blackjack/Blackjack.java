package blackjack;
import java.util.*;



public class Blackjack implements BlackjackEngine {

	// Int for player account amount.
	private int accountAmount;

	// Int for bet amount.
	private int betAmount;

	// Int for number of decks created.
	private int numOfDecks;

	// Int for random generation.
	private Random random;

	// ArrayList that stores deck cards.
	private ArrayList <Card> deck;

	// ArrayList that stores cards assigned to dealer.
	private ArrayList <Card> dealerHand;

	// ArrayList that stores cards assigned to player.
	private ArrayList <Card> playerHand;

	// Card[] array that stores deck.
	private Card[] gameDeck;

	// Card[] array that stores dealer cards.
	private Card[]dealer;

	// Card[] array that stores player cards.
	private Card[] player;

	// int[] array that stores all possible dealerHand values
	private int[] dealerTotal= new int[1];
	private int[] dealerTotal2= new int[2];

	// int[] array that stores all possible playerHand values.
	private int[] playerTotal= new int [1];
	private int[] playerTotal2=new int [2];

	// Int for total sum of dealerHand cards.
	private int dealerSum;

	// Int for total sum of playerHand cards.
	private int playerSum;

	// Int for sum of both player and dealer cards.
	private int sum;

	// Int that shows game status.
	private int gameStatus;


	// Constructor that initializes the game.
	public Blackjack(Random randomGenerator, int numberOfDecks) {

		// Set player account amount to 200.
		accountAmount = 200;

		// Set bet amount to 5;
		betAmount=5;

		// Store number of decks created.
		numOfDecks=numberOfDecks;
		random=randomGenerator;

		// Initialize dealerHand ArrayList. 
		dealerHand=new ArrayList<Card>();

		// Initialize playerHand ArrayList. 
		playerHand=new ArrayList<Card>();

	}

	// Return number of decks created.
	public int getNumberOfDecks() {
		return numOfDecks;
	}

	// Create and shuffle a new game deck.
	public void createAndShuffleGameDeck() {

		// Initialize deck ArrayList.
		deck=new ArrayList<Card>();

		// Create number of decks.
		for(int num=0; num<numOfDecks; num++) {

			// Create Card for each suit and value.
			for ( CardSuit suit : CardSuit.values()) {
				for (CardValue value : CardValue.values()) {
					deck.add(new Card(value, suit));
				}
			}
		}

		// Shuffle the deck created using random generator.
		Collections.shuffle(deck, random);

	}

	// Return deck ArrayList in Card[].
	public Card[] getGameDeck() {

		// Remove all dealt cards from deck.
		removeDealerCards();
		removePlayerCards();

		// Create gameDeck of deck size.
		gameDeck= new Card[deck.size()];

		// Copy each card in deck to gameDeck.
		for(int size=0; size<deck.size(); size++) {

			gameDeck[size]=deck.get(size);
		}


		return gameDeck;
	}

	// Deal cards to player and dealer.
	public void deal() {	

		// Remove dealt cards from deck.
		dealerHand.removeAll(dealerHand);
		playerHand.removeAll(playerHand);

		createAndShuffleGameDeck();

		// Add card to player hand.
		playerHand.add(deck.get(0)); //player

		// Add card to dealer hand.
		dealerHand.add(deck.get(1)); //dealer
		playerHand.add(deck.get(2)); // player
		dealerHand.add(deck.get(3)); //dealer

		// Change game status;
		gameStatus = GAME_IN_PROGRESS;

		// Set account amount.
		setAccountAmount(accountAmount-betAmount);

		// Set first card in dealerHand to facedown.
		dealerHand.get(0).setFaceDown();
		getGameDeck();


	}

	// Return dealerHand in Card[].
	public Card[] getDealerCards() {

		// Create a new dealer array of dealerHand size.
		dealer=new Card[dealerHand.size()];

		// Copy each dealer card to array.
		for (int index=0; index<dealerHand.size(); index++) {

			dealer[index]=dealerHand.get(index);
		}

		removeDealerCards();

		return dealer;	

	}

	// Return all possible sums of dealer card values.
	public int[] getDealerCardsTotal() {

		getDealerCards();

		// Calculate sum of cards in dealerHand ArrayList.
		addCards(dealerHand);

		// Initialize dealer cards sum to 0.
		dealerSum=0;
		
		if(sum>21) {
			
			dealerTotal=null;
			dealerSum=sum;
			return dealerTotal;
		}

		// Check every card in dealerHand.
		for(Card d: dealerHand) {

			// Check if dealer cards have Ace.
			if(d.getValue()==CardValue.Ace) {

				// Assign smaller value to the array.
				dealerTotal2[0]=sum;

				// Assign larger value to the array.
				dealerTotal2[1]=sum+10;

				// Check if dealer sum is not greater than 21.
				if (dealerTotal2[1]<=21) {

					// dealer sum is the largest possible value.
					dealerSum=dealerTotal2[1];

				} else {

					// dealer sum is the smallest possible value.
					dealerSum=dealerTotal2[0];
				}


				return dealerTotal2;

				// If no Ace card in hand then return total sum.
			} else {

				dealerTotal[0]=sum;
				dealerSum=dealerTotal[0];
			}
		}

		return dealerTotal;


	}

	// Evaluate dealer sum.
	public int getDealerCardsEvaluation() {

		getDealerCardsTotal();

		// If dealer sum is null return BUST.
		if(getDealerCardsTotal()==null) {

			gameStatus=PLAYER_WON;
			setAccountAmount(getAccountAmount()+(getBetAmount()*2));
			return BUST;
		}

		// Check for BLACKJACk and HAS_21
		if (dealerSum==21) {

			// If two cards add to 21
			if(dealerHand.size()==2) {

				return BLACKJACK;

				// If more than two cards add to 21.
			} else {

				return HAS_21;
			}
		}

		// Dealer sum is LESS_THAN_21.
		if(dealerSum<21) {

			return LESS_THAN_21;

			// Dealer BUST
		} if (dealerSum>21 ) {

			gameStatus=PLAYER_WON;
			setAccountAmount(getAccountAmount()+(getBetAmount()*2));
			return BUST;

		}
		return dealerSum;

	}

	// Return playerHand ArrayList in Card[].
	public Card[] getPlayerCards() {

		// Create player array of playerHand size.
		player=new Card[playerHand.size()];

		// Copy playerHand to player array.
		for (int p=0; p<playerHand.size(); p++) {

			player[p]=playerHand.get(p);
		}


		removePlayerCards();
		return player;	



	}

	// Return all possible values of player Hand.
	public int[] getPlayerCardsTotal() {

		getPlayerCards();

		// Sum of player cards.
		addCards(playerHand);

		// Initialize player sum to 0.
		playerSum=0;


		for(Card p: playerHand) {

			// If playerHand has Ace
			if(p.getValue()==CardValue.Ace) {

				// Assign smaller value to array.
				playerTotal2[0]=sum;

				// Assign larger value to array.
				playerTotal2[1]=sum+10;

				// Check if player sum is not BUST.
				if (playerTotal2[1]<=21) {

					playerSum=playerTotal2[1];

					// Otherwise take smaller sum.
				} else {

					playerSum=playerTotal2[0];
				}

				return playerTotal2;

				// If no Ace card in hand.
			} else {

				playerTotal[0]=sum;
				playerSum=playerTotal[0];
			}
		}

		return playerTotal;

	}

	// Evaluate player sum.
	public int getPlayerCardsEvaluation() {

		getPlayerCardsTotal();

		// Return BUST if playerHand is null
		if(getPlayerCardsTotal()==null) {

			gameStatus=DEALER_WON;
			setAccountAmount(getAccountAmount());
			return BUST;
		}

		// Check for BLACKJACK AND HAS_21.
		if (playerSum==21) {

			// If two cards add to 21.
			if(playerHand.size()==2) {

				return BLACKJACK;

				// More than two cards add up to 21.
			} else {

				return HAS_21;
			}

		}

		// LESS_THAN_21.
		if(playerSum<21) {

			return LESS_THAN_21;

			// player sum is BUST.
		} if (playerSum>21) {

			gameStatus=DEALER_WON;
			dealerHand.get(0).setFaceUp();
			setAccountAmount(getAccountAmount());
			return BUST;
			
		} 

		return playerSum;
	}

	// Hit player.
	public void playerHit() {

		// Add first on the deck to playerHand.
		playerHand.add(deck.get(0));

		// Evaluate playerHand.
		getPlayerCardsEvaluation();

		// Remove player cards from deck.
		removePlayerCards();
	}

	// Player stand.
	public void playerStand() {

		// Flip up dealer card.
		dealerHand.get(0).setFaceUp();		

		// Deal cards to dealer if dealer sum is less than 16 and not BUST.
		if(lessThan16() && getDealerCardsEvaluation()!=BUST) {

			dealerHand.add(deck.get(0));

			// Remove dealer cards from deck.
			removeDealerCards();

			// If dealer cards sum is not less than 16.
		} if (!lessThan16()) {

			// Evaluate dealer sum.
			getDealerCardsEvaluation();

			// Evaluate player sum.
			getPlayerCardsEvaluation();

			// Player is BUST
			if(getPlayerCardsEvaluation()==BUST) {

				gameStatus=DEALER_WON;
				// Adjust player account amount.
				setAccountAmount(getAccountAmount());
			}

			// Dealer sum is greater than player sum.
			if(dealerSum>playerSum) {

				gameStatus=DEALER_WON;

				// Adjust player account amount.
				setAccountAmount(getAccountAmount());

			}

			// Dealer sum is less than player sum.
			if(dealerSum<playerSum) {

				gameStatus=PLAYER_WON;

				// Adjust player account amount.
				setAccountAmount(getAccountAmount()+(getBetAmount()*2));
			}

			// DRAW.
			if(dealerSum==playerSum) {

				// Check if any hands have BLACKJACK
				if(getDealerCardsEvaluation()==BLACKJACK) {

					gameStatus=DEALER_WON;
					setAccountAmount(getAccountAmount());

				} else if ( getPlayerCardsEvaluation()==BLACKJACK) {

					gameStatus=PLAYER_WON;
					setAccountAmount(getAccountAmount()+(getBetAmount()*2));

					// Both hands have BLACKJACK or 21.
				} else {

					gameStatus=DRAW;
					setAccountAmount(getAccountAmount()+getBetAmount());
				}

			}	

		}		

	}

	// Return game status.
	public int getGameStatus() {
		return gameStatus;
	}

	// Set bet amount to the parameter listed.
	public void setBetAmount(int amount) {
		betAmount=amount;
	}

	// Get bet amount.
	public int getBetAmount() {
		return betAmount;
	}

	// Set player account amount to the parameter listed.
	public void setAccountAmount(int amount) {	
		accountAmount= amount;
	}

	// Return player account amount.
	public int getAccountAmount() {
		return accountAmount;
	}

	// Remove dealer cards from deck.
	private void removeDealerCards() {

		// Check if any dealer cards match any deck cards.
		if (dealer!=null) {
			for(int index=0; index<dealer.length; index++) {

				deck.remove(dealer[index]);
			}
		}
	}

	// Remove player cards from deck.
	private void removePlayerCards() {

		// Check if any player cards match any deck cards.
		if(player!=null) {
			for(int index=0; index<player.length; index++) {

				deck.remove(player[index]);
			}
		}
	}

	// Check if dealer is less than 16.
	private boolean lessThan16() {

		getDealerCardsEvaluation();

		if (dealerSum<16) {

			return true;
		}

		return false;

	}

	// Return sum of an ArrayList of cards.
	private int addCards(ArrayList <Card> cards) {

		sum = 0;

		// Add values of each card in ArrayList.
		for (Card c: cards) {

			sum+= c.getValue().getIntValue();

		}

		return sum;

	}

}