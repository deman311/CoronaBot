package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public abstract class MapGen2 implements Runnable {
	private static Random rand = new Random();
	private static String mapRAW[][][];
	private static int[] playerPOS = new int[2];
	public static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>();
	private static int localEnemies = 0;
	private static boolean isTackle = false;

	public static void createMap() {
		mapRAW = new String[2][rand.nextInt(300) + 500][rand.nextInt(300) + 500];
		allEnemies.clear();
		localEnemies = 0;

		for (int i = 0; i < mapRAW[0].length; i++)
			for (int j = 0; j < mapRAW[0][i].length; j++) {
				int c = rand.nextInt(100);
				try {
					if ((mapRAW[0][i][j - 1].contains("~") || mapRAW[0][i - 1][j].contains("~")) && c < 90)
						c = rand.nextInt(2) + 10;
					else if ((mapRAW[0][i][j - 1].contains("^") || mapRAW[0][i - 1][j].contains("^")) && c <= 30)
						c = 0;
				} catch (Exception e) {
				}
				if (c >= 0 && c < 5)
					mapRAW[0][i][j] = "^";
				else if (c >= 5 && c < 11)
					mapRAW[0][i][j] = "~";
				else if (c >= 11 && c < 14)
					mapRAW[0][i][j] = "!";
				else if (c >= 14 && c < 48)
					mapRAW[0][i][j] = "*";
				else if (c >= 48 && c < 99)
					mapRAW[0][i][j] = "#";
				else if (c >= 99 && c < 100)
					mapRAW[0][i][j] = "$";
			}
		playerPOS[0] = rand.nextInt(mapRAW[0].length);
		playerPOS[1] = rand.nextInt(mapRAW[0][0].length);
		mapRAW[1][playerPOS[0]][playerPOS[1]] = "P";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mapRAW[0].length; i++) {
			for (int j = 0; j < mapRAW[0][i].length; j++) {
				if (j != mapRAW[0][i].length - 1)
					sb.append(mapRAW[0][i][j] + " ");
				else
					sb.append(mapRAW[0][i][j]);
			}
			sb.append("\n");
		}

		writeMap(sb.toString());
	}

	public static boolean putAt(int Y, int X, String symbol) {
		if (mapRAW[0][Y][X].contains("~") || mapRAW[0][Y][X].contains("$"))
			return false;

		if (mapRAW[1][Y][X] == null) {
			mapRAW[1][Y][X] = symbol;
			return true;
		} else if (mapRAW[1][Y][X].contains("P"))
			isTackle = true;

		return false;
	}

	public static boolean removeFrom(int Y, int X) {
		if (mapRAW[1][Y][X] != null) {
			mapRAW[1][Y][X] = null;
			return true;
		}

		return false;
	}

	public static void genEnemies(int amount) {
		for (int i = 0; i < amount; i++) {
			Enemy e1 = new Enemy();
			int randx, randy;

			do {
				randx = rand.nextInt(9) + 2;
				if (rand.nextBoolean())
					randx *= -1;
				randy = rand.nextInt(9) + 2;
				if (rand.nextBoolean())
					randy *= -1;
			} while (!putAt(playerPOS[0] + randy, playerPOS[1] + randx, "E"));

			e1.setPOS(playerPOS[0] + randy, playerPOS[1] + randx);
			allEnemies.add(e1);
		}
	}

	public static void moveTurn() {

		for (Enemy e : allEnemies)
			if (e.move())
				AfekaLandsController.enemy1 = e;
	}

	public static String localMap() {
		StringBuilder sb = new StringBuilder();
		int lastrow = 0;
		localEnemies = 0;
		for (int i = playerPOS[0] - 20; i < playerPOS[0] + 20; i++)
			for (int j = playerPOS[1] - 20; j < playerPOS[1] + 20; j++)
				if (i >= 0 && j >= 0 && i < mapRAW[0].length && j < mapRAW[0][0].length) {
					if (i >= playerPOS[0] - 4 && i <= playerPOS[0] + 4 && j >= playerPOS[1] - 4
							&& j <= playerPOS[1] + 4) {
						if (i != lastrow)
							sb.append("\n");
						if (i == playerPOS[0] && j == playerPOS[1])
							if (j < playerPOS[1] + 4)
								sb.append(mapRAW[1][i][j] + " ");
							else
								sb.append(mapRAW[1][i][j]);
						else if (j < playerPOS[1] + 4)
							if (mapRAW[1][i][j] == null)
								sb.append(mapRAW[0][i][j] + " ");
							else
								sb.append(mapRAW[1][i][j] + " ");
						else if (mapRAW[1][i][j] == null)
							sb.append(mapRAW[0][i][j]);
						else
							sb.append(mapRAW[1][i][j]);
						lastrow = i;
					}
					if (i >= playerPOS[0] - 10 && i <= playerPOS[0] + 10 && j >= playerPOS[1] - 10
							&& j <= playerPOS[1] + 10) {
						if (mapRAW[1][i][j] != null && mapRAW[1][i][j].contains("E"))
							localEnemies++;
					} else if (mapRAW[1][i][j] != null && mapRAW[1][i][j].contains("E") && !allEnemies.isEmpty())
						for (Enemy e : allEnemies)
							if (e.getPOS()[0] == i && e.getPOS()[1] == j) {
								allEnemies.remove(e);
								e = null;
								break;
							}
				}
		return Convert(sb.toString());
	}

	public static String Convert(String rawMAP) {
		String finalMAP = rawMAP;
		finalMAP = finalMAP.replace("#", "🌳").replace("!", "🌾").replace("~", "🟦").replace("^", "⛰")
				.replace("P", "🧝‍♂️").replace("$", "🏘").replace("E", "👹").replace("*", "🌲");
		return finalMAP;
	}

	public static void writeMap(String rawMAP) {
		try {
			FileWriter map = new FileWriter(AfekaLandsController.FS_PATH + "/AfekaLands/Map2.txt");
			PrintWriter writeMap = new PrintWriter(map);

			String[] rows = rawMAP.split("\n");
			for (String row : rows)
				writeMap.append(row + "\n");

			writePOS();

			writeMap.flush();
			writeMap.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writePOS() {
		try {
			FileWriter details = new FileWriter(AfekaLandsController.FS_PATH + "/AfekaLands/Map2Details.txt");
			PrintWriter writeDetails = new PrintWriter(details);

			writeDetails.append("Map Size: " + mapRAW[0].length + " " + mapRAW[0][0].length);
			writeDetails.append("\nPlayer Pos: " + playerPOS[0] + " " + playerPOS[1]);

			writeDetails.flush();
			writeDetails.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getPOS() {
		return "Player POS: " + playerPOS[0] + " " + playerPOS[1];
	}

	public static String currentTile() {
		return mapRAW[0][playerPOS[0]][playerPOS[1]].replace(" ", "");
	}

	public static void readMap() {
		try {
			Scanner mapReader = new Scanner(new File(AfekaLandsController.FS_PATH + "/AfekaLands/Map2.txt"));
			Scanner detailsReader = new Scanner(new File(AfekaLandsController.FS_PATH + "/AfekaLands/Map2Details.txt"));
			String[] mapSize = detailsReader.nextLine().replace("Map Size: ", "").split(" ");
			String[] playerPos = detailsReader.nextLine().replace("Player Pos: ", "").split(" ");

			mapRAW = new String[2][Integer.parseInt(mapSize[0])][Integer.parseInt(mapSize[1])];
			mapRAW[1][Integer.parseInt(playerPos[0])][Integer.parseInt(playerPos[1])] = "P";
			playerPOS[0] = Integer.parseInt(playerPos[0]);
			playerPOS[1] = Integer.parseInt(playerPos[1]);

			for (int i = 0; i < mapRAW[0].length; i++)
				mapRAW[0][i] = mapReader.nextLine().split(" ");

			for (int i = 0; i < mapRAW[0].length; i++)
				for (int j = 0; j < mapRAW[0][i].length; j++) {
					if (mapRAW[0][i][j].contains("$"))
						AfekaLandsController.shops.add(new Shop(i, j));
				}

			mapReader.close();
			detailsReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void movePlayer(char direction) {
		if ((direction == 'w') || (direction == 'a') || (direction == 's') || (direction == 'd')) {
			int lastX, lastY;
			lastX = playerPOS[1];
			lastY = playerPOS[0];
			try {
				mapRAW[1][playerPOS[0]][playerPOS[1]] = null;
				switch (direction) {
				case 'w':
					if (playerPOS[0] - 1 >= 0)
						--playerPOS[0];
					break;
				case 's':
					if (playerPOS[0] + 1 <= mapRAW[0].length)
						++playerPOS[0];
					break;
				case 'd':
					if (playerPOS[1] + 1 <= mapRAW[0][0].length)
						++playerPOS[1];
					break;
				case 'a':
					if (playerPOS[1] - 1 >= 0)
						--playerPOS[1];
					break;
				}
				if (mapRAW[0][playerPOS[0]][playerPOS[1]].contains("~")) {
					playerPOS[0] = lastY;
					playerPOS[1] = lastX;
				}

				if (mapRAW[1][playerPOS[0]][playerPOS[1]] != null
						&& mapRAW[1][playerPOS[0]][playerPOS[1]].contains("E")) {
					isTackle = true;
					for (Enemy e : allEnemies)
						if (e.getPOS()[0] == playerPOS[0] && e.getPOS()[1] == playerPOS[1]) {
							AfekaLandsController.enemy1 = e;
							allEnemies.remove(e);
							break;
						}
				}

				mapRAW[1][playerPOS[0]][playerPOS[1]] = "P";
				writePOS();
			} catch (ArrayIndexOutOfBoundsException e) {
				playerPOS[0] = lastY;
				playerPOS[1] = lastX;
				mapRAW[1][playerPOS[0]][playerPOS[1]] = "P";
				System.err.println("EDGE OF MAP");
			}
		}
	}

	public static int getLocalEnemies() {
		return localEnemies;
	}

	public static boolean checkTackle() {
		return isTackle;
	}

	public static void setTackle(boolean state) {
		isTackle = state;
	}

	public static int getMapHeight() {
		return mapRAW[0].length;
	}

	public static int getMapWidth() {
		return mapRAW[0][0].length;
	}
}
