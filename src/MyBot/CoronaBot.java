package MyBot;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import AfekaLands.AfekaLandsController;
import AfekaLands.Leaderboards;
import AfekaLands.MapGen2;
import AfekaLands.Skill;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;

public abstract class CoronaBot {

	// CORONABOT VERSION 0.3.9.5

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
	public static boolean grossEnable = false;
	public static String[] lecNames;

	public static void main(String[] args) throws LoginException {
		jda = JDABuilder.createDefault("Njg5NDU4MzcxNjM1ODM5MDU3.XnIFqQ.tWfb9btyVCCDUqxoMHJ5UWwZj8w").build();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.getPresence().setActivity(Activity.playing("AfekaLands"));
		jda.addEventListener(new Responds());

		// AFEKA LANDS RELATED -------------------------------

		jda.addEventListener(new AfekaLandsController());
		Poker.NewDeck();
		Skill.readAll();
		AfekaLandsController.checkPlayer();
		AfekaLandsController.checkEnemy();
		if (AfekaLandsController.hasPlayer())
			MapGen2.readMap();
		Leaderboards.checkWrite();
		PirateBot.start();

		// ---------------------------------------------------

		getAllUsers();

		// SEND DAILY LINKS
//		if (!LocalDate.now().getDayOfWeek().name().contentEquals("FRIDAY")
//				|| !LocalDate.now().getDayOfWeek().name().contentEquals("SATURDAY"))
//			sendDailyLinks();
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
			List<DomElement> rows = null;
			Iterator<DomElement> games = null;

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

			// DYNAMIC GENERATION - HtmlUnit
			WebClient wc = new WebClient(BrowserVersion.CHROME);
			wc.getOptions().setJavaScriptEnabled(true);
			wc.getOptions().setThrowExceptionOnScriptError(false);
			wc.waitForBackgroundJavaScript(5000);
			HtmlPage page = null;

			for (int j = 0; j < 3; j++) {
				if (j == 0)
					page = wc.getPage("https://cs.rin.ru/forum/viewforum.php?f=10");
				else if (j == 1)
					page = wc.getPage("https://cs.rin.ru/forum/viewforum.php?f=10&start=100");
				else if (j == 2)
					page = wc.getPage("https://cs.rin.ru/forum/viewforum.php?f=10&start=200");

				rows = page.getByXPath("//a[@class='topictitle']");
				games = rows.iterator();
				int i = 0;

				while (games.hasNext()) {
					if (i < 10) {
						games.next();
						i++;
					} else {
						DomElement game = games.next();
						String link;
						if (game.getTextContent().contains("VR Only")
								|| game.getTextContent().contains("VR Optional")) {
							link = "https://cs.rin.ru/forum/" + game.getAttribute("href").substring(2);
							sb.append(game.getTextContent() + "\n" + link + "\n\n");

							run.addCarriageReturn();
							run.addCarriageReturn();

							boolean found = false;
							for (String title : lastTitles)
								if (title.contains(game.getTextContent()))
									found = true;

							if (found)
								run.setText("☠ " + game.getTextContent().replace("[Info] ", "") + " --- ");
							else {
								XWPFRun run2 = wordFile.getParagraphs().get(0).createRun();
								run2.setColor("F71616");
								run2.setBold(true);
								run2.setText("☠ " + game.getTextContent().replace("[Info] ", "") + " --- ");
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
			wc.close();
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
}