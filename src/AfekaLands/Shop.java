package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import AfekaLands.Armor.aKind;
import AfekaLands.Item.Sort;
import MyBot.CoronaBot;

public class Shop {
	private String shopName,ownerName;
	private int[] pos = new int[2];

	private ArrayList<Item> inv = new ArrayList<Item>();
	
	public static Random rand = new Random();
	
	public Shop(int i,int j) {
		pos[0] = i;
		pos[1] = j;
		shopName = "The " + genShopTitle() + " " + genShopName();
		ownerName = genOwnerName();
		
		aKind[] kinds = aKind.values();
		
		if(rand.nextInt(2)==0)
			inv.add(new Item(Sort.HP,rand.nextInt(CoronaBot.player1.getLevel()*(rand.nextInt(10)+1))));
		if(rand.nextInt(2)==0)
			inv.add(new Item(Sort.EP,rand.nextInt(CoronaBot.player1.getLevel()*(rand.nextInt(3)+1))));
		
		inv.add(new Item(Sort.BEER,rand.nextInt(10)+2));
		
		if(rand.nextInt(5)==0)
			inv.add(new Weapon(CoronaBot.player1.getLevel()));
		if(rand.nextInt(5)==0)
			inv.add(new Weapon(CoronaBot.player1.getLevel()));
		if(rand.nextInt(4)==0)
			inv.add(new Armor(kinds[rand.nextInt(kinds.length)],CoronaBot.player1.getLevel()));
		if(rand.nextInt(4)==0)
			inv.add(new Armor(kinds[rand.nextInt(kinds.length)],CoronaBot.player1.getLevel()));
	}
	
	public void reStock() {
		inv.clear();
		
		aKind[] kinds = aKind.values();
		
		if(rand.nextInt(2)==0)
			inv.add(new Item(Sort.HP,rand.nextInt(10)+1));
		if(rand.nextInt(2)==0)
			inv.add(new Item(Sort.EP,rand.nextInt(10)+1));
		
		inv.add(new Item(Sort.BEER,rand.nextInt(10)+2));
		
		if(rand.nextInt(5)==0)
			inv.add(new Weapon(CoronaBot.player1.getLevel()));
		if(rand.nextInt(5)==0)
			inv.add(new Weapon(CoronaBot.player1.getLevel()));
		if(rand.nextInt(4)==0)
			inv.add(new Armor(kinds[rand.nextInt(kinds.length)],CoronaBot.player1.getLevel()));
		if(rand.nextInt(4)==0)
			inv.add(new Armor(kinds[rand.nextInt(kinds.length)],CoronaBot.player1.getLevel()));
	}
	
	public String genOwnerName() {
		Scanner readfile;
		ArrayList<String> names = new ArrayList<String>();
		try {
			readfile = new Scanner(new File("./Files/AfekaLands/Shop/oNames.txt"));
			while(readfile.hasNext())
				names.add(readfile.nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names.get(rand.nextInt(names.size()));
	}
	
	public String genShopName() {
		Scanner readfile;
		ArrayList<String> names = new ArrayList<String>();
		try {
			readfile = new Scanner(new File("./Files/AfekaLands/Shop/sNames.txt"));
			while(readfile.hasNext())
				names.add(readfile.nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names.get(rand.nextInt(names.size()));
	}
	
	public String genShopTitle() {
		Scanner readfile;
		ArrayList<String> names = new ArrayList<String>();
		try {
			readfile = new Scanner(new File("./Files/AfekaLands/Shop/sTitle.txt"));
			while(readfile.hasNext())
				names.add(readfile.nextLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names.get(rand.nextInt(names.size()));
	}
	
	public void removeInv(Sort item) {	
		for(Item slot : inv) {
			if(slot.item == item) {
				if(slot.size>1) {
					slot.size-=1;
					return;
				}
				inv.remove(slot);
				return;
			}
		}
	}
	public void removeInv(Sort item,int size) {	
		for(Item slot : inv) {
			if(slot.item == item) {
				if(slot.size>=size) {
					slot.size-=size;
					return;
				}
				return;
			}
		}
	}
	public void removeInv(String item) {	
		for(Item slot : inv) {
			if(slot.getName().equalsIgnoreCase(item)) {
				if(slot.size>1) {
					slot.size-=1;
					return;
				}
				inv.remove(slot);
				return;
			}
		}
	}
	
	public String getShopName() {
		return shopName;
	}

	public String getOwnerName() {
		return ownerName;
	}
	
	public int[] getPos() {
		return pos;
	}
	
	public boolean checkItem(Sort item) {
		for(Item slot : inv)
			if(slot.item==item)
				return true;
		
		return false;
	}
	
	public ArrayList<Item> getShopINV() {
		return inv;
	}
}
