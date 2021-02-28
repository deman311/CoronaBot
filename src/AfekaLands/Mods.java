package AfekaLands;

import java.util.Random;

import AfekaLands.Item.Sort;
import MyBot.CoronaBot;

public interface Mods {
	enum wMOD {TRASH,WEAK,GOOD,STRONG,EPIC,GODLY};
	enum eMOD {BABY,WEAK,TOUGH,STRONG,BOSS};
	Random rand = new Random();
	
	public static Weapon modWeapon(Weapon wep,wMOD mod) {
		Weapon old=wep;
		switch (mod) {
		case TRASH:
			wep.name = "Trash " + wep.name;
			wep.setDamage(wep.getDamage()/3);
			wep.setPrice(wep.getPrice()/5);
			wep.rewriteDes();
			break;
		case WEAK:
			wep.name = "Weak " + wep.name;
			wep.setDamage((int)(wep.getDamage()*0.7));
			wep.setPrice((int)(wep.getPrice()*0.5));
			wep.rewriteDes();
			break;
		case GOOD:
			wep.name = "Good " + wep.name;
			wep.setDamage((int)(wep.getDamage()*1.2));
			wep.setPrice((int)(wep.getPrice()*1.2));
			wep.rewriteDes();
			break;
		case STRONG:
			wep.name = "Strong " + wep.name;
			wep.setDamage((int)(wep.getDamage()*1.4));
			wep.setPrice((int)(wep.getPrice()*1.2));
			wep.rewriteDes();
			break;
		case EPIC:
			wep.name = "EPIC " + wep.name;
			wep.setDamage((int)(wep.getDamage()*1.7));
			wep.setPrice((int)(wep.getPrice()*1.5));
			wep.rewriteDes();
			break;
		case GODLY:
			wep.name = "GODLY " + wep.name;
			wep.setDamage((int)(wep.getDamage()*2.2));
			wep.setPrice((int)(wep.getPrice()*2));
			wep.rewriteDes();
			break;
		}
		System.out.println("Weapon rewrite: " + Weapon.rewriteWeapon(old, wep));
		return wep;
	}
	public static Armor modArmor(Armor arm,wMOD mod) {
		Armor old=arm;
		switch (mod) {
		case TRASH:
			arm.name = "Trash " + arm.name;
			arm.setDEF(arm.getDEF()/3);
			arm.setPrice(arm.getPrice()/5);
			arm.rewriteDes();
			break;
		case WEAK:
			arm.name = "Weak " + arm.name;
			arm.setDEF((int)(arm.getDEF()*0.7));
			arm.setPrice((int)(arm.getPrice()*0.5));
			arm.rewriteDes();
			break;
		case GOOD:
			arm.name = "Good " + arm.name;
			arm.setDEF((int)(arm.getDEF()*1.2));
			arm.setPrice((int)(arm.getPrice()*1.2));
			arm.rewriteDes();
			break;
		case STRONG:
			arm.name = "Strong " + arm.name;
			arm.setDEF((int)(arm.getDEF()*1.4));
			arm.setPrice((int)(arm.getPrice()*1.2));
			arm.rewriteDes();
			break;
		case EPIC:
			arm.name = "EPIC " + arm.name;
			arm.setDEF((int)(arm.getDEF()*1.7));
			arm.setPrice((int)(arm.getPrice()*1.5));
			arm.rewriteDes();
			break;
		case GODLY:
			arm.name = "GODLY " + arm.name;
			arm.setDEF((int)(arm.getDEF()*2.2));
			arm.setPrice((int)(arm.getPrice()*2));
			arm.rewriteDes();
			break;
		}
		System.out.println("Armor rewrite: " + Armor.rewriteArmor(old, arm));
		return arm;
	}
	public static Enemy modEnemy(Enemy e1,eMOD mod) {
		switch (mod) {
		case BABY:
			e1.setName("Baby " + e1.getName());
			e1.setHP((int)(e1.getHP()*0.1));
			e1.setMaxHP(e1.getHP());
			e1.setDEF(0);
			e1.setDMG(5);
			e1.setEXP(5);
			e1.setLooted(true);
			break;
		case WEAK:
			e1.setName("Weak " + e1.getName());
			e1.setHP((int)(e1.getHP()*0.5));
			e1.setMaxHP(e1.getHP());
			e1.setDEF((int)(e1.getDEF()*0.5));
			e1.setDMG((int)(e1.getDamage()*0.5));
			e1.setEXP((int)(e1.getEXP()*0.5));
			break;
		case TOUGH:
			e1.setName("Tough " + e1.getName());
			e1.setHP((int)(e1.getHP()*2));
			e1.setMaxHP(e1.getHP());
			e1.setDEF((int)(e1.getDEF()*1.2));
			e1.setDMG((int)(e1.getDamage()*0.75));
			e1.setEXP((int)(e1.getEXP()*1.5));
			e1.addWeapon(1);
			e1.addArmor(1);
			break;
		case STRONG:
			e1.setName("Strong " + e1.getName());
			e1.setDEF((int)(e1.getDEF()*1.25));
			e1.setDMG((int)(e1.getDamage()*1.5));
			e1.setEXP((int)(e1.getEXP()*1.75));
			e1.addLoot(Sort.COIN, CoronaBot.player1.getLevel()*2+rand.nextInt(CoronaBot.player1.getLevel()));
			e1.addWeapon(1);
			e1.addArmor(1);
			break;
		case BOSS:
			e1.setName("[BOSS] " + e1.getName());
			if(e1.getLevel()<CoronaBot.player1.getLevel())
				e1 = new Enemy(CoronaBot.player1.getLevel());
			e1.setHP((int)(e1.getHP()*3));
			e1.setMaxHP(e1.getHP());
			e1.setDEF((int)(e1.getDEF()*1.5));
			e1.setDMG((int)(e1.getDamage()*2));
			e1.setEXP((int)(e1.getEXP()*5));
			e1.addLoot(Sort.COIN, CoronaBot.player1.getLevel()*4+rand.nextInt(CoronaBot.player1.getLevel()));
			e1.addLoot(Sort.HP, CoronaBot.player1.getLevel()*(1+rand.nextInt(2)));
			e1.addLoot(Sort.EP, CoronaBot.player1.getLevel()*(1+rand.nextInt(2)));
			e1.addWeapon(3);
			e1.addArmor(3);
			break;
		}
		
		return e1;
	}
	
	public static Weapon chanceMod(Weapon wep) {
		int c = rand.nextInt(100);
		if(c>=0 && c<=9)
			return modWeapon(wep, wMOD.TRASH);
		else if(c>=10 && c<=24)
			return modWeapon(wep, wMOD.WEAK);
		else if(c>=75 && c<=92)
			return modWeapon(wep, wMOD.GOOD);
		else if(c>=93 && c<=97)
			return modWeapon(wep, wMOD.EPIC);
		else if(c==98 || c==99)
			return modWeapon(wep, wMOD.GODLY);
		
		return wep;
	}
	public static Armor chanceMod(Armor arm) {
		int c = rand.nextInt(100);
		if(c>=0 && c<=9)
			return modArmor(arm, wMOD.TRASH);
		else if(c>=10 && c<=24)
			return modArmor(arm, wMOD.WEAK);
		else if(c>=75 && c<=92)
			return modArmor(arm, wMOD.GOOD);
		else if(c>=93 && c<=97)
			return modArmor(arm, wMOD.EPIC);
		else if(c==98 || c==99)
			return modArmor(arm, wMOD.GODLY);
		
		return arm;
	}
	public static Enemy chanceMod(Enemy e1) {
		int c = rand.nextInt(100);
		if(c>=0 && c<=5)
			return modEnemy(e1, eMOD.BABY);
		else if(c>=6 && c<=20)
			return modEnemy(e1, eMOD.WEAK);
		else if(c>=75 && c<=92)
			return modEnemy(e1, eMOD.TOUGH);
		else if(c>=93 && c<=97)
			return modEnemy(e1, eMOD.STRONG);
		else if(c==98 || c==99)
			return modEnemy(e1, eMOD.BOSS);
		
		return e1;
	}
}
