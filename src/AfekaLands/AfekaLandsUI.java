/*
	 _     __      _         _                    _               
    / \   / _| ___| | ____ _| |    __ _ _ __   __| |___           
   / _ \ | |_ / _ \ |/ / _` | |   / _` | '_ \ / _` / __|          
  / ___ \|  _|  __/   < (_| | |__| (_| | | | | (_| \__ \          
 /_/   \_\_|  \___|_|\_\__,_|_____\__,_|_| |_|\__,_|___/
                                                                  
*/// (c) Dmitry Gribovsky 317088573

package AfekaLands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import AfekaLands.Item.Sort;

public interface AfekaLandsUI {
	public static final Random rand = new Random();
	public static int moves = 0;

	public static String ShopMenu(Shop shop) {
		StringBuilder sb = new StringBuilder();
		ArrayList<Item> inv = shop.getShopINV();

		sb.append("🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨\n");
		sb.append("🍻 " + shop.getShopName() + "! 🍻\n");
		sb.append("\"Welcome! My name is " + shop.getOwnerName() + ",\nHow can I help you today?\"\n\n");
		sb.append("PRICES: ");
		for (Item slot : inv)
			sb.append(slot.getName() + "[" + slot.getPrice() + "] ");
		sb.append("\n");
		sb.append("STOCK: [");
		for (Item slot : inv) {
			if (slot.getSize() > 0) {
				if (slot.item == Sort.HP)
					sb.append(" 🍎(" + slot.size + ") ");
				else if (slot.item == Sort.EP)
					sb.append(" ⚡(" + slot.size + ") ");
				else if (slot.item == Sort.BEER)
					sb.append(" 🍺(" + slot.size + ") ");
				else if (slot.item == Sort.WEAPON) {
					Weapon wep = (Weapon) slot;
					sb.append("\n🗡 " + slot.getName() + "[Atk: " + wep.getDamage() + "]");
				} else if (slot.item == Sort.ARMOR) {
					Armor arm = (Armor) slot;
					sb.append("\n👘 " + arm.getName() + "(" + arm.getKind() + ")[Def: " + arm.getDEF() + "]");
				}
			}
		}
		sb.append("]\n");

		for (Item slot : inv)
//			if(slot.getName().contentEquals("Health Potion"))
//				sb.append("@buyHP");
//			else if(slot.getName().contentEquals("Energy Potion"))
//				sb.append("@buyEP");
//			else
			sb.append("@buy " + slot.getName() + " | ");

		sb.append("\n\n@sellJunk");
		sb.append("\n@exitShop");
		sb.append("\n💰 Coins: " + AfekaLandsController.player1.getCOINS());
		sb.append("\n🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨\n");

		return sb.toString();
	}

	public static String equipMenu() {
		StringBuilder sb = new StringBuilder();
		Armor[] equipment = AfekaLandsController.player1.getArmor();

		sb.append("⭐ EQUIPMENT: ⭐\n\n");
		if (equipment[0] != null)
			sb.append("⛑ Head: " + equipment[0].getName() + " (+" + equipment[0].getDEF() + " Def)");
		else
			sb.append("⛑ Head: ");
		if (equipment[1] != null)
			sb.append("\n🎽 Torso: " + equipment[1].getName() + " (+" + equipment[1].getDEF() + " Def)");
		else
			sb.append("\n🎽 Torso: ");
		if (equipment[2] != null)
			sb.append("\n👖 Leggings: " + equipment[2].getName() + " (+" + equipment[2].getDEF() + " Def)");
		else
			sb.append("\n👖 Leggings: ");
		if (equipment[3] != null)
			sb.append("\n🥾 Footwear: " + equipment[3].getName() + " (+" + equipment[3].getDEF() + " Def)");
		else
			sb.append("\n🥾 Footwear: ");
		if (equipment[4] != null)
			sb.append("\n💍 Ring: " + equipment[4].getName() + " (+" + equipment[4].getDEF() + " Def)");
		else
			sb.append("\n💍 Ring: ");
		if (AfekaLandsController.player1.getWeapon() != null)
			sb.append("\n🗡 Weapon: " + AfekaLandsController.player1.getWeapon().getName() + " (+"
					+ AfekaLandsController.player1.getWeapon().getDamage() + " Atk)");
		else
			sb.append("\n🗡 Weapon: ");

		return sb.toString();
	}

	public static String fightMenu(Enemy enemy1) {
		StringBuilder sb = new StringBuilder();
		int ehp = enemy1.getHP();
		int emhp = enemy1.getMaxHP();
		int HP = AfekaLandsController.player1.getHP();
		int maxHP = AfekaLandsController.player1.getMaxHP();
		int ENG = AfekaLandsController.player1.getENG();
		int maxENG = AfekaLandsController.player1.getMaxENG();

		if (ehp > 0) {
			sb.append("🔻🔻🔻🔻🔻🔻🔻🔻🔻🔻\n");
			sb.append("⚔ FIGHT ⚔\n");
		} else {
			sb.append("◻◻◻◻◻◻◻◻◻◻\n");
			sb.append("💀 DEAD 💀\n\n");
		}
		sb.append("Enemy: " + enemy1.getName() + "\n");
		sb.append("Level: " + enemy1.getLevel() + "\n");
		sb.append("[");
		for (int i = 0; i <= 10; i++) {
			if (precentOf(ehp, emhp) > i * 10 && ehp > 0)
				sb.append("🧡");
			else if (ehp == 0)
				sb.append("#");
		}
		sb.append("] " + "(" + ehp + "/" + emhp + ")\n");
		if (ehp != 0)
			sb.append("🔺🔺🔺🔺🔺🔺🔺🔺🔺🔺");
		else
			sb.append("◻◻◻◻◻◻◻◻◻◻\n");

		sb.append("\n");
		if (AfekaLandsController.player1.getHP() > 0) {
			sb.append("〰〰〰〰〰〰〰〰〰〰\n");
			sb.append("Health: [");
			for (int i = 0; i < 10; i++)
				if (precentOf(HP, maxHP) >= i * 10) {
					if (precentOf(HP, maxHP) < (i + 1) * 10 && i <= 10)
						sb.append("💕");
					else
						sb.append("❤");
				}
			sb.append(
					"] " + AfekaLandsController.player1.getHP() + "/" + AfekaLandsController.player1.getMaxHP() + "\n");
			sb.append("Energy: [");
			for (int i = 0; i < 10; i++)
				if (precentOf(ENG, maxENG) >= i * 10 && precentOf(ENG, maxENG) != 0)
					sb.append("💙");
			sb.append("] " + AfekaLandsController.player1.getENG() + "/" + AfekaLandsController.player1.getMaxENG()
					+ "\n");
			sb.append("〰〰〰〰〰〰〰〰〰〰\n");
			sb.append("💭 @sword @fireball ");
			for (Skill skill : AfekaLandsController.player1.getSkills())
				sb.append("💭 @" + skill.getName() + " ");
			sb.append("\n");
			if (AfekaLandsController.player1.checkInv(Sort.HP))
				sb.append("@useHP🍎(" + AfekaLandsController.player1.giveItemSize(Sort.HP) + ") ");
			if (AfekaLandsController.player1.checkInv(Sort.EP))
				sb.append("@useEP⚡(" + AfekaLandsController.player1.giveItemSize(Sort.EP) + ") ");
			sb.append("💭\n");
		} else {
			sb.append("〰〰〰〰〰〰〰〰〰〰\n");
			sb.append("☠GAME OVER☠\n");
			sb.append("〰〰〰〰〰〰〰〰〰〰");
		}

		return sb.toString();
	}

	public static String mapMenu() {
		StringBuilder sb = new StringBuilder();
		sb.append("🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹\n");
		sb.append("🗺 MAP 🗺\n");
		sb.append("\n");
		sb.append(MapGen2.localMap() + "\n");
		sb.append("\n");
		sb.append("🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹🔹");

		return sb.toString();
	}

	public static String[] getStatus() {
		File file = new File("./Files/AfekaLands/Character.txt");
		String[] Char = null;
		String line = null;
		StringBuilder text = new StringBuilder();
		Scanner read;

		try {
			// BufferedReader reader = new BufferedReader(new InputStreamReader(new
			// FileInputStream(file),"UTF8"));
			read = new Scanner(file);
			while (read.hasNext()) {
				line = read.nextLine().replace("NAME: ", "").replace("HP: ", "").replace("ENG: ", "")
						.replace("COINS: ", "").replace("LEVEL: ", "").replace("EXP: ", "").replace("INV: ", "");
				text.append(line + "\n");
			}
			read.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Character.txt not found!");
			e1.printStackTrace();
		}
		Char = text.toString().split("\n");

		return Char;
	}

	public static void updateStatus(int hp, int eng) {
		AfekaLandsController.player1.setHP(hp);
		AfekaLandsController.player1.setENG(eng);
		try {
			AfekaLandsController.player1.updateStatus("x", 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String buryChar() {
		File file = new File("./Files/AfekaLands/Character.txt");
		String line = null;
		StringBuilder oldtext = new StringBuilder();
		String[] Char = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			while ((line = reader.readLine()) != null) {
				oldtext.append(line + "\n");
			}
			reader.close();
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Unsupported Encoding in Character.txt");
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			System.out.println("Character.txt not found!");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOE on Character.txt");
			e.printStackTrace();
		}

		Char = oldtext.toString().split("\n");

		if (Char[0] == null || Char[0].contentEquals(""))
			return "Last hero is already burried.";
		if (!(AfekaLandsController.player1.getHP() == 0))
			return "Cannot bury a live hero!";

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			PrintWriter deleteWepArchinve = new PrintWriter(
					new FileOutputStream("./Files/AfekaLands/cWeapons.txt", false));
			deleteWepArchinve.flush();
			deleteWepArchinve.close();
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Unsupported Encoding in Character.txt");
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			System.out.println("Character.txt not found!");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOE on Character.txt");
			e.printStackTrace();
		}

		AfekaLandsController.setHasPlayer(false);
		return AfekaLandsController.player1.getName() + " is now burried.\nRIP 🀄";
	}

	public static int precentOf(int num, int from) {
		int c = num * 100 / from;
		return c;
	}

	public static int precent(int num, int from) {
		int c = from * num / 100;
		return c;
	}

}
