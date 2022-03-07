# CoronaBot ![header](/assets/botpic.png "Mr. CoronaBot")
A Discord bot for various tasks i.e easy-access replies (e.a links, songs, corona statistics), quote saving as a joke, minigames (e.a Poker, Car Model Guessing) and even a small RPG game completely made from scratch.

This was my first year side project and sometimes I keep updating it to this day but the it's main purpose is for my own testing and code learning because it gives me an easy platform that has a lot of options in terms of API's such as Jsoup for accessing internet elements and website data.

Because we started attending courses only through Zoom after the whole CoronaVirus pandemic, I've decided to create a bot on the Discord platform so that me and my friends, as students, could have a tool that would pass time during the long days of lecture attending, while sitting in front of the monitor at home. This made us feel more connected, less lonely and more inspired to engage with each other and use our Discord Server.

## Feature Description
This section is dedicated for a short summary of the **some** of the features of CoronaBot.

### Corona Statistics
![statistics](/assets/zombiespic.png "Corona Statistics")
This feature uses Jsoup in order to access the data at: 'https://www.worldometers.info/coronavirus/', constructs a description using emojies and display statistics about world statistics and Israel's specific statistics by navigating to the relevant extracted data.
```
			try {
				doc = Jsoup.connect("https://www.worldometers.info/coronavirus/country/israel/").get();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			doc.getElementsByClass("maincounter-number").eachText().toArray(data);
			info.appendDescription("\n\n🟦 ISRAEL: ⬜\n\n" + "• Infected Cases: " + data[0] + "\n" + "• Related Deaths: "
					+ data[1] + "\n" + "• Recovered: " + data[2]);
```
