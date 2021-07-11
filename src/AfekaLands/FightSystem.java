package AfekaLands;

import java.io.FileNotFoundException;
import java.util.Random;

public interface FightSystem {
	public static Random rand = new Random();

	public static String swordMove(Character p1, Enemy e1) {
		int pD = p1.getDMG(), eD = rand.nextInt(e1.getDamage() / 2) + e1.getDamage() / 2;
		pD = pD / 2 + rand.nextInt(pD / 2);
		StringBuilder message = new StringBuilder();

		message.append("💢 " + e1.dealDamage(pD));
		try {
			int[] crit = AfekaLandsController.player1.getWeapon().getCrit();
			int c = rand.nextInt(100);
			if (c < crit[1]) {
				pD += crit[0];
				message.append("\n❗ CRIT (+" + crit[0] + ")");
			}
		} catch (NullPointerException e) {
		}
		if (e1.getHP() > 0)
			try {
				int DMG = eD - p1.getDEF();
				message.append("\n");
				if (DMG <= 0)
					message.append("💔 " + e1.getName() + " missed you while attacking back.");
				else {
					p1.updateStatus("HP", -DMG);
					message.append("💔 " + AfekaLandsController.enemy1.getName() + " hit you back with "
							+ (eD - p1.getDEF()) + " Damage.");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return message.toString();
	}

	public static String skillMove(Character p1, Enemy e1, Skill skill) {
		int pD = skill.getDamage(), eD = rand.nextInt(e1.getDamage() / 2) + e1.getDamage() / 2;
		pD = pD / 2 + rand.nextInt(pD / 2);
		StringBuilder message = new StringBuilder();
		message.append(skill.getSign() + " " + e1.dealDamage(pD));

		if (e1.getHP() > 0 && rand.nextInt(3) == 0) {
			try {
				int DMG = eD - p1.getDEF();
				message.append("\n");
				if (DMG <= 0)
					message.append("💔 " + e1.getName() + " missed you while attacking back.");
				else {
					p1.updateStatus("HP", -DMG);
					message.append("💔 " + AfekaLandsController.enemy1.getName() + " hit you back with "
							+ (eD - p1.getDEF()) + " Damage.");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message.toString();
	}
}
