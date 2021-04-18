package MyBot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Responds extends ListenerAdapter {

	private static Random rand = new Random();
	private String BAN;
	private static int lastTimeVladSpam, lastTimeSpam;
	private String carAnswer, carFormat;
	private static boolean repeatCatcher;
	private static GuildMessageReceivedEvent event;

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Responds.event = event;
		repeatCatcher = false;
		boolean calledBot = false;

//		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String[] quoteable = { "בוריס", "אפרת", "ויקטור", "ירדן", "ולד" };

		EmbedBuilder info = new EmbedBuilder();
		Document doc = null;

		String role = event.getMember().getRoles().toString();
		Member sender = event.getMember();
		boolean isAdmin = event.getMember().getId().contains("633662715792982026");
		String messageID = event.getMessageId();
		String messageRaw = "";
		if (!event.getAuthor().isBot())
			messageRaw = event.getMessage().getContentRaw();

//========================================================================================== MAIN LOGIC =====================================================================================================

		// REPLY TO MESSAGES --------------------------------------------

		if (!event.getAuthor().isBot())
			try {
				replyTo("!matan", "if(opinion==a)\n   Matan.setOpinion(!a);");
				replyTo("https://tenor.com/view/ejaculation-explosion-cumming-fall-ejaculating-gif-12807230",
						"https://tenor.com/view/spongebob-daddis-cummies-mayonnaise-spongebob-movie-gif-15569327");
				replyTo("!roll", "You rolled: " + (rand.nextInt(6) + 1) + "!");
				replyTo("בן זונה", GetText.GetRandomText("בן זונה", "ResToCall"), 3);
				replyTo("בת זונה", GetText.GetRandomText("בת זונה", "ResToCall"), 3);
				replyTo("לא הבנתי", "https://baumann.co.il/", 5);
				replyTo("!rickroll", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
				replyTo("!ping", "pong!");
				replyTo("!pong", "ping!");
				replyTo("!joke", Jokes.getJoke());
				replyTo("Bot", "Did someone mention me?");
				replyTo("כוסעמק", "מישהו קילל? 🤐", 2);
				replyTo("שרמוטה", "מישהו קילל? 🤐", 2);
				replyTo("!drive",
						"https://drive.google.com/drive/folders/17pif5n15MCS1t5U3mAHu4VsoBjC0ppMW?usp=sharing");

				if (time.getHour() > 6 && time.getHour() < 10)
					replyTo("בוקר", GetText.GetRandomText("BokerTov"));

				String[] names = { "דימה", "טימור", "אלון", "אלעד", "מתן", "דמיטרי", "בן הגבר" };
				for (String name : names)
					if (messageRaw.contains(name) && event.getMessage().getMentionedMembers().isEmpty()) {
						String res = null;
						switch (name) {
						case "דימה":
							if (messageRaw.contains("קדימה"))
								return;
							res = "<@633662715792982026>";
							break;
						case "טימור":
							res = "<@687978431299715148>";
							break;
						case "אלון":
							if (messageRaw.contains("שאלון"))
								return;
							res = "<@687980623419408445>";
							break;
						case "אלעד":
							res = "<@688825751818207362>";
							break;
						case "מתן":
							res = "<@381344453966954517>";
							break;
						case "דמיטרי":
							res = "<@442013487221309471>";
							break;
						case "בן הגבר":
							res = "<@312878485230321665>";
							break;
						}
						event.getChannel().sendMessage(res).queue();
					}

				String[] namesBig = { "טימור", "אלעד", "מתן", "אירנה", "אלוהים", "חן", "אלון", "דימיטרי", "ויקטור",
						"טינץ" };
				for (String name : namesBig)
					if (args[0].contains(name))
						replyTo(name, GetText.GetRandomText(name, "ResToCall"), 2);

			} catch (Exception e) {
				return;
			}

		// --------------------------------------------------------------

		// ADMIN COMNMANDS

		if (event.getMember().getId().contains("633662715792982026")) {

			if (messageRaw.contains("!seekVR"))
				CoronaBot.vrUpdates();
		}

		// --------------------------------------------------------------

		if (event.getMessage().getContentRaw().split(" ")[0].contentEquals("!notifyRecievers:")
				&& event.getMember().isOwner()) {
			event.getMessage().delete().queue();
			for (Entry<String, User> userEntry : CoronaBot.getAllUsers().entrySet())
				userEntry.getValue().openPrivateChannel().complete()
						.sendMessage(
								"Hello " + userEntry.getKey() + "! ❤\n" + messageRaw.replace("!notifyRecievers: ", ""))
						.queue();
		}

		if (messageRaw.equalsIgnoreCase("!coin")) {
			if (rand.nextBoolean())
				event.getChannel().sendMessage("HEADS").queue();
			else
				event.getChannel().sendMessage("TAILS").queue();
		}

		if (messageRaw.indexOf("!spam ") == 0) {
			calledBot = true;
			if (time.getMinute() == lastTimeSpam) {
				event.getChannel().sendMessage("Can only spam once a minute.").queue();
				return;
			}
			event.getChannel().deleteMessageById(messageID).queue();
			lastTimeSpam = time.getMinute();
			StringBuilder spam = new StringBuilder();
			while (spam.length() < 1950) {
				spam.append(messageRaw.replace("!spam ", "") + " ");
				if (spam.length() > 1950)
					spam.delete(1950, spam.length());
			}

			event.getChannel().sendMessage(sender.getEffectiveName() + " SPAMS! \n" + spam.toString()).queue();
		}

		if (messageRaw.indexOf("!! ") == 0 && isAdmin) {
			calledBot = true;
			String temp = messageRaw.substring(3);
			event.getMessage().delete().queue();
			event.getChannel().sendMessage(temp).queue();
		}

		if (messageRaw.equalsIgnoreCase(CoronaBot.prefix + "spamVLAD")) {
			int minute = time.getMinute();
			if (lastTimeVladSpam != minute) {
				lastTimeVladSpam = minute;
				event.getChannel().sendMessage("https://tenor.com/view/bounce-gif-5353517").queue();
				event.getChannel().sendMessage("https://tenor.com/view/bounce-gif-5353517").queue();
				event.getChannel().sendMessage("https://tenor.com/view/bounce-gif-5353517").queue();
				event.getChannel().sendMessage("https://tenor.com/view/bounce-gif-5353517").queue();
				event.getChannel().sendMessage("https://tenor.com/view/bounce-gif-5353517").queue();
			} else
				event.getChannel().sendMessage("Only once in a minute.").queue();
		}

		if (messageRaw.contains("bot silence ") && isAdmin) {
			calledBot = true;
			BAN = messageRaw.replace("bot silence ", "");
			event.getChannel().sendMessage(BAN + " silenced.").queue();
		}

		if (messageRaw.contains("bot stop silence") && isAdmin) {
			calledBot = true;
			BAN = null;
			event.getChannel().sendMessage("Roger.").queue();
		}

		if (BAN != null && sender.getEffectiveName().contains(BAN)) {
			event.getChannel().deleteMessageById(messageID).queue();
		}

		if (messageRaw.contains("bot return float of")) {
			calledBot = true;
			String ae[] = new String[2];
			ae = messageRaw.replace("bot return float of ", "").split("/");
			float a = Float.parseFloat(ae[0]), b = Float.parseFloat(ae[1]);
			float c = a / b;
			event.getChannel().sendMessage("Float is: " + c).queue();
		} else if (messageRaw.contains("bot return double of")) {
			calledBot = true;
			String ae[] = new String[2];
			ae = messageRaw.replace("bot return double of ", "").split("/");
			double a = Double.parseDouble(ae[0]), b = Double.parseDouble(ae[1]);
			double c = a / b;
			event.getChannel().sendMessage("Double is: " + c).queue();
		} else if (messageRaw.contains("bot return short of")) {
			calledBot = true;
			String ae[] = new String[2];
			ae = messageRaw.replace("bot return short of ", "").split("/");
			short a = Short.parseShort(ae[0]), b = Short.parseShort(ae[1]);
			short c = (short) (a / b);
			event.getChannel().sendMessage("Short is: " + c).queue();
		}

		if (!CoronaBot.grossEnable && messageRaw.contains("tenor.com")
				&& (messageRaw.contains("vomit") || messageRaw.contains("puke") || messageRaw.contains("gross")
						|| messageRaw.contains("disgusting") || messageRaw.contains("poop")
						|| messageRaw.contains("spit"))) {
			calledBot = true;
			event.getMessage().delete().queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Janitor activated, gross GIF deleted.\nStop being so gross " + sender + ".")
					.queue();
		}

		if (messageRaw.contains("בוט מותר להיות מגעילים") && isAdmin) {
			calledBot = true;
			CoronaBot.grossEnable = true;
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("איכס, כן בוס.").queue();
		} else if (messageRaw.contains("בוט תביא את השרת") && isAdmin) {
			java.util.List<Message> mesHis = event.getChannel().getHistory().getRetrievedHistory();
			String MES;
			for (Message mes : mesHis) {
				MES = mes.getContentRaw();
				System.out.println(MES);
				if (MES.contains("tenor.com") && (MES.contains("vomit") || MES.contains("puke") || MES.contains("gross")
						|| MES.contains("disgusting") || MES.contains("poop"))) {
					mes.delete().queue();
				}
			}
			calledBot = true;
			CoronaBot.grossEnable = false;
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("הניקיון מתחיל בוס.").queue();
		}

		if (messageRaw.equalsIgnoreCase("!dog")) {
			calledBot = true;
			boolean flag;
			do {
				flag = false;
				try {
					event.getChannel().sendMessage(Jsoup.connect("https://random.dog/").get().getAllElements()
							.select("img").first().absUrl("src")).queue();
				} catch (IOException | NullPointerException e) {
					event.getChannel().sendMessage("He couldn't make it, searching for another...").queue();
					flag = true;
				}
			} while (flag);
		}

		if (messageRaw.equalsIgnoreCase("!carquiz")) {
			calledBot = true;
			boolean flag;
			String car, imgLink, link;
			int TRY = 0;
			do {
				TRY++;
				flag = false;
				try {
					doc = Jsoup.connect("https://www.automobile-catalog.com/simulation.php").get();
					link = doc.getElementsByAttributeValue("title", "photo of the moment").first().absUrl("src");
					link = link.replace("https://www.automobile-catalog.com/img/picto30/", "");
					link = "wikipedia " + link.substring(link.indexOf("/") + 1).replace(".JPG", "").replace(".jpg", "")
							.replace(".png", "");
					Document doc2 = Jsoup.connect("https://www.google.com/search?q=" + link).get();
					imgLink = doc2.getElementsByAttributeValueContaining("href", "wikipedia.org").first()
							.absUrl("href");
					car = Jsoup.connect(imgLink).get().select("table").first().select("img").first().absUrl("src");
					System.out.println(link);

					URL url = new URL(car);
					URLConnection uc = url.openConnection();
					OutputStream os = null;

					if (car.contains(".svg"))
						throw new NullPointerException();

					else if (car.contains(".jpg") || car.contains(".JPG")) {
						os = new FileOutputStream("./car.jpg");
						carFormat = "jpg";
					} else if (car.contains(".png")) {
						os = new FileOutputStream("./car.png");
						carFormat = "png";
					}

					byte[] b = new byte[2048];
					int length;

					while ((length = uc.getInputStream().read(b)) != -1)
						os.write(b, 0, length);

					os.close();

					BufferedImage carImg = ImageIO.read(new FileInputStream("./car.jpg"));
					BufferedImage carCropped = carImg.getSubimage((int) carImg.getWidth() / 3, 0,
							(int) carImg.getWidth() - (int) carImg.getWidth() / 3,
							(int) carImg.getHeight() - (int) carImg.getHeight() / 3);

					OutputStream co = new FileOutputStream("./carCropped." + carFormat);
					if (!ImageIO.write(carCropped, carFormat, co))
						System.out.println("DID NOT WRITE");
					event.getChannel().sendFile(new File("./carCropped." + carFormat)).queue();

					carAnswer = link.replace("wikipedia ", "");
					co.close();

				} catch (IOException e) {
					flag = true;
					e.printStackTrace();
				} catch (NullPointerException e) {
					flag = true;
					if (TRY < 2)
						event.getChannel().sendMessage("Could not get, Retrying...").queue();
				}
			} while (flag);
		}

		if (messageRaw.equalsIgnoreCase("!carAnswer") && carAnswer != null) {
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(carAnswer).queue();
			event.getChannel().sendFile(new File("./car." + carFormat)).queue();
		}

		if (messageRaw.equalsIgnoreCase("!carquiz2")) {
			try {
				event.getChannel().sendTyping().queue();
				doc = Jsoup.connect("https://www.generatormix.com/random-cars").get();
				FileOutputStream os = new FileOutputStream("./car.jpg");
				Elements es = doc.getElementsByClass("lazy thumbnail aspect-wide-contain");
				Element sCar = es.get(rand.nextInt(es.size()));
				String link = sCar.absUrl("data-src");
				InputStream linkConnection = new URL(link).openConnection().getInputStream();
				byte[] b = new byte[2048];
				int length;
				while ((length = linkConnection.read(b)) != -1)
					os.write(b, 0, length);

				os.close();

				BufferedImage carImg = ImageIO.read(new FileInputStream("./car.jpg"));
				BufferedImage carCropped = carImg.getSubimage((int) carImg.getWidth() / 3, 0,
						(int) carImg.getWidth() - (int) carImg.getWidth() / 3,
						(int) carImg.getHeight() - (int) carImg.getHeight() / 3);
				FileOutputStream co = new FileOutputStream("./carCropped.jpg");
				ImageIO.write(carCropped, "jpg", co);

				event.getChannel().sendFile(new File("./carCropped.jpg")).queue();

				carAnswer = sCar.attr("alt");
				carFormat = "jpg";

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (messageRaw.contains("!song: ")) {
			calledBot = true;
			messageRaw = messageRaw.replace("!song: ", "");
			messageRaw = messageRaw.replace(" ", "+");
			Element emts = null;
			String data = null;
			try {
				doc = Jsoup.connect("https://www.google.com/search?q=" + messageRaw).get();
				emts = doc.getElementById("search").select("a").first();
				if (!emts.toString().contains("https://www.youtube.com/watch") || emts == null)
					data = "Something went wrong, maybe try another name?";
				else
					data = emts.absUrl("href");
			} catch (IOException e) {
				System.out.println("YouTube link IOExxception!");
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("YouTube link returned Null!");
				data = "Something went wrong, maybe try another name?";
			}

			if (!data.contains("https://www.youtube.com/watch") || data == null) {
				data = "Could not find such a song in a YouTube format, maybe try another name?";
			}

			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(data).queue();
		}

		if (event.getMessage().getContentRaw().contains("אני שונא אותה")) {
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("גם אני.").queue();
		}

		if (event.getMessage().getContentRaw().contains("בוט ספר לי משהו")) {
			calledBot = true;
			boolean noluck = false;
			Elements pic = null;
			String picurl = null;
			String data = null;
			try {
				doc = Jsoup.connect(
						"https://he.wikipedia.org/wiki/%D7%9E%D7%99%D7%95%D7%97%D7%93:%D7%90%D7%A7%D7%A8%D7%90%D7%99")
						.get();
				data = doc.select("p").first().wholeText();
				pic = doc.getElementsByClass("infobox").select("img");
				if (pic.first().absUrl("alt").contains("קריאת טבלת מיון"))
					picurl = pic.get(1).absUrl("src");
				else
					picurl = pic.first().absUrl("src");

				if (picurl.contains(
						"https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/No_free_image_-_woman-he.svg/150px-No_free_image_-_woman-he.svg.png")
						|| picurl.contains(
								"https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Male_no_free_image_yet-he.svg/150px-Male_no_free_image_yet-he.svg.png")
						|| picurl.contains(
								"https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Blue_pencil_RTL.svg/15px-Blue_pencil_RTL.svg.png"))
					picurl = null;
			} catch (IOException e) {
				System.out.println("Could not get a WIKIPEDIA article!");
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Could not get a WIKI article! (Out of bounds)");
			} catch (NullPointerException e) {
				data = null;
				System.out.println("Could not get a WIKI article.");
			} catch (IllegalArgumentException e) {
				System.out.println("Could not get a WIKI article.");
			}
			if (data == null) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("לא מצאתי משהו מעניין, אולי תנסה שוב?").queue();
				noluck = true;
			}
			if (!noluck) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(data).queue();
				if (picurl != null && data != null)
					event.getChannel().sendMessage("[PIC] " + picurl + " [PIC]").queue();
			}
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "kabukME")
				|| event.getMessage().getContentRaw().contains("בוט תן לי משפט ירדן")
				|| event.getMessage().getContentRaw().contains("בוט תן משפט ירדן")) {
			calledBot = true;
			event.getChannel().sendMessage("ה" + Kabuk.getWord() + " של ה" + Kabuk.getWord() + " זה בעצם "
					+ Kabuk.getWord() + " וגם " + Kabuk.getWord() + " של ה" + Kabuk.getWord() + "?").queue();
		}

		if (event.getMessage().getContentRaw().contains("בוט תן לי ציטוטי")) {
			for (String name : quoteable) {
				calledBot = true;
				if (event.getMessage().getContentRaw().contains(name))
					event.getChannel().sendMessage(QuoteReader.fileread(name)).queue();
			}
		}

		if (event.getMessage().getContentRaw().contains("בוט תוסיף לציטוטי")) {
			String[] input;
			for (String name : quoteable) {
				if (event.getMessage().getContentRaw().contains(name))
					calledBot = true;
				input = event.getMessage().getContentRaw().split("בוט תוסיף לציטוטי " + name + ": ");
				for (int i = 0; i < input.length; i++) {
					if (i > 0)
						QuoteReader.filewrite(QuoteReader.getName(name), input[i]);
				}
			}
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "hack")) {
			String line = null;
			int a = rand.nextInt(3);
			switch (a) {
			case 0:
				line = "I've been compromized by a group named: SHABAK.\nRetreating.";
				break;
			case 1:
				line = "Should I censor the nude photos?";
				break;
			case 2:
				line = "Now is not a good time, I have 23,413 active russian hackers trying to shut me down.";
				break;
			case 4:
				line = "img";
			}
			if (line != "img") {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(line).queue();
			}
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "pokertable")) {
			String[] pokertable = new String[5];

			if (CoronaBot.deckCount < 10) {
				event.getChannel().sendMessage("You are out of cards! Reshuffle!").queue();
				return;
			}
			CoronaBot.deckCount -= 5;
			Poker.GetTable(pokertable);
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("POKER-TABLE:\n").queue();
			for (String cards : pokertable) {
				event.getChannel().sendMessage("\n" + cards).queue();
			}

			Poker.NewDeck();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "pokerhand")) {
			String card1 = null;
			String card2 = null;
			if (CoronaBot.deckCount < 10) {
				event.getChannel().sendMessage("You are out of cards! Reshuffle!").queue();
				return;
			}
			CoronaBot.deckCount -= 2;
			card1 = Poker.GetCard();
			card2 = Poker.GetCard();

			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Your cards are: " + Poker.cardNum(card1))
					.append(Poker.cardSign(card1) + " and " + Poker.cardNum(card2)).append(Poker.cardSign(card2))
					.queue();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "shuffle")) {
			Poker.NewDeck();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Unlike a human, took me 0.0003 to shuffle.\nShuffled!").queue();
		}

		if (event.getMessage().getContentRaw().contains("בוט מה עם הקורונה?")
				|| args[0].equalsIgnoreCase(CoronaBot.prefix + "zombies")) {
			calledBot = true;
			info.setTitle("🦠 COVID-19 Status: 🦠");
			info.setColor(0x24c73a);
			try {
				doc = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			String[] data = null;
			data = doc.getElementsByClass("maincounter-number").text().split("\\s+");

			info.setDescription("🟥 WORLDWIDE: 🟥\n\n" + "• Infected Cases: " + data[0] + "\n" + "• Related Deaths: "
					+ data[1] + "\n" + "• Recovered: " + data[2] + "\n");

			try {
				doc = Jsoup.connect("https://www.worldometers.info/coronavirus/country/israel/").get();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			doc.getElementsByClass("maincounter-number").eachText().toArray(data);
			info.appendDescription("\n\n🟦 ISRAEL: ⬜\n\n" + "• Infected Cases: " + data[0] + "\n" + "• Related Deaths: "
					+ data[1] + "\n" + "• Recovered: " + data[2]);

			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "jenks")) {
			if (!sender.getEffectiveName().equalsIgnoreCase("matan"))
				try {
					doc = Jsoup.connect("https://tenor.com/search/alisa-jenks-gifs").get();
					Elements emts = doc.getElementsByClass("Gif").select("img");
					String img;
					do {
						img = emts.get(rand.nextInt(emts.size())).absUrl("src");
					} while (img.contains(
							"https://tenor.com/assets/img/gif-maker-entrypoints/search-entrypoint-desktop.gif"));
					event.getChannel().deleteMessageById(messageID).queue();
					event.getChannel().sendMessage("Just for you " + sender + " ♥").queue();
					event.getChannel().sendMessage(img).queue();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else {
				event.getChannel().sendMessage("Just for you " + sender + " ♥").queue();
				event.getChannel().sendMessage("https://tenor.com/view/kots-straal-spugen-vomit-puke-gif-14891152")
						.queue();
			}
		}

		if (messageRaw.equalsIgnoreCase("!rando")) {
			event.getMessage().delete().queue();
			try {
				FileOutputStream os = new FileOutputStream("./rando.jpg");
				InputStream linkConnection = new URL("https://thispersondoesnotexist.com/image").openConnection()
						.getInputStream();
				byte[] b = new byte[2048];
				int length;
				while ((length = linkConnection.read(b)) != -1)
					os.write(b, 0, length);

				os.close();

				ImageIO.read(new FileInputStream("./rando.jpg"));

				int gender = rand.nextInt(2) + 2, rarity = rand.nextInt(2) + 2;
				doc = Jsoup.connect("http://random-name-generator.info/index.php?n=10&g=" + gender + "&st=" + rarity)
						.get();
				String name = doc.getElementsByClass("nameList").select("li").first().ownText();

				EmbedBuilder box = new EmbedBuilder();
				box.setTitle("Your Soul Mate");
				String gString;
				if (gender == 2) {
					box.setColor(Color.BLUE);
					gString = "Male";
				} else {
					box.setColor(Color.PINK);
					gString = "Female";
				}
				box.appendDescription("🧬 Gender: " + gString + "\n📝 Name: " + name + "\n🔢 Age: " + rand.nextInt(10));
				box.setImage("attachment://rando.jpg");

				event.getChannel().sendMessage(box.build()).addFile(new File("./rando.jpg"), "rando.jpg").queue();
				box.clear();
				// event.getChannel().sendFile(new File("./rando.jpg")).queue();
				Thread.sleep(1000);
			} catch (IOException | InterruptedException e) {
				event.getChannel().sendMessage("[ERROR] Could not get image from site.").queue();
				e.printStackTrace();
			}
		}

		if (messageRaw.contains("דימה") && !messageRaw.contains("קדימה") && !sender.getEffectiveName().contains("Dima")
				&& rand.nextInt(2) == 1) {
			try {
				doc = Jsoup.connect("https://tenor.com/search/ragnar-gifs").get();
				Elements emts = doc.getElementsByClass("Gif").select("img");
				String img;
				do {
					img = emts.get(rand.nextInt(emts.size())).absUrl("src");
				} while (img
						.contains("https://tenor.com/assets/img/gif-maker-entrypoints/search-entrypoint-desktop.gif"));
				event.getChannel().sendMessage(img).queue();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "mememe")) {
			try {
				doc = Jsoup.connect("https://www.memedroid.com/memes/top/day").get();
			} catch (IOException e) {
				System.out.println("Could not get a meme.");
				e.printStackTrace();
			}
			Elements articles = doc.select("section").select("article");
			Element ie = articles.get(rand.nextInt(articles.size())).select("img").first();
			String abs = ie.absUrl("src");
			event.getChannel().sendMessage(abs).queue();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "vic")) {
			try {
				doc = Jsoup.connect("https://moodle.afeka.ac.il/message/output/popup/notifications.php").get();
			} catch (IOException e) {
				System.out.println("Could not get a meme.");
				e.printStackTrace();
			}
			Elements articles = doc.getElementsByClass("content");
			Element ie = articles.select("br").select("#text").first();
			String abs = null;
			abs = ie.text();
			event.getChannel().sendMessage(abs).queue();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "help")
				|| event.getMessage().getContentRaw().contains("בוט איך אני")) {
			info.setTitle("Here's what you can tell me to do:");
			info.setDescription("🕶 !help - Display this list.\n\n"
					+ "🕶 !moodle - Go to notifications in Moodle site.\n\n"
					+ "🕶 ![Teacher's Name] - According ZOOM Lecture.\n\n"
					+ "🕶 !cheats - Google Drive homework answers.\n\n" + "🕶 !ping/pong - You can't beat me!\n\n"
					+ "🕶 !rickroll - Your favorite song.\n\n" + "🕶 !zombies - Display current COVID-19 status.\n\n"
					+ "🕶 !joke - Tell me a joke.\n\n" + "🕶 !roll - Roll a standard dice.\n\n"
					+ "🕶 !pokerhand - Give me a hand in POKER.\n\n" + "🕶 !pokertable - Roll a POKER-TABLE.\n\n"
					+ "🕶 !shuffle - Shuffle the deck.\n\n" + "🕶 !kabukME - Send a stupid question.\n\n"
					+ "🕶 !song: [name] - Give a song from YouTube.\n\n"
					+ "🕶 בוט ספר לי משהו - תקציר רנדומלי מויקיפדיה\n\n"
					+ "🕶 בוט תוסיף לציטוטי [שם]: [ציטוט] - להוסיף ציטוט\n\n"
					+ "🕶 בוט תן לי ציטוטי [שם] - להראות את הציטוטים הרלוונטיים\n\n");
			info.setColor(0xd126a3);
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (event.getMessage().getContentRaw().contains("בוט אני צריך תשובות לשיעורים")
				|| args[0].equalsIgnoreCase(CoronaBot.prefix + "cheats")) {
			calledBot = true;
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(
					"© Computer Sience:\nhttps://drive.google.com/drive/folders/1AxpCikuYdmharYnSYp50XtTn3h-Hc9dC"
							+ "\n\n© Software Engineering:\nhttps://drive.google.com/drive/folders/19AzMTb-D_yvfe3AmJsWS8XHAf1R7xN8a")
					.queue();
		}

		if (args[0].equalsIgnoreCase(CoronaBot.prefix + "moodle")) {
			info.setTitle("Moodle Site 📚");
			info.setDescription("THERE YOU GO MASTER:\n");
			info.appendDescription("https://moodle.afeka.ac.il/message/output/popup/notifications.php");
			info.setColor(0xe3910e);
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}

		if (rand.nextInt(2) == 1)
			for (String haha : args) {
				if (haha.contains("חחחחחחחחחחחחחח")) {
					CoronaBot.w = CoronaBot.w + 1;
					if (CoronaBot.w == 1 && repeatCatcher == false) {
						event.getChannel().sendTyping().queue();
						switch (rand.nextInt(4)) {
						case 0:
							event.getChannel().sendMessage("https://tenor.com/view/minionslol-gif-4519855").queue();
							break;
						case 1:
							event.getChannel().sendMessage("https://tenor.com/view/spit-take-laughing-lmao-gif-9271317")
									.queue();
							break;
						case 2:
							event.getChannel().sendMessage(
									"https://tenor.com/view/sarcastic-laugh-funny-michael-scott-hahaha-gif-14461360")
									.queue();
							break;
						case 3:
							event.getChannel()
									.sendMessage("https://tenor.com/view/jerry-funny-animal-laughing-gif-13124924")
									.queue();
						}
						repeatCatcher = true;
					} else if (CoronaBot.w == 3)
						CoronaBot.w = 0;
				} else if (haha.contains("חחחחח") && !sender.getEffectiveName().contains("Dima")) {
					CoronaBot.w = CoronaBot.w + 1;
					if (CoronaBot.w == 1 && repeatCatcher == false) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < rand.nextInt(8) + 3; i++)
							sb.append("ח");
						event.getChannel().sendTyping().queue();
						event.getChannel().sendMessage(sb.toString()).queue();
						repeatCatcher = true;
					} else if (CoronaBot.w == 3)
						CoronaBot.w = 0;
				}

			}

		for (String word : messageRaw.split(" "))
			if (word.contentEquals("בוט") && !calledBot && !repeatCatcher) {
				if (role.contains("בריון")) {
					if (CoronaBot.BC == 0) {
						event.getChannel().sendTyping().queue();
						event.getChannel().sendMessage("אני לא עונה לבריונים.").queue();
						CoronaBot.BC++;
						repeatCatcher = true;
					} else if (CoronaBot.BC == 1) {
						event.getChannel().sendTyping().queue();
						event.getChannel().sendMessage("אמרתי לך כבר שאני לא עונה לך.").queue();
						CoronaBot.BC++;
						repeatCatcher = true;
					} else if (CoronaBot.BC == 2) {
						event.getChannel().sendTyping().queue();
						event.getChannel().sendMessage("שתוק.").queue();
						CoronaBot.BC = 0;
						repeatCatcher = true;
					}
				} else {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage(NameRes.main(sender.getEffectiveName())).queue();
					repeatCatcher = true;
				}

			}
	}

	public static void replyTo(String catchMes, String throwMes) throws Exception {
		if (event.getMessage().getContentRaw().contains(catchMes)) {
			repeatCatcher = true;
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(throwMes).queue();
			throw new Exception();
		}
	}

	public static void replyTo(String catchMes, String throwMes, int oneOfChance) throws Exception {
		if (event.getMessage().getContentRaw().contains(catchMes) && rand.nextInt(oneOfChance) == 0) {
			repeatCatcher = true;
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(throwMes).queue();
			throw new Exception();
		}
	}
}