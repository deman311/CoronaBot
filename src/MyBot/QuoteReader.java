package MyBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

abstract class QuoteReader {

	public static String fileread(String name) {
		String context = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(CoronaBot.FS_PATH + "/Quotes/" + getName(name) + "Quotes.txt"), "UTF8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (context == null)
					context = line + "\n";
				else
					context = context + line + "\n";
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return context;
	}

	public static void filewrite(String name, String line) {
		String emoji = null;
		try {
			File file = new File(CoronaBot.FS_PATH + "/Quotes" + name + "Quotes.txt");
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			writer.newLine();

			if (name == "efrat")
				emoji = "<:efrat:709663657688301568>";
			else if (name == "boris")
				emoji = "<:boris:710386821925699614>";
			else if (name == "victor")
				emoji = ":triangular_ruler:";
			else if (name == "yarden")
				emoji = ":zany_face:";
			else if (name == "vlad")
				emoji = ":straight_ruler:";

			writer.append(emoji + " ").append(line).append(" " + emoji);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not write to file!");
			e.printStackTrace();
		}
	}

	public static String getName(String name) {
		switch (name) {
		case "ויקטור":
			name = "victor";
			break;
		case "אפרת":
			name = "efrat";
			break;
		case "בוריס":
			name = "boris";
			break;
		case "ירדן":
			name = "yarden";
			break;
		case "ולד":
			name = "vlad";
			break;
		}

		return name;
	}
}