import java.util.*;

public class Cards {
	
	Random random = new Random();
	private int cards;
	private int pick;
	
	public void setCards(int numCards) {
		cards = numCards;
		pickCard();
	}
	
	public void pickCard() {
		pick = random.nextInt(cards);
	}
	
	public int getPick() {
		return pick;
	}

}

//comments
