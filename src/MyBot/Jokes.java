package MyBot;

import java.util.Random;

abstract class Jokes {

	public static String getJoke() {
	String joke=null;
	Random rand = new Random();
	int num1 = rand.nextInt(14)+1;		
			switch (num1) {
				case 1:
					joke = "כשערס מציע נישואים לערסית, הם מתערסים?";
					break;
				case 2:
					joke="שם אידיאלי לעובד בצ'יינג'? תמיר.";
					break;
				case 3:
					joke="למה עצים לא עולים אף פעם לתיכון? הם לא שורדים את החטיבה.";
					break;
				case 4:
					joke="קניתי לחמותי מזרן חדש, מקווה שהיא תמות עליו.";
					break;
				case 5:
					joke="מחיר של סנדלי שורש: 400 - מביא למוכר שטר של 20.";
					break;
				case 6:
					joke="החיים שלך הם בדיחה.";
					break;
				case 7:
					joke="What do you call a boomerang that doesn't work?\na stick.";
					break;
				case 8:
					joke="What do you call friends who love math?\nAlgebro's.";
					break;
				case 9:
					joke="Why did the two 4’s skip lunch?\nThey already 8!";
					break;
				case 10:
					joke="MATH - Mental Abuse To Humans.";
					break;
				case 11:
					joke="I put my root beer in a square glass.\nNow it’s just beer.";
					break;
				case 12:
					joke="You’ll never be as lazy as whoever named the fireplace.";
					break;
				case 13:
					joke="There’s no “I” in denial.";
					break;
				case 14:
					joke="I was gonna tell a time-traveling joke, but you guys didn’t like it.";
					break;
			}
			
			return joke;
		}
	}