package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public abstract class MapGen {
	static Random rand = new Random();
	public static String[][] map;
	public static String[][] eMap;
	public static String lastTerrain;
	public static int[] playerPos = new int[2]; 
	public static int moves=0;
	
	public static void createMap() {
		int worldHeight = rand.nextInt(8) + 5;
		int worldWidth = rand.nextInt(8) + 5;
		int numOfTaverns = rand.nextInt(5) + 3;
		boolean rowHasTav = false, playerPlaced = false;
		map = new String[worldHeight][worldWidth];
		StringBuilder mapString = new StringBuilder();
		for (int i = 0; i < worldHeight; i++) {
			for (int j = 0; j < worldWidth; j++) {
				if (numOfTaverns > 0 && rand.nextInt(15) == 1 && !rowHasTav) {
					map[i][j] = "$ ";
					rowHasTav = true;
					numOfTaverns--;
				} else if (!playerPlaced && rand.nextInt(worldHeight * worldWidth / 2) == 1) {
					map[i][j] = "P ";
					playerPos[0] = i;
					playerPos[1] = j;
					playerPlaced = true;
				} else if (!playerPlaced && i == worldHeight - 1 && j == worldWidth - 1) {
					map[i][j] = "P ";
					playerPos[0] = i;
					playerPos[1] = j;
				}
				else {
					if (rand.nextInt(4) == 1)
						map[i][j] = "~ ";
					else
						map[i][j] = "# ";
				}
			}
			rowHasTav = false;
			map[i][worldWidth-1]=map[i][worldWidth-1].substring(0, 1);
		}
		try {
			File file = new File("./Files/AfekaLands/Map.txt");
			PrintWriter write = new PrintWriter(file);
			for (String[] row : map) {
				for (String index : row) {
					mapString.append(index);
				}
				mapString.append("\n");
			}
			mapString.deleteCharAt(mapString.length() - 1);
			write.append(mapString);
			write.flush();
			write.close();
			readMapFile(0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] readMapFile(int type) {
		File mapFile = new File("./Files/AfekaLands/Map.txt");
		File Detailsfile = new File("./Files/AfekaLands/GameDetails.txt");
		Scanner readMap, readDetails;
		String[] textRow;
		String line;
		int mapL = 0, mapW = 0;
		StringBuilder mapText = new StringBuilder();
		try {
			readDetails = new Scanner(Detailsfile);
			while (readDetails.hasNext()) {
				line = readDetails.nextLine();
				if (line.contains("Last Terrain: ")) {
					line = line.replace("Last Terrain: ", "");
					lastTerrain = line;
				} else
					lastTerrain = "~";
			}
			readMap = new Scanner(mapFile);
			while (readMap.hasNext()) {
				mapText.append(readMap.nextLine() + "\n");
				mapL++;
			}
			if(mapText.charAt(mapText.length() - 1) == '\n')
				mapText.deleteCharAt(mapText.length() - 1);
			textRow = mapText.toString().split("\n");
			mapW = textRow[0].split(" ").length;
			map = new String[mapL][mapW];
			for (int i = 0; i < map.length; i++) {
				map[i] = textRow[i].split(" ");
			}
			
			if(type==1) {
				eMap = new String[mapL][mapW];
				for(int i=0;i<mapL;i++)
					for(int j=0;j<mapW;j++) {
						switch (map[i][j]) {
						case "#":
							eMap[i][j] = "🌲 ";
							break;
						case "~":
							eMap[i][j] = "🌾 ";
							break;
						case "$":
							eMap[i][j] = "🏘 ";
							break;
						case "P":
							eMap[i][j] = "🧝‍♂️ ";
							playerPos[0] = i;
							playerPos[1] = j;
							break;
						}
					}
			}
		} catch (FileNotFoundException | StringIndexOutOfBoundsException e) {}
		String[] mapRaw = new String[map.length];
		for (int i = 0; i < map.length; i++) {
			if(type==1)
				mapRaw[i] = Arrays.toString(eMap[i]);
			else 
				mapRaw[i] = Arrays.toString(map[i]);
		}
		return mapRaw;
	}

	@SuppressWarnings("resource")
	public static boolean movePlayer(char direction) {
		
		StringBuilder text = new StringBuilder();
		boolean notMoved = true;
		PrintWriter write,writeDetails;
		try {
			write = new PrintWriter("./Files/AfekaLands/Map.txt");
			writeDetails = new PrintWriter("./Files/AfekaLands/GameDetails.txt");

			for (int i = 0; i < map.length; i++)
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j].contains("P") && notMoved) {
						if((direction == 'w' && i==0) || (direction == 'a' && j==0) || (direction == 's' && i+1==map.length) || (direction == 'd' && j+1==map[0].length))
							return false;
						
						switch (direction) {
							case 'd':
								map[i][j] = lastTerrain;
								lastTerrain = map[i][j + 1];
								map[i][j + 1] = "P";
								playerPos[0] = i;
								playerPos[1] = j + 1;
								break;
							case 'a':
								map[i][j] = lastTerrain;
								lastTerrain = map[i][j - 1];
								map[i][j - 1] = "P";
								playerPos[0] = i;
								playerPos[1] = j - 1;
								break;
							case 'w':
								map[i][j] = lastTerrain;
								lastTerrain = map[i - 1][j];
								map[i - 1][j] = "P";
								playerPos[0] = i - 1;
								playerPos[1] = j;
								break;
							case 's':
								map[i][j] = lastTerrain;
								lastTerrain = map[i + 1][j];
								map[i + 1][j] = "P";
								playerPos[0] = i + 1;
								playerPos[1] = j;
								break;
							}
						notMoved = false;
					}
				}

			writeDetails.append("Last Terrain: " + lastTerrain);	
			writeDetails.flush();
			writeDetails.close();
			
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					text.append(map[i][j] + " ");
				}
				text.append("\n");
			}
			text.deleteCharAt(text.length() - 1);
			write.append(text);
			write.flush();
			write.close();
		} catch (IndexOutOfBoundsException e) {
			return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-----------------------");
		MapGen.moves++;
		return true;
	}
}
