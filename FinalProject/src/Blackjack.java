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
	
	static Cards cards = new Cards();
	
	static boolean keepPlaying = true; //used for while loop to keep playing
	static boolean playing = true; //used for do while loop for each time a card is added to user's hand
	
	public static void main(String[] args) {
		
		boolean cont = false; //used for do while loop to ask if the user wants to keep playing
		final double startingCash;
		double currentCash;
		double bets = 0;
		int total;
		
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
		
		while (keepPlaying) {
			initializeCards();	//clears old deck to create a new one
			dealStartCards();	//deals 2 cards to the user and the dealer
			
			System.out.println("How much money would you like to bet? Enter the amount:");
			double bet = keyboard.nextDouble();
			keyboard.nextLine();		//TODO check user's money
			currentCash -= bet;
			bets += bet;
			
			do {
				total = userTotal();
				System.out.println("Your cards are " + userFace + " which totals " + total + ".");
				
				System.out.println("Do you want to Stand or Hit? Enter 1 for stand or 2 for Hit:");
				int input = keyboard.nextInt();
				keyboard.nextLine();
				
				standHit(input);
				
				
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
    	
    		
    	if (lower.equals("yes") || (letter == 'y' && lowerLength == 1)) {
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
		
		//picks user's first card
		cards.setCards(currentDeckFace.size());
		int user1 = cards.getPick();
		if (currentDeckValue.get(user1) == 0) {
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:"); 
			int input = keyboard.nextInt();	//todo check if 1 or 11
			keyboard.nextLine();
			userFace.add(currentDeckFace.get(user1));
			userValue.add(input);
			currentDeckFace.remove(user1);
			currentDeckValue.remove(user1);
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
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:");
			int input = keyboard.nextInt(); //todo check if 1 or 11
			keyboard.nextLine();
			userFace.add(currentDeckFace.get(user2));
			userValue.add(input);
			currentDeckFace.remove(user2);
			currentDeckValue.remove(user2);
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
	
	public static void initializeCards() {
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
	
	public static int userTotal() {
		int total = 0;
		for (int i = 0; i < userValue.size(); i++) {
			total = total + userValue.get(i);
		}
		
		return total;
	}
	
	public static void dealUserCard() {
		cards.setCards(currentDeckFace.size());
		int user1 = cards.getPick();
		if (currentDeckValue.get(user1) == 0) {
			System.out.println("You have an ace! Would you like to use it as a 1 or an 11? Enter 1 or 11:");
			int input = keyboard.nextInt(); //todo check if 1 or 11
			keyboard.nextLine();
			userFace.add(currentDeckFace.get(user1));
			userValue.add(input);
			currentDeckFace.remove(user1);
			currentDeckValue.remove(user1);
		} else {
			userFace.add(currentDeckFace.get(user1));
			userValue.add(currentDeckValue.get(user1));
			currentDeckFace.remove(user1);
			currentDeckValue.remove(user1);
		}
	}
	
	public static void dealDealerCard() {
		int value;
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
	
	public static void standHit(int value) {
		if (value == 1) {
			playing  = false;
		} else {
			dealUserCard();
		}
	}
}

/* TODO
 * check players bet to their money
 * check if user entered 1 or 11 on ace
 * check who won
 * 
 */
