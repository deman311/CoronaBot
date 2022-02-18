package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public abstract class Leaderboards {

	public static String checkWrite() {
		Scanner readLB;
		StringBuilder sb = new StringBuilder();
		try {
			readLB = new Scanner(new File(AfekaLandsController.FS_PATH + "/AfekaLands/Leaderboards.txt"));
			String[] lbs, level;

			while (readLB.hasNext())
				sb.append(readLB.nextLine() + "\n");
			if (!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length() - 1);
			System.out.println(sb.toString());

			lbs = sb.toString().split("#");
			level = lbs[0].split("\n");

			for (int i = 1; i < 4; i++) {
				try {
					String temp1, temp2;

					if (level[i].contains(
							AfekaLandsController.player1.getName() + " [L]" + AfekaLandsController.player1.getLevel()))
						break;

					if ((Integer.parseInt(level[i].substring(level[i].indexOf("[") + 3)) < AfekaLandsController.player1
							.getLevel())) {
						temp1 = level[i];
						level[i] = AfekaLandsController.player1.getName() + " [L]"
								+ AfekaLandsController.player1.getLevel();
						for (int j = i; j < 4; j++)
							if (j < 3) {
								temp2 = level[j + 1];
								level[j + 1] = temp1;
								temp1 = temp2;
							}
						break;
					}
				} catch (NullPointerException e) {
					if (i == 1)
						System.err.println("[INFO] No player created yet.");
				}
			}

			PrintWriter write = new PrintWriter(new FileWriter(AfekaLandsController.FS_PATH + "/AfekaLands/Leaderboards.txt"));
			sb.delete(0, sb.length());

			int counter = 0;
			for (String slot : level) {
				write.append(slot + "\n");
				if (counter != 0)
					sb.append(counter + ". " + slot.replace("[L]", "| Level: ") + "\n");
				counter++;
			}

			write.flush();
			write.close();
			readLB.close();

			if (!sb.toString().isEmpty())
				sb.deleteCharAt(sb.length() - 1);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

}
