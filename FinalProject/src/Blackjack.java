import java.util.*;

public class Blackjack {
	
	static String[] deckFace = {"Ace of Hearts", "2 of Hearts", "3 of Hearts", "4 of Hearts", "5 of Hearts", "6 of Hearts", "7 of Hearts", "8 of Hearts", "9 of Hearts", "10 of Hearts", "Jack of Hearts", "Queen of Hearts", "King of Hearts", "Ace of Diamonds", "2 of Diamonds", "3 of Diamonds", "4 of Diamonds", "5 of Diamonds", "6 of Diamonds", "7 of Diamonds", "8 of Diamonds", "9 of Diamonds", "10 of Diamonds", "Jack of Diamonds", "Queen of Diamonds", "King of Diamonds", "Ace of Clubs", "2 of Clubs", "3 of Clubs", "4 of Clubs", "5 of Clubs", "6 of Clubs", "7 of Clubs", "8 of Clubs", "9 of Clubs", "10 of Clubs", "Jack of Clubs", "Queen of Clubs", "King of Clubs", "Ace of Spades", "2 of Spades", "3 of Spades", "4 of Spades", "5 of Spades", "6 of Spades", "7 of Spades", "8 of Spades", "9 of Spades", "10 of Spades", "Jack of Spades", "Queen of Spades", "King of Spades" };
	static int[] deckValue = {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, };
	
	static ArrayList<String> currentDeckFace;
	static ArrayList<Integer> currentDeckValue;
	
	static Scanner keyboard = new Scanner(System.in);
	static Random random = new Random();
	
	static ArrayList<String> userFace;		//names of cards in user's hand
	static ArrayList<Integer> userValue;	//values of cards in user's hand
	static ArrayList<String> dealerFace;	//names of cards in dealer's hand
	static ArrayList<Integer> dealerValue;	//vales of cards in dealers hand
	
	static int user;
	static int dealer;
	static double bet;
	static double currentCash;
	static double totalWinnings;
	
	static Cards cards = new Cards();
	
	static boolean keepPlaying = true; //used for while loop to keep playing
	static boolean playing = true; //used for do while loop for each time a card is added to user's hand
	
	
	public static void main(String[] args) {
		
		boolean cont = false; //used for do while loop to ask if the user wants to keep playing
		boolean checkMoney = true;
		final double startingCash;

		
		currentDeckFace = new ArrayList<String>();
		currentDeckValue = new ArrayList<Integer>();
		userFace = new ArrayList<String>();
		userValue = new ArrayList<Integer>();
		dealerFace = new ArrayList<String>();
		dealerValue = new ArrayList<Integer>();
		
		
		//used to print out instructions to user
		intro();
		
		//asks user how much money they have to play with
		System.out.println("How much money do you have to play with? Enter the amount:");
		startingCash = keyboard.nextDouble();
		currentCash = startingCash;
		keyboard.nextLine(); //consumes rest of line
		
		//loop that allows the user to play again
		while (keepPlaying) {
			
			initializeCards();	//clears old deck to create a new one
			
			
			do {	//loop to make sure the user doesn't bet more then they have
				System.out.println("How much money would you like to bet? Enter the amount:");
				bet = keyboard.nextDouble();
				keyboard.nextLine();	//consumes rest of line
				if (bet > currentCash) {
					System.out.println("You don't have enough money for that bet!");
					checkMoney = true;
				} else {
					currentCash -= bet;
					checkMoney = false;
				}
			} while (checkMoney);
			
			dealStartCards();	//deals 2 cards to the user and the dealer
			
			
			do {	//loop for the actual game
				
				userCards();
				System.out.println();
				dealerShowing();
				blackjackBust();
					
				
				
	
			}while (playing);
			
			//loop used to ask the user if they want to keep playing.
			do {
				System.out.println();
				System.out.println("Would you like to continue? Yes/No");
				String input = keyboard.nextLine();
			
				String verified = verify(input);
			
				if (verified == "yes") {
					keepPlaying = true;
					cont = false;
				} else if (verified == "no") {
					keepPlaying = false;
					cont = false;
				} else if (verified == "retry") {
					cont = true;
				} else {
					System.out.println("Something Broke");
					cont = false;
					keepPlaying = false;
				}
		
			}while (cont);
		}
		
		System.out.println("Thank you for playing!");
		
	}
	
	//prints out starting instructions
	public static void intro() {
		System.out.println("Welcome to Blackjack!");
		System.out.println("---------------------");
		System.out.println("The aim of the game is to get your cards as close to or equal 21 without going over.");
		System.out.println("If it's a tie between you and the dealer, the dealer wins.");
		System.out.println();
	}
	
	//method used to verify if the user wants to keep playing
	public static String verify(String answer){
    	
    	String lower = answer.toLowerCase();
    	char letter = lower.charAt(0);
    	int lowerLength = lower.length();
    	
    	//ends the game if the user has no more money but tries to continue
    	if (currentCash <= 0 && !(lower.equals("no") || (letter == 'n' && lowerLength == 1))) {
			System.out.println("I'm sorry, you have no more money to continue.");
			keepPlaying = false;
			return "no";
		} else if (lower.equals("yes") || (letter == 'y' && lowerLength == 1)) {
    		return "yes";
    	} else if (lower.equals("no") || (letter == 'n' && lowerLength == 1)) {
    		return "no";
    	} else {
    		System.out.println("Sorry, didn't quite understand.");
    		return "retry";
    	}
    	
    }
	
	//deals four starting cards, two to user and two to dealer
	public static void dealStartCards() {
		
		int value;
		int total = userTotal();
		boolean aceTest1 = false; //used to keep looping until user enters a correct Ace value
		boolean aceTest2 = false; //used to keep looping until user enters a correct Ace value
		
		//picks user's first card
		cards.setCards(currentDeckFace.size());
		int user1 = cards.getPick();
		if (currentDeckValue.get(user1) == 0) {
			System.out.println("Your cards: " + userFace + " which totals " + total + ".");
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:"); 
			do { 	//tests if user entered 1 or 11
				
				int input = keyboard.nextInt();	
				keyboard.nextLine(); //consumes rest of line
				
				if (input == 1 ) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest1 = false;
				} else if (input == 11) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest1 = false;
				} else {
					System.out.println("Please enter a correct value!");
				}
				
			} while (aceTest1);
		} else {
			userFace.add(currentDeckFace.get(user1));
			userValue.add(currentDeckValue.get(user1));
			currentDeckFace.remove(user1);
			currentDeckValue.remove(user1);
		}
		
		//sets dealer's first card
		cards.setCards(currentDeckFace.size());
		int dealer1 = cards.getPick();
		if (currentDeckValue.get(dealer1) == 0) {
			int number = random.nextInt(2)+1;
			if (number == 1) {
				value = 1;
			} else {
				value = 11;
			}
			dealerFace.add(currentDeckFace.get(dealer1));
			dealerValue.add(value);
			currentDeckFace.remove(dealer1);
			currentDeckValue.remove(dealer1);
		} else {
			dealerFace.add(currentDeckFace.get(dealer1));
			dealerValue.add(currentDeckValue.get(dealer1));
			currentDeckFace.remove(dealer1);
			currentDeckValue.remove(dealer1);
		}
		
		
		//sets user's second card
		cards.setCards(currentDeckFace.size());
		int user2 = cards.getPick();
		if (currentDeckValue.get(user2) == 0) {
			System.out.println("Your cards: " + userFace + " which totals " + total + ".");
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:"); 
			do {	//tests if user entered 1 or 11
				
				int input = keyboard.nextInt();	
				keyboard.nextLine();	//consumes rest of line
				
				if (input == 1 ) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest2 = false;
				} else if (input == 11) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest2 = false;
				} else {
					System.out.println("Please enter a correct value!");
				}
			} while (aceTest2);
			
		} else {
			userFace.add(currentDeckFace.get(user2));
			userValue.add(currentDeckValue.get(user2));
			currentDeckFace.remove(user2);
			currentDeckValue.remove(user2);
		}
		
		//sets dealer's second card
		cards.setCards(currentDeckFace.size());
		int dealer2 = cards.getPick();
		if (currentDeckValue.get(dealer2) == 0) {
			int number = random.nextInt(2)+1;
			if (number == 1) {
				value = 1;
			} else {
				value = 11;
			}
			dealerFace.add(currentDeckFace.get(dealer2));
			dealerValue.add(value);
			currentDeckFace.remove(dealer2);
			currentDeckValue.remove(dealer2);
		} else {
			dealerFace.add(currentDeckFace.get(dealer2));
			dealerValue.add(currentDeckValue.get(dealer2));
			currentDeckFace.remove(dealer2);
			currentDeckValue.remove(dealer2);
		}
	
	}
	
	public static void initializeCards() { //clears all arrays and creates new ones, essentially creating a new deck of cards
		currentDeckFace.clear();
		currentDeckValue.clear();
		userFace.clear();
		userValue.clear();
		dealerFace.clear();
		dealerValue.clear();
		
		for (int i = 0; i < deckFace.length; i++) {
			currentDeckFace.add(deckFace[i]);
		}
		
		for (int k = 0; k < deckValue.length; k++) {
			currentDeckValue.add(deckValue[k]);
		}
	}
	
	public static int userTotal() { //totals the value of the user's cards
		int total = 0;
		for (int i = 0; i < userValue.size(); i++) {
			total = total + userValue.get(i);
		}
		
		return total;
	}
	
	public static int dealerTotal() { //totals the value of the dealer's cards
		int total = 0;
		for (int i = 0; i < dealerValue.size(); i++) {
			total = total + dealerValue.get(i);
		}
		
		return total;
	}
	
	public static int dealerShowingTotal() { //totals the the dealer's up cards
		int showing = 0;
		for (int i = 1; i < dealerValue.size(); i++) {
			showing = showing + dealerValue.get(i);
		}
		
		return showing;
	}
	
	public static void dealUserCard() { //deals a card to the user
		
		int total = userTotal();
		boolean aceTest = true;
		
		cards.setCards(currentDeckFace.size());
		int user1 = cards.getPick();
		if (currentDeckValue.get(user1) == 0) {
			System.out.println("Your cards: " + userFace + " which totals " + total + ".");
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:"); 
			do {	//tests if user entered 1 or 11
				
				int input = keyboard.nextInt();	
				keyboard.nextLine(); //consumes rest of line
				
				if (input == 1 ) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest = false;
				} else if (input == 11) {
					userFace.add(currentDeckFace.get(user1));
					userValue.add(input);
					currentDeckFace.remove(user1);
					currentDeckValue.remove(user1);
					aceTest = false;
				} else {
					System.out.println("Please enter a correct value!");
				}
				
			} while (aceTest);
		} else {
			userFace.add(currentDeckFace.get(user1));
			userValue.add(currentDeckValue.get(user1));
			currentDeckFace.remove(user1);
			currentDeckValue.remove(user1);
		}
		
		charlie();
		
	}
	
	public static void dealDealerCard() { //deals a card to the dealer
		int dealerTotal = dealerTotal();
		int value;
		
		if (dealerTotal < 17) {
		cards.setCards(currentDeckFace.size());
		int dealer1 = cards.getPick();
		if (currentDeckValue.get(dealer1) == 0) {
				int number = random.nextInt(2)+1;
			if (number == 1) {
					value = 1;
			} else {
				value = 11;
			}
				dealerFace.add(currentDeckFace.get(dealer1));
				dealerValue.add(value);
				currentDeckFace.remove(dealer1);
				currentDeckValue.remove(dealer1);
			} else {
				dealerFace.add(currentDeckFace.get(dealer1));
				dealerValue.add(currentDeckValue.get(dealer1));
				currentDeckFace.remove(dealer1);
				currentDeckValue.remove(dealer1);
			}
		}
	}
	
	public static void standHit() { //asks the user if they want to stand or hit and then proceed accordingly
		
		if (playing = true) {
			System.out.println();
			System.out.println("Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:");
			int input = keyboard.nextInt();
			keyboard.nextLine();
		
			if (input == 1) {
				stand();
			} else if (input == 2){
				dealUserCard();
				dealDealerCard();
			} else {
				System.out.println("Broken");
			}
		}
	}
	
	public static void charlie() { //tests for five card charlie - if the user has five cards and doesn't bust
		if (userFace.size() == 5 && userTotal() < 21) {
			playing = false;
			System.out.println("Five Card Charlie! Your cards were " + userFace + " and totaled " + userTotal() + ".");
		}
	}
	
	public static void userCards() { //outputs the user's cards
		int total = userTotal();
		
		System.out.println("Your cards are: ");
		for (int i = 0; i < userFace.size(); i++) {
			System.out.println(userFace.get(i));
		}
		
		System.out.println("That totals " + total + ".");
	}
	
	public static void dealerShowing() { //outputs the dealer's up cards
		int showing = dealerShowingTotal();
		
		System.out.println("The dealer has: ");
		
		for (int i = 1; i < dealerFace.size(); i++) {
			System.out.println(dealerFace.get(i));
		}
		
		System.out.println("The dealer has " + showing + " showing.");
	}
	
	public static void stand() { //ends the round for the user
		playing  = false;
		dealDealerCard();
		bust();
	}
	
	public static void checkWinner(String result) { //checks the winner of the game depending on different situations and also outputs wins/losses
		
		int user = userTotal();
		int dealer = dealerTotal();
		
		if (result == "finished") {
			if (user > dealer) {
				double winnings = bet * 2;
				currentCash += winnings;
				totalWinnings += winnings;
				System.out.println();
				System.out.println("Your cards " + userFace + " which total " + user);
				System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
				System.out.println("YOU WON! Your winnings are $" + winnings + " which brings your current cash to $" + currentCash + ".");
			} else if (user < dealer) {
				totalWinnings -= bet;
				System.out.println();
				System.out.println("Your cards " + userFace + " which total " + user);
				System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
				System.out.println("YOU LOSE! Your losses are $" + bet + " which brings your current cash to $" + currentCash + ".");
			} else if (user == dealer) {
				totalWinnings -= bet;
				System.out.println();
				System.out.println("Your cards " + userFace + " which total " + user);
				System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
				System.out.println("IT'S A TIE, YOU LOSE! Your losses are $" + bet + " which brings your current cash to $" + currentCash + ".");
			}
		} else if (result == "userBlackjack") {
			double winnings = bet + (bet * 1.5);
			currentCash += winnings;
			totalWinnings += winnings;
			System.out.println();
			System.out.println("Your cards " + userFace + " which total " + user);
			System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
			System.out.println("YOU WON! Your winnings are $" + winnings + " which brings your current cash to $" + currentCash + ".");
		} else if (result == "dealerBlackjack") {
			double losses = (bet * 1.5);
			currentCash -= losses;
			totalWinnings -= losses;
			System.out.println();
			System.out.println("Your cards " + userFace + " which total " + user);
			System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
			System.out.println("YOU LOSE! Your losses are $" + losses + " which brings your current cash to $" + currentCash + ".");
		} else if (result == "userBusted") {
			totalWinnings -= bet;
			System.out.println();
			System.out.println("Your cards " + userFace + " which total " + user);
			System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
			System.out.println("YOU LOSE! Your losses are $" + bet + " which brings your current cash to $" + currentCash + ".");	
		} else if (result == "dealerBusted") {
			double winnings = bet * 2;
			currentCash += winnings;
			totalWinnings += winnings;
			System.out.println();
			System.out.println("Your cards " + userFace + " which total " + user);
			System.out.println("The dealer's cards " + dealerFace + " which total " + dealer);
			System.out.println("YOU WON! Your winnings are $" + winnings + " which brings your current cash to $" + currentCash + ".");
		} else {
			System.out.println("Broken");
		}
	}
	
	public static void blackjackBust() { //checks if the user or dealer have blackjack or busted, if not the user can stand or hit
		int userTotal = userTotal();
		int dealerTotal = dealerTotal();
		
		if (userFace.size() == 2 && userTotal == 21) {
			System.out.println();
			System.out.println("BLACKJACK!");
			playing = false;
			checkWinner("userBlackjack");
		} else if (dealerFace.size() == 2 && dealerTotal == 21){
			System.out.println();
			System.out.println("The dealer got Blackjack!");
			playing = false;
			checkWinner("dealerBlackjack");
		} else if (userTotal > 21) {
			System.out.println();
			System.out.println("BUSTED!");
			playing = false;
			checkWinner("userBusted");
		} else if (dealerTotal > 21) {
			System.out.println();
			System.out.println("The dealer busted!");
			playing = false;
			checkWinner("dealerBusted");
		} else {
			standHit();	
		}
		
	}
	
	public static void bust() { //checks if the dealer or user busted - if not, it ends the round
		
		int userTotal = userTotal();
		int dealerTotal = dealerTotal();
		
		if (userTotal > 21) {
			System.out.println();
			System.out.println("BUSTED!");
			playing = false;
			checkWinner("userBusted");
		} else if (dealerTotal > 21) {
			System.out.println();
			System.out.println("The dealer busted!");
			playing = false;
			checkWinner("dealerBusted");
		} else {
			playing = false;
			checkWinner("finished");
		}
	}
}

/*
 Welcome to Blackjack!
---------------------
The aim of the game is to get your cards as close to or equal 21 without going over.
If it's a tie between you and the dealer, the dealer wins.

How much money do you have to play with? Enter the amount:
500
How much money would you like to bet? Enter the amount:
20
Your cards are: 
8 of Hearts
5 of Spades
That totals 13.

The dealer has: 
Jack of Spades
The dealer has 10 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
2
Your cards are: 
8 of Hearts
5 of Spades
7 of Hearts
That totals 20.

The dealer has: 
Jack of Spades
9 of Spades
The dealer has 19 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
1

Your cards [8 of Hearts, 5 of Spades, 7 of Hearts] which total 20
The dealer's cards [2 of Hearts, Jack of Spades, 9 of Spades] which total 21
YOU LOSE! Your losses are $20.0 which brings your current cash to $480.0.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
50
Your cards are: 
8 of Clubs
3 of Hearts
That totals 11.

The dealer has: 
10 of Clubs
The dealer has 10 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
2
Your cards are: 
8 of Clubs
3 of Hearts
10 of Hearts
That totals 21.

The dealer has: 
10 of Clubs
Jack of Diamonds
The dealer has 20 showing.

The dealer busted!

Your cards [8 of Clubs, 3 of Hearts, 10 of Hearts] which total 21
The dealer's cards [5 of Hearts, 10 of Clubs, Jack of Diamonds] which total 25
YOU WON! Your winnings are $100.0 which brings your current cash to $530.0.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
500
Your cards are: 
Jack of Spades
7 of Spades
That totals 17.

The dealer has: 
7 of Clubs
The dealer has 7 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
1

Your cards [Jack of Spades, 7 of Spades] which total 17
The dealer's cards [10 of Clubs, 7 of Clubs] which total 17
IT'S A TIE, YOU LOSE! Your losses are $500.0 which brings your current cash to $30.0.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
15
Your cards are: 
2 of Spades
5 of Hearts
That totals 7.

The dealer has: 
8 of Hearts
The dealer has 8 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
2
Your cards: [2 of Spades, 5 of Hearts] which totals 7.
You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:
11
Your cards are: 
2 of Spades
5 of Hearts
Ace of Clubs
That totals 18.

The dealer has: 
8 of Hearts
8 of Diamonds
The dealer has 16 showing.

The dealer busted!

Your cards [2 of Spades, 5 of Hearts, Ace of Clubs] which total 18
The dealer's cards [7 of Hearts, 8 of Hearts, 8 of Diamonds] which total 23
YOU WON! Your winnings are $30.0 which brings your current cash to $45.0.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
30
Your cards are: 
6 of Hearts
Queen of Hearts
That totals 16.

The dealer has: 
5 of Spades
The dealer has 5 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
1

Your cards [6 of Hearts, Queen of Hearts] which total 16
The dealer's cards [7 of Spades, 5 of Spades, 2 of Diamonds] which total 14
YOU WON! Your winnings are $60.0 which brings your current cash to $75.0.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
25
Your cards: [King of Hearts] which totals 0.
You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:
11
Your cards are: 
King of Hearts
2 of Diamonds
That totals 21.

The dealer has: 
5 of Spades
The dealer has 5 showing.

BLACKJACK!

Your cards [King of Hearts, 2 of Diamonds] which total 21
The dealer's cards [3 of Hearts, 5 of Spades] which total 8
YOU WON! Your winnings are $62.5 which brings your current cash to $112.5.

Would you like to continue? Yes/No
yes
How much money would you like to bet? Enter the amount:
112.5
Your cards: [] which totals 0.
You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:
11
Your cards are: 
Ace of Spades
3 of Diamonds
That totals 14.

The dealer has: 
6 of Spades
The dealer has 6 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
2
Your cards are: 
Ace of Spades
3 of Diamonds
8 of Clubs
That totals 22.

The dealer has: 
6 of Spades
2 of Clubs
The dealer has 8 showing.

BUSTED!

Your cards [Ace of Spades, 3 of Diamonds, 8 of Clubs] which total 22
The dealer's cards [King of Hearts, 6 of Spades, 2 of Clubs] which total 18
YOU LOSE! Your losses are $112.5 which brings your current cash to $0.0.

Would you like to continue? Yes/No
yes
I'm sorry, you have no more money to continue.
Thank you for playing!

-------------------------------------------------------------------------------------------------------

Welcome to Blackjack!
---------------------
The aim of the game is to get your cards as close to or equal 21 without going over.
If it's a tie between you and the dealer, the dealer wins.

How much money do you have to play with? Enter the amount:
1
How much money would you like to bet? Enter the amount:
1
Your cards are: 
9 of Clubs
Jack of Hearts
That totals 19.

The dealer has: 
4 of Hearts
The dealer has 4 showing.

Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:
2
Your cards are: 
9 of Clubs
Jack of Hearts
9 of Diamonds
That totals 28.

The dealer has: 
4 of Hearts
3 of Diamonds
The dealer has 7 showing.

BUSTED!

Your cards [9 of Clubs, Jack of Hearts, 9 of Diamonds] which total 28
The dealer's cards [6 of Spades, 4 of Hearts, 3 of Diamonds] which total 13
YOU LOSE! Your losses are $1.0 which brings your current cash to $0.0.

Would you like to continue? Yes/No
no
Thank you for playing!
 */
