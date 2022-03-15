package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import AfekaLands.Armor.aKind;
import AfekaLands.Item.Sort;

public class Enemy {
	private String name;
	private int hp, def, level, exp, maxHP, levelConst, damage;
	private boolean looted;
	private int[] enemyPOS = new int[2];
	private ArrayList<Item> loot = new ArrayList<Item>();

	public static Random rand = new Random();
	public File namesFile = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Enemy/eNames.txt");
	public File titleFile = new File(AfekaLandsController.FS_PATH + "/AfekaLands/Enemy/eTitle.txt");

	public Enemy() {
		name = newName();
		int rando = rand.nextInt(6);

		if (rand.nextInt(3) > 0)
			rando *= -1;
		if (AfekaLandsController.player1.getLevel() > 5)
			level = AfekaLandsController.player1.getLevel() + rando;
		else
			level = 1 + rand.nextInt(5);

		levelConst = level * 200;

		hp = 50 + level * (5 + rand.nextInt(level));
		maxHP = hp;
		def = (hp) / (10 + rand.nextInt(level) * 20);
		exp = levelConst * levelConst / 500000 + 25;
		damage = (int) (level * Math.ceil(Math.sqrt(level)) * (rand.nextInt(level) + 3));

		if (AfekaLandsController.player1.getLevel() < level)
			exp += 0.25 * (level - AfekaLandsController.player1.getLevel());
		else if (AfekaLandsController.player1.getLevel() > level + 1)
			exp -= 0.25 * (AfekaLandsController.player1.getLevel() - level);

		if (exp <= 0)
			exp = 1;

		looted = false;
	}

	public Enemy(int level) {
		this.level = level;
		levelConst = level * 200;
		name = newName();

		hp = 50 + level * level * (5 + rand.nextInt(10));
		maxHP = hp;
		def = (hp) / (10 + rand.nextInt(level) * 20);
		exp = levelConst * levelConst / 500000 + 25;
		damage = level * 20 + rand.nextInt(level) * rand.nextInt(level) * 5;

		if (AfekaLandsController.player1.getLevel() < level)
			exp += 0.25 * (level - AfekaLandsController.player1.getLevel());
		else if (AfekaLandsController.player1.getLevel() > level + 1)
			exp -= 0.25 * (AfekaLandsController.player1.getLevel() - level);

		if (exp <= 0)
			exp = 1;

		looted = false;
	}

	public void createLoot() {
		Item money = null, HP = null, EP = null;
		if (!(rand.nextInt(5) == 0))
			money = new Item(Sort.COIN, level * (1 + rand.nextInt(5)));
		if (rand.nextInt(3) == 0)
			HP = new Item(Sort.HP, 1 + level / 5);
		if (rand.nextInt(3) == 0)
			EP = new Item(Sort.EP, 1 + level / 10);
		if (rand.nextInt(5) == 0)
			addWeapon(1);
		if (rand.nextInt(4) == 0)
			addArmor(1);

		if (money != null)
			loot.add(money);
		if (HP != null)
			loot.add(HP);
		if (EP != null)
			loot.add(EP);
	}

	public void addLoot(Sort sort, int size) {
		for (Item slot : loot)
			if (slot.getSort() == sort) {
				slot.size += size;
				return;
			}
		loot.add(new Item(sort, size));
	}

	public void addWeapon(int amount) {
		Weapon wep = null;
		int level = AfekaLandsController.player1.getLevel();
		for (int i = 0; i < amount; i++) {
			if (rand.nextInt(2) == 0)
				level -= rand.nextInt(5);
			else
				level += rand.nextInt(5);
			wep = new Weapon(level);
			wep = Mods.chanceMod(wep);
			loot.add(wep);
		}
	}

	public void addArmor(int amount) {
		Armor arm = null;
		int level = AfekaLandsController.player1.getLevel();
		aKind[] kinds = aKind.values();
		for (int i = 0; i < amount; i++) {
			if (rand.nextInt(2) == 0)
				level -= rand.nextInt(5);
			else
				level += rand.nextInt(5);
			arm = new Armor(kinds[rand.nextInt(kinds.length)], level);
			arm = Mods.chanceMod(arm);
			loot.add(arm);
		}
	}

	public String newName() {
		String[] names = new String[0];
		String[] titles = new String[0];
		Scanner readName, readTitle;
		String title, name, finish;
		try {
			readName = new Scanner(namesFile);
			readTitle = new Scanner(titleFile);
			while (readName.hasNext()) {
				names = Arrays.copyOf(names, names.length + 1);
				names[names.length - 1] = readName.nextLine();
			}
			while (readTitle.hasNext()) {
				titles = Arrays.copyOf(titles, titles.length + 1);
				titles[titles.length - 1] = readTitle.nextLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		title = titles[rand.nextInt(titles.length)];
		name = names[rand.nextInt(names.length)];
		if (rand.nextInt(3) == 0)
			finish = title + " " + name;
		else
			finish = name;

		return finish;
	}

	public String dealDamage(int damage) {
		int Damage = damage - def;

		if (Damage > 0) {
			hp -= Damage;
			if (hp < 0)
				hp = 0;
			writeEnemy();
			return "Total damage dealt: " + Damage;
		} else
			return "Missed.";
	}

	public void writeEnemy() {
		try {
			PrintWriter write = new PrintWriter(AfekaLandsController.FS_PATH + "/AfekaLands/Enemy/Enemy.txt");
			write.append(name + "\n");
			write.append(String.valueOf(hp) + "\n");
			write.append(String.valueOf(def) + "\n");
			write.flush();
			write.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean move() {
		int c = rand.nextInt(5);
		int lasty, lastx;
		lasty = enemyPOS[0];
		lastx = enemyPOS[1];

		switch (c) {
		case 0:
			--enemyPOS[0];
			if (enemyPOS[0] < 0) {
				enemyPOS[0] = lasty;
				return false;
			}
			break;
		case 1:
			++enemyPOS[0];
			if (enemyPOS[0] >= MapGen2.getMapHeight()) {
				enemyPOS[0] = lasty;
				return false;
			}
			break;
		case 2:
			--enemyPOS[1];
			if (enemyPOS[1] < 0) {
				enemyPOS[1] = lastx;
				return false;
			}
			break;
		case 3:
			++enemyPOS[1];
			if (enemyPOS[1] >= MapGen2.getMapWidth()) {
				enemyPOS[1] = lastx;
				return false;
			}
			break;
		case 4:
			return false;
		}
		if (!MapGen2.putAt(enemyPOS[0], enemyPOS[1], "E")) {
			enemyPOS[0] = lasty;
			enemyPOS[1] = lastx;
			if (MapGen2.checkTackle()) {
				MapGen2.setTackle(false);
				MapGen2.allEnemies.remove(this);
				MapGen2.removeFrom(lasty, lastx);
				return true;
			}

			return false;
		}
		MapGen2.removeFrom(lasty, lastx);
		return false;
	}

	public void setPOS(int Y, int X) {
		enemyPOS[0] = Y;
		enemyPOS[1] = X;
	}

	public int[] getPOS() {
		return enemyPOS;
	}

	public int getDamage() {
		return damage;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}

	public int getHP() {
		return hp;
	}

	public int getLevel() {
		return level;
	}

	public int getEXP() {
		return exp;
	}

	public boolean isDead() {
		if (hp <= 0)
			return true;
		return false;
	}

	public ArrayList<Item> getLoot() {
		return loot;
	}

	public void setLooted(boolean state) {
		looted = state;
	}

	public boolean isLooted() {
		return looted;
	}

	public int getDEF() {
		return def;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public void setDMG(int dmg) {
		this.damage = dmg;
	}

	public void setDEF(int def) {
		this.def = def;
	}

	public void setEXP(int exp) {
		this.exp = exp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
}
