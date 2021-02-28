package AfekaLands;

public class Item {
	public static enum Sort {HP,EP,COIN,WEAPON,ARMOR,BEER};
	protected Sort item;
	protected String name,description;
	protected int price,size;
	protected boolean use;
	
	
	public Item(Sort item,int size) {
		this.size = size;
		switch(item.name()) {
			case "HP":
				createHP();
				break;
			case "EP":
				createEP();
				break;
			case "COIN":
				createCOIN();
				break;
			case "WEAPON":
				createWEAPON();
				break;
			case "ARMOR":
				createARMOR();
				break;
			case "BEER":
				createBEER();
				break;
		}
	}
	
	
	protected void addSize(int size) {
		this.size+=size;
	}
	
	public String getName() {
		return name;
	}
	public String getDes() {
		return description;
	}
	public int getPrice() { 
		return price;
	}	
	public int getSize() {
		return size;
	}
	public Sort getSort() {
		return item;
	}
	public boolean canUse() {
		return use;
	}
	
	
	protected void createHP() {
		name = "Health Potion";
		use = true;
		price = 4;
		this.item = Sort.HP;
	}
	protected void createEP() {
		name = "Energy Potion";
		use = true;
		price = 3;
		this.item = Sort.EP;
	}
	protected void createCOIN() {
		name = "Coins";
		this.item = Sort.COIN;
		use = false;
	}
	protected void createWEAPON() {
		this.item = Sort.WEAPON;
		use = false;
	}
	protected void createARMOR() {
		this.item = Sort.ARMOR;
		use = false;
	}
	protected void createBEER() {
		name = "Beer";
		use = true;
		this.item = Sort.BEER;
		price = 2;
	}
}