package MyBot;

import java.util.ArrayList;
import java.util.Random;

abstract class Poker {
	public static Random rand = new Random();

	private static int deckCount = 0;

	public static ArrayList<String> deck = new ArrayList<String>(); // a arraylist of 52 cards represented by coded
																	// string

	public static String getCard() {
		deckCount--;
		String card = deck.remove(0);
		return cardNum(card) + cardSign(card);
	}

	public static String getTable() {
		String table = "";
		for (int i = 0; i < 5; i++) {
			table += getCard();
			if (i < 4)
				table += " | ";
		}
		return table;
	}

	public static int getDeckCount() {
		return deckCount;
	}

	public static void NewDeck() {

		deck.clear(); // first of all we need to clear the deck
		String newcard = null;

		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 13; i++) {
				if (i >= 2 && i <= 10)
					newcard = "" + i;
				else
					switch (i) {
					case 0:
						newcard = "Ace";
						break;
					case 1:
						newcard = "Jack";
						break;
					case 11:
						newcard = "Queen";
						break;
					case 12:
						newcard = "King";
						break;
					}
				switch (j) {
				case 0:
					newcard = (newcard + "♠");
					break;
				case 1:
					newcard = (newcard + "♥");
					break;
				case 2:
					newcard = (newcard + "♦");
					break;
				case 3:
					newcard = (newcard + "♣");
					break;
				}

				deck.add(newcard);
			}

		deckCount = 52;
		Shuffle();
	}

	public static void Shuffle() {
		// The process of taking out a variable from a random position and putting it
		// back at the end of it shuffles the deck.
		for (int i = 0; i < deckCount * 3; i++) {
			String c1 = deck.get(rand.nextInt(deckCount));
			deck.remove(c1);
			deck.add(c1);
		}
	}

	public static String cardSign(String card) {

		String sign = null;

		if (card.contains("♠"))
			sign = " <:spade:700319468496355399>";
		else if (card.contains("♥"))
			sign = " <:heartt:700319468907135046>";
		else if (card.contains("♦"))
			sign = " <:diamond:700319468357812316>";
		else if (card.contains("♣"))
			sign = " <:club:700319468223594650>";

		return sign;
	}

	public static String cardNum(String card) {

		String cardnum = String.valueOf(card.charAt(0));

		switch (cardnum) {
		case "A":
			cardnum = "Ace";
			break;
		case "J":
			cardnum = "Jack";
			break;
		case "Q":
			cardnum = "Queen";
			break;
		case "K":
			cardnum = "King";
			break;
		case "1":
			cardnum = "10";
			break;
		}

		return cardnum;
	}
}
