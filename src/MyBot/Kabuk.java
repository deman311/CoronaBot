package MyBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

abstract class Kabuk {

	public static String getWord() {
		File file = new File(CoronaBot.FS_PATH + "/Quotes/Kabuk.txt");
		String word = null;
		String[] text = null;
		StringBuilder sb = new StringBuilder();
		String line;
		Random rand = new Random();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			while ((line = reader.readLine()) != null)
				sb.append(line + "\n");

			reader.close();
			text = sb.toString().split("\n");
		} catch (IOException e) {
			System.out.println(System.getProperty("Could not read the file!"));
			e.printStackTrace();
		}
		word = text[rand.nextInt(text.length)];
		return word;
	}
}
