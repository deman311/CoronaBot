package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Weapon extends Item {
	private int damage;
	private int[] crit = new int[2];
	Random rand = new Random();
	public static ArrayList<Weapon> allWeapons = new ArrayList<Weapon>();
	
	public Weapon() {
		super(Sort.WEAPON,1);
	}
	
	public Weapon(int level) {
		super(Sort.WEAPON,1);
		if(level==1)
			level = 2;
		super.name = createName();
		damage = (level/2+rand.nextInt(level/2))*(10+rand.nextInt(11));
		if(level==1)
			damage = 10;
		super.price = damage*level/10;
		crit[0] = rand.nextInt(level*5)+1;
		crit[1] = 1+rand.nextInt(20);
		super.description = super.name + ", is a sword with a Damage of " + damage + ", and a Critical Rate of " + crit[0] + " Damage with " + crit[1] + "% Chance.";
		allWeapons.add(this);
	}
	
	public Weapon(String name, int level) {
		super(Sort.WEAPON,1);
		if(level==1)
			level = 2;
		super.name = name;
		damage = (level/2+rand.nextInt(level/2))*(10+rand.nextInt(11));
		damage = 10;
		super.price = damage*level/10;
		crit[0] = rand.nextInt(level*5)+1;
		crit[1] = 1+rand.nextInt(20);
		super.description = super.name + ", is a sword with a Damage of " + damage + ", and a Critical Rate of " + crit[0] + " Damage with " + crit[1] + "% Chance.";
		allWeapons.add(this);
	}

	private String createName() {
		Scanner readName;
		Scanner readTitle;
		String finalName = null;
		try {			
			readName = new Scanner(new File("./Files/Afekalands/Items/wNames.txt"));
			readTitle = new Scanner(new File("./Files/Afekalands/Items/wTitle.txt"));
			StringBuilder sbn = new StringBuilder();
			StringBuilder sbt= new StringBuilder();
			while(readName.hasNext())
				sbn.append(readName.nextLine() + "\n");
			sbn.deleteCharAt(sbn.length()-1);
			String[] allNames = sbn.toString().split("\n");
			while(readTitle.hasNext())
				sbt.append(readTitle.nextLine() + "\n");
			sbt.deleteCharAt(sbt.length()-1);
			String[] allTitles = sbt.toString().split("\n");
			finalName = allTitles[rand.nextInt(allTitles.length)] + " " + allNames[rand.nextInt(allNames.length)];
			readName.close();
			readTitle.close();		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalName;
	}

	public void rewriteDes() {
		super.description = super.name + ", is a sword with a Damage of " + damage + ", and a Critical Rate of " + crit[0] + " Damage with " + crit[1] + "% Chance.";
	}
	
	public String fileFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n#");
		sb.append("\nname: " + super.name);
		sb.append("\nprice: " + super.price);
		sb.append("\ndamage: " + damage);
		sb.append("\ncrit: " + crit[0] + " " + crit [1]);
		sb.append("\ndescription: " + super.description);
		
		return sb.toString();
	}
	
	public static boolean rewriteWeapon(Weapon OLD,Weapon NEW) {
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cWeapons.txt"));
			StringBuilder sb = new StringBuilder();
			
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			sb.deleteCharAt(sb.length()-1).toString();
			read.close();
			
			if(sb.toString().contains(OLD.fileFormat())) {
				PrintWriter write = new PrintWriter("./Files/AfekaLands/Items/cWeapons.txt");
				write.println(sb.toString().replace(OLD.fileFormat(), NEW.fileFormat()));
				
				write.flush();
				write.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static Weapon findByName(String name) {
		Weapon wep = null;
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cWeapons.txt"));
			StringBuilder sb = new StringBuilder();
			String[] allWeapons;
			String[] weaponText = null;
			String LINE;
			
			while(read.hasNext()) {
				LINE=read.nextLine();
				if(!LINE.isEmpty())
					sb.append(LINE+"\n");
			}
			sb.deleteCharAt(sb.length()-1);
			allWeapons = sb.toString().split("#");
			read.close();
			
			for(String text : allWeapons) {
				if(text.contains("name: " + name)) {
					weaponText = text.split("\n");
					wep = new Weapon();
					break;
				}
			}
			
			if(weaponText==null) {
				System.out.println("WRONG WEAPON NAME SEARCH.");
				return null;
			}
			
			for(String line : weaponText) {
				if(line.contains("name: "))
					wep.setName(line.replace("name: ", ""));
				else if(line.contains("price: "))
					wep.setPrice(Integer.parseInt(line.replace("price: ", "")));
				else if(line.contains("description: "))
					wep.setDes(line.replace("description: ", ""));
				else if(line.contains("damage: "))
					wep.setDamage(Integer.parseInt(line.replace("damage: ", "")));
				else if(line.contains("crit: ")) {
					String[] critString = new String[2];
					int[] crit = new int[2];
					critString = line.replace("crit: ", "").split(" ");
					crit[0] = Integer.parseInt(critString[0]);
					crit[1] = Integer.parseInt(critString[1]);
					wep.setCrit(crit);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		allWeapons.add(wep);
		return wep;
	}
	
	public static void removeWeapon(Weapon weapon) {
		StringBuilder sb = new StringBuilder();
		String op=null;
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cWeapons.txt"));
			
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			
			PrintWriter write = new PrintWriter("./Files/AfekaLands/Items/cWeapons.txt");
			
			if(sb.toString().contains(weapon.fileFormat()))
					op = sb.toString().replace(weapon.fileFormat(), "");
			
			if(op==null)
				op = "";
			write.print(op);
			write.flush();
			write.close();
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public int getDamage() {
		return damage;
	}
	public int[] getCrit() {
		return crit;
	}
	public String getName() {
		return super.name;
	}
	public String getDes() {
		return super.description;
	}
	public int getPrice() {
		return super.price;
	}
	public void setName(String name) {
		super.name = name;
	}
	public void setDes(String des) {
		super.description = des;
	}
	public void setPrice(int price) {
		super.price = price;
	}
	public void setCrit(int[] crit) {
		this.crit = crit;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
}
