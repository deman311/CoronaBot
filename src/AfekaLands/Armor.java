package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Armor extends Item {
	protected static enum aKind {HEAD,TORSO,LEG,BOOT,RING};
	protected static enum aEffect {DAMAGE,MAGICRES,DEF,MANA,HP};
	private aKind kind;
	private aEffect effect; //IMPLEMENT EFFECTS TO ARMOR
	private int def;
	
	protected static ArrayList<Armor> allArmors = new ArrayList<Armor>();
	
	Random rand = new Random();
	
	public Armor() {
		super(Sort.ARMOR, 1);
	}
	
	public Armor(aKind kind,int level) {
		super(Sort.ARMOR,1);
		if(level == 1)
			level = 2;
		this.kind = kind;
		super.name = createName(kind);
		
		switch (kind) {
		case HEAD:
			def = level+rand.nextInt(2)*level;
			break;
		case TORSO:
			def = 2*level+rand.nextInt(5)*level;
			break;
		case LEG:
			def = 2*level+rand.nextInt(4)*level;
			break;
		case BOOT:
			def = level+rand.nextInt(4)*level;
			break;
		case RING:
			def = 1+rand.nextInt(level);
			break;
		}
		
		super.price = def*level/10;
		super.description = super.name + ", is a " + kind + " with a Defense of " + def;
		allArmors.add(this);
	}
	
	public static boolean removeArmor(Armor armor) {
		StringBuilder sb = new StringBuilder();
		Scanner read;
		try {
			read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			read.close();
			
			PrintWriter write = new PrintWriter("./Files/AfekaLands/Items/cArmor.txt");
			if(sb.toString().contains(armor.fileFormat())); {
				write.println(sb.toString().replace(armor.fileFormat(), ""));
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
	
	public void rewriteDes() {
		super.description = super.name + ", is a " + kind + " with a Defense of " + def;
	}
	
	public static boolean rewriteArmor(Armor OLD,Armor NEW) {
		StringBuilder sb = new StringBuilder();
		Scanner read;
		try {
			read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			if(!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length()-1);
			read.close();
			PrintWriter write = new PrintWriter("./Files/AfekaLands/Items/cArmor.txt");
			if(sb.toString().contains(OLD.fileFormat())); {
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
	
	public static Armor findByName(String name) {
		StringBuilder sb = new StringBuilder();
		String[] sepArmor,sepText;
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			if(!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length()-1);
			read.close();
			
			sepArmor=sb.toString().split("#");
			
			for(String arm : sepArmor) {
				if(arm.contains("name: " + name)) {
					sepText = arm.split("\n");
					Armor arm1 = new Armor();
					for(String line : sepText) {
						if(line.contains("name: "))
							arm1.name = line.replace("name: ", "");
						else if(line.contains("description: "))
							arm1.description = line.replace("description: ", "");
						else if(line.contains("price: "))
							arm1.price = Integer.parseInt(line.replace("price: ", ""));
						else if(line.contains("def: "))
							arm1.def = Integer.parseInt(line.replace("def: ", ""));
						else if(line.contains("type: "))
							arm1.setType(line.replace("type: ", ""));
					}
					allArmors.add(arm1);
					return arm1;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("COULD NOT READ ARMOR NAME FROM FILE!");
		return null;
	}
	
	public boolean checkNameExist(String name) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			if(!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length()-1);
			read.close();
			
			if(sb.toString().contains("name: " + name))
				return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void writeArmor() {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			if(!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length()-1);
			read.close();
			if(!sb.toString().contains(this.fileFormat())) {
				PrintWriter write = new PrintWriter("./Files/AfekaLands/Items/cArmor.txt");
				write.println(sb.toString() + fileFormat());
				write.flush();
				write.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String fileFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("#\n");
		sb.append("name: " + super.name + "\n");
		sb.append("description: " + super.description + "\n");
		sb.append("price: " + super.price + "\n");
		sb.append("def: " + def + "\n");
		sb.append("type: " + kind + "\n");
		if(effect!=null)
			sb.append("effect: " + effect.name());
		
		return sb.toString();
	}
	
//	public static int lastCounter() {
//		int counter=0;
//		String line;
//		try {
//			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
//			while(read.hasNext()) {
//				line = read.nextLine();
//				if(line.contains("id: "))
//					if(counter < Integer.parseInt(line.replace("id: ", "")))
//						counter = Integer.parseInt(line.replace("id: ", ""));
//			}
//			read.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return counter;
//	}
	
	public String createName(aKind kind) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		String name = null;
		String[] lines;
		ArrayList<String> HEAD = new ArrayList<String>();
		ArrayList<String> TORSO = new ArrayList<String>();
		ArrayList<String> LEG = new ArrayList<String>();
		ArrayList<String> BOOT = new ArrayList<String>();
		ArrayList<String> RING = new ArrayList<String>();
		try {
			Scanner read = new Scanner(new File("./Files/AfekaLands/Items/aNames.txt"));
			Scanner read2 = new Scanner(new File("./Files/AfekaLands/Items/cArmor.txt"));
			while(read.hasNext())
				sb.append(read.nextLine() + "\n");
			if(!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length()-1);
			read.close();
			while(read2.hasNext())
				sb2.append(read2.nextLine() + "\n");
			if(!sb2.toString().isEmpty())
				sb2.deleteCharAt(sb2.length()-1);
			read2.close();
			
			lines = sb.toString().split("\n");
			for(String line : lines) {
				if(line.contains("HEAD: "))
					HEAD.add(line.replace("HEAD: ", ""));
				if(line.contains("TORSO: "))
					TORSO.add(line.replace("TORSO: ", ""));
				if(line.contains("LEG: "))
					LEG.add(line.replace("LEG: ", ""));
				if(line.contains("BOOT: "))
					BOOT.add(line.replace("BOOT: ", ""));
				if(line.contains("RING: "))
					RING.add(line.replace("RING: ", ""));
			}
			
			switch (kind) {
			case HEAD:				
					name = HEAD.get(rand.nextInt(HEAD.size()));
				break;
			case TORSO:		
					name = TORSO.get(rand.nextInt(TORSO.size()));
				break;
			case LEG:				
					name = LEG.get(rand.nextInt(LEG.size()));
				break;
			case BOOT:				
					name = BOOT.get(rand.nextInt(BOOT.size()));
				break;
			case RING:			
					name = RING.get(rand.nextInt(RING.size()));
				break;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int counter=2;
		while(checkNameExist(name)) {
			if(name.contains("^")) {
				name = name.replace("^" + counter, "^" + (counter+1));
				counter++;
			}
			else
				name = name + "^2";
		}
		
		return name;
	}
	
	@Override
	public String getName() {
		return super.name;
	}
	@Override
	public String getDes() {
		return super.description;
	}
	@Override
	public int getPrice() {
		return super.price;
	}
	public int getDEF() {
		return def;
	}
	public aKind getKind() {
		return kind;
	}
	
	
	public void setType(String type) {
		switch (type) {
		case "HEAD":
			kind = aKind.HEAD;
			break;
		case "TORSO":
			kind = aKind.TORSO;
			break;
		case "LEG":
			kind = aKind.LEG;
			break;
		case "BOOT":
			kind = aKind.BOOT;
			break;
		case "RING":
			kind = aKind.RING;
			break;
		}
	}
	public void setDEF(int def) {
		this.def = def;
	}
	public void setPrice(int price) {
		super.price = price;
	}
}
