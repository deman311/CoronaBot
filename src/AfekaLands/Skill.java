package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Skill {
	private String name,sign;
	private int damage,mana;
	private static ArrayList<Skill> allSkills = new ArrayList<Skill>();
	
	public static void readAll() {
		Scanner read;
		try {
			read = new Scanner(new File("./Files/AfekaLands/Skills.txt"));
			StringBuilder sb = new StringBuilder();
			String[] sepSkills;
			String[] oneSkill;
			while(read.hasNext())
				sb.append(read.nextLine()+"\n");
			
			sepSkills = sb.toString().split("#");
			
			for(String text : sepSkills) {
				oneSkill = text.split("\n");
				Skill a = new Skill();
				for(String line : oneSkill) {
					if(line.contains("name: "))
						a.setName(line.replace("name: ", ""));
					else if(line.contains("sign: "))
						a.setSign(line.replace("sign: ", ""));
					else if(line.contains("damage: "))
						a.setDamage(Integer.parseInt(line.replace("damage: ", "")));
					else if(line.contains("mana: "))
						a.setMana(Integer.parseInt(line.replace("mana: ", "")));
				}
				allSkills.add(a);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Skill findSkillByName(String name) {
		for(Skill skill : allSkills)
			if(skill.getName().contains(name))
				return skill;
		
		return null;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	
	public String getName() {
		return name;
	}
	public String getSign() {
		return sign;
	}
	public int getDamage() {
		return damage;
	}
	public int getMana() {
		return mana;
	}
	public static ArrayList<Skill> skillArray() {
		return allSkills;
	}
}