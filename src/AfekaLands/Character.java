package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import AfekaLands.Item.Sort;

public class Character {
	public static Random rand = new Random();
	private int level = 1;
	private int levelConst;
	private String name;
	private int HP, ENG, maxHP, maxENG, EXP, maxEXP;
	private int baseDMG, baseARM;
	private String CREATOR;
	private ArrayList<Item> inv = new ArrayList<Item>();
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	private Weapon equipATK = null;
	private Armor[] equipDEF = new Armor[5];

	public Character(String name, String CREATOR) {
		this.name = name;
		this.CREATOR = CREATOR;

		if (!AfekaLandsController.hasPlayer()) {
			level = 1;
			levelConst = level * 200;
			EXP = 0;
			maxEXP = 50 + levelConst * levelConst / 10000;
			maxHP = 50 + (level * level - level) * 10;
			maxENG = 20 + 5 * (level - 1);
			HP = 50;
			ENG = 20;
			baseDMG = 30 * level;
			baseARM = 1 * level;
			updateStatus();
		}
	}

	public void addSkill(Skill skill) {
		skills.add(skill);
	}

	public ArrayList<Skill> getSkills() {
		return skills;
	}

	public String wear(Armor armor) {
		Armor lastArmor = null;
		switch (armor.getKind().name()) {
		case "HEAD":
			if (equipDEF[0] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[0] = armor;
			takeOut(armor);
			break;
		case "TORSO":
			if (equipDEF[1] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[1] = armor;
			takeOut(armor);
			break;
		case "LEG":
			if (equipDEF[2] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[2] = armor;
			takeOut(armor);
			break;
		case "BOOT":
			if (equipDEF[3] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[3] = armor;
			takeOut(armor);
			break;
		case "RING":
			if (equipDEF[4] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[4] = armor;
			takeOut(armor);
			break;
		}
		baseARM += armor.getDEF();
		updateStatus();
		if (lastArmor != null)
			return "Successfully worn: " + armor.getName() + " instead of " + lastArmor.getName() + "! (Def change: "
					+ (armor.getDEF() - lastArmor.getDEF()) + ")";
		return "Successfully worn: " + armor.getName() + "! (Def +" + armor.getDEF() + ")";
	}

	public String wear(Weapon weapon) {
		Weapon lastWEP = null;
		if (equipATK != null) {
			lastWEP = AfekaLandsController.player1.equipATK;
			takeoff(AfekaLandsController.player1.equipATK);
		}
		System.out.println(weapon.getName());
		equipATK = weapon;
		baseDMG += weapon.getDamage();
		System.out.println(takeOut(weapon));
		updateStatus();
		if (lastWEP != null)
			return "Successfully worn: " + weapon.getName() + " instead of " + lastWEP.getName() + "! (Atk change: "
					+ (weapon.getDamage() - lastWEP.getDamage()) + ")";
		return "Successfully worn: " + weapon.getName() + "! (Atk +" + weapon.getDamage() + ")";
	}

	public String wear(String item) {
		Weapon weapon = null, lastWEP = null;
		Armor armor = null, lastArmor = null;
		for (Weapon wep : Weapon.allWeapons)
			if (wep.getName().equalsIgnoreCase(item)) {
				weapon = wep;
				break;
			}
		if (weapon == null)
			for (Armor arm : Armor.allArmors)
				if (arm.getName().equalsIgnoreCase(item)) {
					armor = arm;
					break;
				}

		if (weapon == null && armor == null)
			return "Could not find an item with such name.";

		if (weapon != null) {
			if (equipATK != null) {
				lastWEP = AfekaLandsController.player1.equipATK;
				AfekaLandsController.player1.takeoff(AfekaLandsController.player1.equipATK);
			}
			System.out.println(weapon.getName());
			equipATK = weapon;
			baseDMG += weapon.getDamage();
			System.out.println(takeOut(weapon));
			updateStatus();
			if (lastWEP != null)
				return "Successfully worn: " + weapon.getName() + " instead of " + lastWEP.getName() + "! (Atk change: "
						+ (weapon.getDamage() - lastWEP.getDamage()) + ")";
			return "Successfully worn: " + weapon.getName() + "! (Atk +" + weapon.getDamage() + ")";
		}

		switch (armor.getKind().name()) {
		case "HEAD":
			if (equipDEF[0] != null) {
				lastArmor = equipDEF[0];
				takeoff(equipDEF[0]);
			}
			equipDEF[0] = armor;
			System.out.println(takeOut(armor));
			break;
		case "TORSO":
			if (equipDEF[1] != null) {
				lastArmor = equipDEF[1];
				takeoff(equipDEF[1]);
			}
			equipDEF[1] = armor;
			System.out.println(takeOut(armor));
			break;
		case "LEG":
			if (equipDEF[2] != null) {
				lastArmor = equipDEF[2];
				takeoff(equipDEF[2]);
			}
			equipDEF[2] = armor;
			System.out.println(takeOut(armor));
			break;
		case "BOOT":
			if (equipDEF[3] != null) {
				lastArmor = equipDEF[3];
				takeoff(equipDEF[3]);
			}
			equipDEF[3] = armor;
			System.out.println(takeOut(armor));
			break;
		case "RING":
			if (equipDEF[4] != null) {
				lastArmor = equipDEF[4];
				takeoff(equipDEF[4]);
			}
			equipDEF[4] = armor;
			System.out.println(takeOut(armor));
			break;
		}
		baseARM += armor.getDEF();
		updateStatus();
		if (lastArmor != null)
			return "Successfully worn: " + armor.getName() + " instead of " + lastArmor.getName() + "! (Def change: "
					+ (armor.getDEF() - lastArmor.getDEF()) + ")";
		return "Successfully worn: " + armor.getName() + "! (Def +" + armor.getDEF() + ")";
	}

	public String takeoff(Armor armor) {
		switch (armor.getKind().name()) {
		case "HEAD":
			if (!equipDEF[0].description.contains(armor.description))
				return "You don't have that equipped.";
			equipDEF[0] = null;
			break;
		case "TORSO":
			if (!equipDEF[1].description.contains(armor.description))
				return "You don't have that equipped.";
			equipDEF[1] = null;
			break;
		case "LEG":
			if (!equipDEF[2].description.contains(armor.description))
				return "You don't have that equipped.";
			equipDEF[2] = null;
			break;
		case "BOOT":
			if (!equipDEF[3].description.contains(armor.description))
				return "You don't have that equipped.";
			equipDEF[3] = null;
			break;
		case "RING":
			if (!equipDEF[4].description.contains(armor.description))
				return "You don't have that equipped.";
			equipDEF[4] = null;
			break;
		}

		baseARM -= armor.getDEF();
		putIn(armor);
		updateStatus();
		return "Successfully taken off " + armor.getName();
	}

	public String takeoff(Weapon weapon) {
		if (!equipATK.description.contains(weapon.description))
			return "You don't have that equipped.";

		equipATK = null;
		baseDMG -= weapon.getDamage();
		putIn(weapon);
		updateStatus();
		return "Successfully taken off " + weapon.getName();
	}

	public void addDamage(int damage) {
		baseDMG += damage;
	}

	public int addEXP(int exp) {
		int counter = 0;
		EXP += exp;
		while (EXP >= maxEXP) {
			EXP -= maxEXP;
			counter++;
			level++;
			levelConst = level * 200;
			maxEXP = 50 + levelConst * levelConst / 10000;
			maxHP = 50 + (level * level - level) * 10;
			maxENG = 20 + 5 * (level - 1);
			HP = maxHP;
			ENG = maxENG;
			baseDMG += 30;
			baseARM += 1;
		}
		updateStatus();
		if (counter > 0)
			return level;

		return 0;
	}

	public void updateStatus(String status, int update) throws FileNotFoundException {
		File file = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Character.txt");
		PrintWriter writeChar = new PrintWriter(file);
		if (status.contains("HP"))
			setHP(update);
		if (status.contains("ENG"))
			setENG(update);

		writeChar.println("NAME: " + name);
		writeChar.println("CREATOR: " + CREATOR);
		writeChar.println("LEVEL: " + level);
		writeChar.println("HP: " + HP);
		writeChar.println("ENG: " + ENG);
		writeChar.println("EXP: " + EXP);
		writeChar.print("SKILLS: ");
		for (Skill skill : skills)
			writeChar.print(skill.getName() + ", ");
		writeChar.println();

		writeChar.print("EQP: ");
		if (equipATK != null)
			writeChar.print(equipATK.getName());
		for (Armor arm : equipDEF)
			if (arm != null)
				writeChar.print("[a]" + arm.getName());

		writeChar.println();

		writeChar.print("INV: ");
		for (Item item : inv) {
			if (item.item == Sort.WEAPON)
				writeChar.print("[w]");
			else if (item.item == Sort.ARMOR)
				writeChar.print("[a]");
			writeChar.print(item.name + "#" + item.size + ", ");
		}
		writeChar.flush();
		writeChar.close();
	}

	public void updateStatus() {
		File file = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Character.txt");
		PrintWriter writeChar;
		try {
			writeChar = new PrintWriter(file);
			writeChar.println("NAME: " + name);
			writeChar.println("CREATOR: " + CREATOR);
			writeChar.println("LEVEL: " + level);
			writeChar.println("HP: " + HP);
			writeChar.println("ENG: " + ENG);
			writeChar.println("EXP: " + EXP);
			writeChar.print("SKILLS: ");
			for (Skill skill : skills)
				writeChar.print(skill.getName() + ", ");
			writeChar.println();
			writeChar.print("EQP: ");
			if (equipATK != null)
				writeChar.print("[w]" + equipATK.getName() + ", ");
			for (Armor arm : equipDEF)
				if (arm != null)
					writeChar.print("[a]" + arm.getName() + ", ");

			writeChar.println();

			writeChar.print("INV: ");
			for (Item item : inv) {
				if (item.item == Sort.WEAPON)
					writeChar.print("[w]");
				else if (item.item == Sort.ARMOR)
					writeChar.print("[a]");
				writeChar.print(item.name + "#" + item.size + ", ");
			}
			writeChar.flush();
			writeChar.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find Character.txt File!");
			e.printStackTrace();
		}
	}

	public int atk() {
		if (baseDMG % 2 == 0)
			return (baseDMG / 2 + rand.nextInt(baseDMG / 2));

		return ((baseDMG + 1) / 2 + rand.nextInt((baseDMG + 1) / 2));
	}

	public int def() {
		if (baseARM % 2 == 0)
			return (baseARM / 2 + rand.nextInt(baseARM / 2));

		return ((baseARM + 1) / 2 + rand.nextInt((baseARM + 1) / 2));
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHP(int offset) {
		HP += offset;
		if (HP < 0)
			HP = 0;
		if (HP > maxHP)
			HP = maxHP;
	}

	public void setENG(int offset) {
		ENG += offset;
		if (ENG < 0)
			ENG = 0;
		if (ENG > maxENG)
			ENG = maxENG;
	}

	public void setLevel(int level) {
		this.level = level;
		levelConst = level * 200;
		maxEXP = 50 + levelConst * levelConst / 10000;
		maxHP = 50 + (level * level - level) * 10;
		maxENG = 20 + 5 * (level - 1);
		baseDMG = 30 * level;
		baseARM = 1 * level;
		updateStatus();
	}

	public void putIn(Sort item) {
		for (Item slot : inv)
			if (slot.item == item) {
				slot.size += 1;
				return;
			}
		inv.add(new Item(item, 1));
		updateStatus();
	}

	public void putIn(Sort item, int size) {
		for (Item slot : inv)
			if (slot.item == item) {
				slot.size += size;
				return;
			}
		inv.add(new Item(item, size));
		updateStatus();
	}

	public void putIn(String item) {
		switch (item) {
		case "Health Potion":
			inv.add(new Item(Sort.HP, 1));
			break;
		case "Energy Potion":
			inv.add(new Item(Sort.EP, 1));
			break;
		case "Coins":
			inv.add(new Item(Sort.COIN, 1));
			break;
		}
		updateStatus();
	}

	public void putIn(String item, int size) {
		if (item.contains("[w]"))
			putIn(Weapon.findByName(item.replace("[w]", "")));
		else if (item.contains("[a]"))
			putIn(Armor.findByName(item.replace("[a]", "")));
		else
			switch (item) {
			case "Health Potion":
				inv.add(new Item(Sort.HP, size));
				break;
			case "Energy Potion":
				inv.add(new Item(Sort.EP, size));
				break;
			case "Coins":
				inv.add(new Item(Sort.COIN, size));
				break;
			}
		updateStatus();
	}

	@SuppressWarnings("resource")
	public void putIn(Weapon item) {
		inv.add(item);
		File file = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Items/cWeapons.txt");
		StringBuilder sb = new StringBuilder();
		try {
			PrintWriter write = new PrintWriter(new FileOutputStream(file, true));
			Scanner read = new Scanner(file);
			while (read.hasNext())
				sb.append(read.nextLine() + "\n");

			if (!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length() - 1);
			read.close();

			if (sb.toString().contains(item.fileFormat()))
				return;

			write.print(item.fileFormat());
			write.flush();
			write.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateStatus();
	}

	@SuppressWarnings("resource")
	public void putIn(Armor item) {
		inv.add(item);
		File file = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Items/cArmor.txt");
		StringBuilder sb = new StringBuilder();
		try {
			PrintWriter write = new PrintWriter(new FileOutputStream(file, true));
			Scanner read = new Scanner(file);
			while (read.hasNext())
				sb.append(read.nextLine() + "\n");

			if (!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length() - 1);

			if (sb.toString().contains(item.fileFormat()))
				return;

			read.close();
			write.print(item.fileFormat());
			write.flush();
			write.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateStatus();
	}

	public void setEXP(int exp) {
		EXP = exp;
	}

	public boolean takeOut(Weapon weapon) {
		for (Item slot : inv)
			if (slot == weapon) {
				if (slot.size > 1)
					slot.size -= 1;
				else
					inv.remove(slot);

				updateStatus();
				return true;
			}
		return false;
	}

	public boolean takeOut(Armor armor) {
		for (Item slot : inv)
			if (slot == armor) {
				if (slot.size > 1)
					slot.size -= 1;
				else
					inv.remove(slot);

				updateStatus();
				return true;
			}
		return false;
	}

	public boolean takeOut(Sort item) {
		for (Item slot : inv)
			if (slot.item == item) {
				if (slot.size > 1)
					slot.size -= 1;
				else
					inv.remove(slot);

				updateStatus();
				return true;
			}
		return false;
	}

	public boolean takeOut(Sort item, int size) {
		for (Item slot : inv)
			if (slot.item == item) {
				if (slot.size >= size)
					slot.size -= size;
				else
					inv.remove(slot);

				updateStatus();
				return true;
			}
		return false;
	}

	public boolean checkInv(Sort item) {
		if (inv.size() > 0)
			for (Item slot : inv)
				if (slot.item == item)
					return true;

		return false;
	}

	public int giveItemSize(Sort item) {
		if (inv.size() > 0)
			for (Item slot : inv)
				if (slot.item == item)
					return slot.getSize();

		return 0;
	}

	public boolean checkInv(Sort item, int size) {
		if (inv.size() > 0)
			for (Item slot : inv)
				if (slot.item == item && slot.size >= size)
					return true;

		return false;
	}

	public void setEQPw(String weapon) {
		equipATK = Weapon.findByName(weapon);
	}

	public void setEQPa(String armor) {
		Armor a = Armor.findByName(armor);
		AfekaLandsController.player1.wear(a);
	}

	public ArrayList<Item> getInv() {
		return inv;
	}

	public String invString() {
		StringBuilder sb = new StringBuilder();
		sb.append("INVENTORY: ");
		if (inv.size() > 0)
			for (Item slot : inv) {
				if (slot.item == Sort.HP)
					sb.append("🍎(" + slot.size + "),");
				else if (slot.item == Sort.EP)
					sb.append("⚡(" + slot.size + "),");
				else if (slot.item == Sort.WEAPON)
					sb.append("🗡" + slot.getName() + ",");
				else if (slot.item == Sort.ARMOR)
					sb.append("👘" + slot.getName() + ",");
				else if (slot.item == Sort.COIN)
					sb.append("💰(" + slot.size + "),");
			}
		if (sb.toString().charAt(sb.length() - 1) == ',')
			sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	public int getCOINS() {
		for (Item slot : inv)
			if (slot.item == Sort.COIN)
				return slot.size;

		return 0;
	}

	public Weapon getWeapon() {
		return equipATK;
	}

	public Armor[] getArmor() {
		return equipDEF;
	}

	public int getDMG() {
		return baseDMG;
	}

	public String getName() {
		return name;
	}

	public int getDEF() {
		return baseARM;
	}

	public int getHP() {
		return HP;
	}

	public int getENG() {
		return ENG;
	}

	public int getMaxEXP() {
		return maxEXP;
	}

	public int getEXP() {
		return EXP;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMaxENG() {
		return maxENG;
	}

	public String getCREATOR() {
		return CREATOR;
	}
}
