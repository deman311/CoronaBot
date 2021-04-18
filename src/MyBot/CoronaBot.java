package MyBot;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import AfekaLands.AfekaLandsController;
import AfekaLands.Character;
import AfekaLands.Enemy;
import AfekaLands.Leaderboards;
import AfekaLands.MapGen2;
import AfekaLands.Shop;
import AfekaLands.Skill;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;

public abstract class CoronaBot {

	// CORONABOT VERSION 0.3.9.2

	public static JDA jda;
	public static String prefix = "!";
	private static Map<String, User> users = new HashMap<String, User>();

	public static int w = 0;
	public static int BC = 0;
	public static String zombies = null;
	public static String[][] Deck = new String[4][13];
	public static int deckCount = 52;
	public static String[][] pokerPlayers = new String[10][2];
	public static int currentPlayers = 0;
	public static boolean hasPlayer = false, hasEnemy = false;
	public static boolean grossEnable = false;
	public static String[] lecNames;

	public static Character player1;
	public static Enemy enemy1;
	public static ArrayList<Shop> shops = new ArrayList<Shop>();

	public static void main(String[] args) throws LoginException {
		jda = JDABuilder.createDefault("Njg5NDU4MzcxNjM1ODM5MDU3.XnIFqQ.tWfb9btyVCCDUqxoMHJ5UWwZj8w").build();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.getPresence().setActivity(Activity.playing("AfekaLands"));
		jda.addEventListener(new Responds());

		// AFEKA LANDS RELATED -------------------------------

		jda.addEventListener(new AfekaLandsController());
		Poker.NewDeck();
		Skill.readAll();
		checkPlayer();
		checkEnemy();
		if (hasPlayer)
			MapGen2.readMap();
		Leaderboards.checkWrite();
		PirateBot.start();

		// ---------------------------------------------------

		getAllUsers();
		if (!LocalDate.now().getDayOfWeek().name().contentEquals("FRIDAY")
				|| !LocalDate.now().getDayOfWeek().name().contentEquals("SATURDAY"))
			sendDailyLinks();
	}

	public static void vrUpdates() {
		try {
			Scanner read = new Scanner(new File("./Files/VRTitles.txt"));
			ArrayList<String> lastTitles = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			while (read.hasNext()) {
				sb.append(read.nextLine() + "\n");
				sb.append(read.nextLine());
				sb.append(read.nextLine());
				lastTitles.add(sb.toString());
				sb = new StringBuilder();
			}
			FileWriter write = new FileWriter(new File("./Files/VRTitles.txt"));
			XWPFDocument wordFile = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File("C://Users/Dmitry Gribovsky/Desktop/VRUpdates.docx"));
			Document data = null;
			Elements rows = null;
			Iterator<Element> games = null;

			wordFile.createHeader(HeaderFooterType.DEFAULT).createParagraph().createRun().setText("© Dmitry Gribovsky");
			XWPFRun run = wordFile.createParagraph().createRun();
			run.setBold(true);
			run.addTab();
			run.addTab();
			run.addTab();
			run.addTab();
			run.setFontSize(20);
			run.setText("Pirated VR Games");
			run = wordFile.getParagraphs().get(0).createRun();
			run.setBold(false);

			for (int j = 0; j < 3; j++) {
				if (j == 0)
					data = Jsoup.connect("https://cs.rin.ru/forum/viewforum.php?f=10").get();
				else if (j == 1)
					data = Jsoup.connect("https://cs.rin.ru/forum/viewforum.php?f=10&start=100").get();
				else if (j == 2)
					data = Jsoup.connect("https://cs.rin.ru/forum/viewforum.php?f=10&start=200").get();

				rows = data.getElementsByClass("topictitle");
				games = rows.iterator();
				int i = 0;

				while (games.hasNext()) {
					if (i < 10) {
						games.next();
						i++;
					} else {
						Element game = games.next();
						String link;
						if (game.text().contains("VR Only") || game.text().contains("VR Optional")) {
							link = "https://cs.rin.ru/forum/" + game.parents().select("a").attr("href").substring(2);
							sb.append(game.text() + "\n" + link + "\n\n");

							run.addCarriageReturn();
							run.addCarriageReturn();

							boolean found = false;
							for (String title : lastTitles)
								if (title.contains(game.text()))
									found = true;

							if (found)
								run.setText("☠ " + game.text().replace("[Info] ", "") + " --- ");
							else {
								XWPFRun run2 = wordFile.getParagraphs().get(0).createRun();
								run2.setColor("F71616");
								run2.setBold(true);
								run2.setText("☠ " + game.text().replace("[Info] ", "") + " --- ");
								run = wordFile.getParagraphs().get(0).createRun();
							}
							XWPFHyperlinkRun hlink = createHyperlinkRun(wordFile.getParagraphs().get(0), link);
							hlink.setText("Forum Link");
							hlink.setColor("0000FF");
							hlink.setUnderline(UnderlinePatterns.SINGLE);
							run = wordFile.getParagraphs().get(0).createRun();
						}
					}
				}
			}
			write.append(sb.toString());
			wordFile.write(out);
			wordFile.close();
			read.close();
			write.flush();
			write.close();
			System.err.println("[VR Seeker] File Updated.");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
		String rId = paragraph.getDocument().getPackagePart()
				.addExternalRelationship(uri, XWPFRelation.HYPERLINK.getRelation()).getId();

		org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink cthyperLink = paragraph.getCTP()
				.addNewHyperlink();
		cthyperLink.setId(rId);
		cthyperLink.addNewR();

		return new XWPFHyperlinkRun((org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink) cthyperLink,
				cthyperLink.getRArray(0), paragraph);
	}

	public static void sendDailyLinks() {
		Lectures.readLastRead();
		if (!Lectures.lastReadDate.contentEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))) {
			for (Entry<String, User> userEntry : users.entrySet()) {
				EmbedBuilder info = new EmbedBuilder();
				String day = LocalDate.now().getDayOfWeek().name().toLowerCase();
				String body = Lectures.getText(userEntry.getKey(), LocalDate.now().getDayOfWeek().name());
				if (body == null || body.isEmpty())
					break;
				day = day.replaceFirst("" + day.charAt(0), ("" + day.charAt(0)).toUpperCase());
				info.setTitle(day + " Links 🖥");
				info.setColor(Color.blue);
				info.setDescription("Good morning " + userEntry.getKey() + " ♥😁" + "\nHere are your daily links:\n\n");
				info.appendDescription(body);
				info.appendDescription("\n\nPlease tell Dima if you need anything else. 🙇‍♂️");
				users.get(userEntry.getKey()).openPrivateChannel().complete().sendMessage(info.build()).queue();

			}
			Lectures.writeLastRead();
		}
	}

	public static Map<String, User> getAllUsers() {
		users.clear();
		users.put("Dima", jda.retrieveUserById("633662715792982026").complete());
		users.put("Timor", jda.retrieveUserById("687978431299715148").complete());
		users.put("Alon", jda.retrieveUserById("687980623419408445").complete());
		users.put("Elad", jda.retrieveUserById("688825751818207362").complete());
		// users.put("Matan", jda.retrieveUserById("381344453966954517").complete());
		return users;
	}

	public static String checkPlayer() {
		File file = new File("./Files/AfekaLands/Character.txt");
		String name = null, CREATOR = null;
		Scanner readFile;
		try {
			readFile = new Scanner(file);
			name = readFile.nextLine().replace("NAME: ", "");
			CREATOR = readFile.nextLine().replace("CREATOR: ", "");
			hasPlayer = true;
			player1 = new Character(name, CREATOR);
			player1.setLevel(Integer.parseInt(readFile.nextLine().replace("LEVEL: ", "")));
			player1.setHP(Integer.parseInt(readFile.nextLine().replace("HP: ", "")));
			player1.setENG(Integer.parseInt(readFile.nextLine().replace("ENG: ", "")));
			player1.setEXP(Integer.parseInt(readFile.nextLine().replace("EXP: ", "")));
			for (String skill : readFile.nextLine().replace("SKILLS: ", "").split(", "))
				if (!skill.isEmpty() && skill != null)
					try {
						player1.addSkill(Skill.findSkillByName(skill));
					} catch (NullPointerException e) {
						e.printStackTrace();
						System.out.println("NO SUCH SKILL NAME FOUND!");
					}
			for (String item : readFile.nextLine().replace("EQP: ", "").split(", "))
				if (!item.isEmpty() && item != null) {
					if (item.contains("[w]")) {
						player1.setEQPw(item.replace("[w]", ""));
						player1.addDamage(player1.getWeapon().getDamage());
					} else if (item.contains("[a]"))
						player1.setEQPa(item.replace("[a]", ""));
				}
			for (String item : readFile.nextLine().replace("INV: ", "").split(", ")) {
				String[] two = new String[2];
				two = item.split("#");
				if (!item.isEmpty() && item != null)
					player1.putIn(two[0], Integer.parseInt(two[1]));
			}
			player1.updateStatus();
			System.out.println("CURRENT PLAYER NAME: " + player1.getName());
			readFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Character.txt not found!");
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			hasPlayer = false;
			return null;
		}
		return name;
	}

	public static void checkEnemy() {
		File file = new File("./Files/AfekaLands/Enemy/Enemy.txt");
		try {
			Scanner readFile = new Scanner(file);
			if (readFile.nextLine() != null)
				hasEnemy = true;
			readFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Character.txt not found!");
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			hasEnemy = false;
		}
	}
}