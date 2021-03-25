package MyBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class PirateBot {

	static Thread seek = new Thread(new Runnable() {

		@Override
		public void run() {
			String[] gameLib = new String[9];
			String[] updatedLib = new String[18];
			while (true) {
				try {
					read(gameLib);
					Document doc = Jsoup.connect("https://www.skidrowreloaded.com/").get();
					int i = 0;
					for (Element e : doc.getElementsByClass("post")) {
						if (i > 19)
							break;
						updatedLib[i] = e.getElementsByClass("meta right").select("a").attr("title");
						updatedLib[i + 1] = e.select("a").attr("href");
						i += 2;
					}
					boolean exist;
					for (int r = 0; r < 18; r += 2) {
						exist = false;
						for (String game2 : gameLib)
							if (updatedLib[r].contains(game2))
								exist = true;
						if (!exist)
							CoronaBot.jda.awaitReady().getTextChannelById("750616819286671400")
									.sendMessage("Pirated!🏴‍☠️\n" + updatedLib[r] + "\n" + updatedLib[r + 1]).queue();
					}
					write(updatedLib);
					Thread.sleep(600000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e1) {
					System.err.println("[GameSeeker] Thread stopped!");
					return;
				}
			}
		}

	});

	public static void start() {
		seek.start();
	}

	public static void read(String[] lib) {
		try {
			Scanner read = new Scanner(new File("./Files/GameTitles.txt"));
			int i = 0;
			String line;
			while (read.hasNext()) {
				line = read.nextLine();
				if (!line.isBlank())
					lib[i] = line;
				i++;
			}
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write(String[] lib) {
		try {
			FileWriter writer = new FileWriter(new File("./Files/GameTitles.txt"));
			for (int i = 0; i < lib.length; i += 2)
				writer.append(lib[i] + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
