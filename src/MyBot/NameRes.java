package MyBot;

import java.util.Random;

abstract class NameRes {

	public static String main(String args) {
		
		Random rand = new Random();
		String line=null;
		int rand2 = rand.nextInt(4);
			
		switch (args) {
			
			case "Timor":
				switch (rand2) {
					case 0: 
						line = "כן טימור?";
						break;							
					case 1:
						line = "לקבלת הטימור המסדר יימתח להקשב, הקשב!";
						break;							
					case 2:
						line = "אפשר עוד רגע? אני עסוק בלקמפל.";
						break;
					case 3:
						line = "מה שלומך יא גבר על?";
						break;
				}
				break;
				
			case "Alon Ronder":
					switch (rand2) {
						case 0: 
							line = "אלון?";
							break;							
						case 1:
							line = "להזמין לך בוגאטי עליי?";
							break;							
						case 2:
							line = "מוכן לפקודות אהובי! ♥";
							break;				
						case 3:
							line = "מה שלומך יא גבר על?";
							break;	
					}
					break;
					
			case "Dima Grib":
				switch (rand2) {
					case 0: 
						line = "אבא?";
						break;							
					case 1:
						line = "אני כבר נחשב ילד אמיתי?";
						break;							
					case 2:
						line = "אלוהים?";
						break;			
					case 3:
						line = "אתה לא קיים.";
						break;
				}
				break;
				
			default:
				line = "מישהו קרא לי?";
				break;
		}
		
		return line;
	}

}
