# CoronaBot ![header](/assets/botpic.png "Mr. CoronaBot")
A Discord bot for various tasks i.e easy-access replies (e.a links, songs, corona statistics), quote saving as a joke, minigames (e.a Poker, Car Model Guessing) and even a small RPG game completely made from scratch.

This was my first year side project and sometimes I keep updating it to this day but the it's main purpose is for my own testing and code learning because it gives me an easy platform that has a lot of options in terms of API's such as Jsoup for accessing internet elements and website data.

Because we started attending courses only through Zoom after the whole CoronaVirus pandemic, I've decided to create a bot on the Discord platform so that me and my friends, as students, could have a tool that would pass time during the long days of lecture attending, while sitting in front of the monitor at home. This made us feel more connected, less lonely and more inspired to engage with each other and use our Discord Server.

## Feature Description
This section is dedicated for a short summary of **some** of CoronaBot's features.

### Corona Statistics
This feature uses Jsoup library in order to access the data at 'https://www.worldometers.info/coronavirus/' website. It constructs a description using emojies and display statistics about the coronavirus worldwide and Israel's specific statistics by navigating to the relevant extracted data.
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

![statistics](/assets/zombiespic.png "Corona Statistics")

### Songs and Videos
A short exposition - I've found no way to access the search database or links from the YouTube site directly using the JDA bot, so I though of a workaround:
* Google does support JDA access -> Search the song/video on google by constructing a search link.
* If the search results of the first page contain a YouTube result, copy the first link (Most relevant by Google's standards) and return in as a message by the bot.
* The resulting Discord logic will then take the link and reconstruct it as a playable video in the Discord chat.

Google search link construction:
```
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
```

![songs](/assets/songpic.png "Pulling the strings")

### Car Quiz
This feature downloads a random image from an online API of random cars: 'https://www.generatormix.com/random-car-model-generator', then it does the following:
* There is a default title for the car pictures in the site but it is mostly very lacking and unimformative in terms of specific models, therefore I use Yandex Image Search in order to search the image and get the title of the most relevant image which is most of the times the exact model of the car.
* If unable to find the model by the Yandex Search, I simply use the default title.
* The program crops the image randomly, I've found some golden ratio by cropping specifically close to the middle of the picture.
* The cropped image is send in the chat and the users have to guess the model.

This car title is saved in the answer variable that can be later pulled by typing the command: '!caranswer' if you think you figured out the answer.

![carquiz](/assets/carpic.png "Car Quiz")

### Poker
One of my very first projects for the bot, The class generates a virtual deck which you can pull cards from (Removed from the array, non-repeating) and then, presumably after everybody have pulled a 'hand', you can pull the table cards (5 cards) and check who've won. This feature passed quite a lot of time when dealing with a speacially boring section in a lecture.
- [ ] I've had a plan to write a game mechanic that actually controls the game flow and know's the rules and can tell who is the winner. TODO

![poker](/assets/pokerpic.png "Playing Poker")
