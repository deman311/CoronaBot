package MyBot;

import java.util.Random;

abstract class Poker {
	public String card = null;
	public Random rand = new Random();
	public static boolean hascard=false;
	
	public static String GetCard() {
		
		Random rand = new Random();
		int cardnum=0;
		int cardsign=0;
		
		String card=null; 
		
		do {
			cardnum = rand.nextInt(13);
			cardsign = rand.nextInt(4);
			} 
			while(CoronaBot.Deck[cardsign][cardnum]=="empty");

			card = CoronaBot.Deck[cardsign][cardnum];
			CoronaBot.Deck[cardsign][cardnum] = "empty";
			
		return card;
	}
		
	public static void GetTable(String[] Table) {
		
		Random rand = new Random();
		String[] pokertable = new String[5];
		int cardnum=0;
		int cardsign=0;
		
		for(int i=0;i<5;i++) {			
			
			do {
				cardnum = rand.nextInt(13);
				cardsign = rand.nextInt(4);
				} 
				while(CoronaBot.Deck[cardsign][cardnum]=="empty");
			
			pokertable[i] = CoronaBot.Deck[cardsign][cardnum];
			CoronaBot.Deck[cardsign][cardnum] = "empty";
			Table[i] = Poker.cardNum(pokertable[i]) + Poker.cardSign(pokertable[i]);
		}
	}
	
	public static void NewDeck() {
		
		String newcard=null;
		CoronaBot.deckCount = 52;
		
		for(int j=0;j<4;j++)
			for(int i=0;i<13;i++) {
				if(i>=2 && i<=10) newcard = String.valueOf(i);
				else
					switch (i) {
						case 0: newcard = "Ace"; break;
						case 1: newcard = "Jack"; break;
						case 11: newcard = "Queen"; break;
						case 12: newcard = "King"; break;				
					}
				switch (j) {
					case 0: newcard = (newcard+ "♠"); break;
					case 1: newcard = (newcard+ "♥"); break;
					case 2: newcard = (newcard+ "♦"); break;
					case 3: newcard = (newcard+ "♣"); break;
				}
				CoronaBot.Deck[j][i] = newcard;
			}
	}
	
	public static String cardSign(String card) {
		
		String sign = null;
		
		if(card.contains("♠"))
			sign = " <:spade:700319468496355399>";
		else if(card.contains("♥"))
			sign = " <:heartt:700319468907135046>";
		else if(card.contains("♦"))
			sign = " <:diamond:700319468357812316>";
		else if(card.contains("♣"))
			sign = " <:club:700319468223594650>";
		
		return sign;
	}
	
	public static String cardNum(String card) {
		
		String cardnum = String.valueOf(card.charAt(0));
		
		switch (cardnum){
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

