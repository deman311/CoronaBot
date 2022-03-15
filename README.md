# CoronaBot ![header](/assets/botpic.png "Mr. CoronaBot")
##### Version 0.5

A Discord bot for various tasks i.e easy-access replies (e.a links, songs, corona statistics), quote saving as a joke, minigames (e.a Poker, Car Model Guessing) and even a small RPG game completely made from scratch.

This was my first year side project and sometimes I keep updating it to this day but the it's main purpose is for my own testing and code learning because it gives me an easy platform that has a lot of options in terms of API's such as Jsoup for accessing internet elements and website data.

Because we started attending courses only through Zoom after the whole CoronaVirus pandemic, I've decided to create a bot on the Discord platform so that me and my friends, as students, could have a tool that would pass time during the long days of lecture attending, while sitting in front of the monitor at home. This made us feel more connected, less lonely and more inspired to engage with each other and use our Discord Server.

## Feature Description ⭐
This section is dedicated for a short summary of **some** of CoronaBot's features.

### Corona Statistics 🧟
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

### Songs and Videos 📹
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

### Car Quiz 🚗
This feature downloads a random image from an online API of random cars: 'https://www.generatormix.com/random-car-model-generator', then it does the following:
* There is a default title for the car pictures in the site but it is mostly very lacking and unimformative in terms of specific models, therefore I use Yandex Image Search in order to search the image and get the title of the most relevant image which is most of the times the exact model of the car.
* If unable to find the model by the Yandex Search, I simply use the default title.
* The program crops the image randomly, I've found some golden ratio by cropping specifically close to the middle of the picture.
* The cropped image is send in the chat and the users have to guess the model.

This car title is saved in the answer variable that can be later pulled by typing the command: '!caranswer' if you think you figured out the answer.

![carquiz](/assets/carpic.png "Car Quiz")

### Poker 🎴
One of my very first projects for the bot, The class generates a virtual deck which you can pull cards from (Removed from the array, non-repeating) and then, presumably after everybody have pulled a 'hand', you can pull the table cards (5 cards) and check who've won. This feature passed quite a lot of time when dealing with a speacially boring section in a lecture.
- [ ] I've had a plan to write a game mechanic that actually controls the game flow and knows the rules - can tell who is the winner.

![poker](/assets/pokerpic.png "Playing Poker")

### Daily Lecture Links ⏰
This feature sends the relevant Zoom links to each of the users on the server by a txt file with a special format where the links are saved - which is prepared at every start of a semester and can be always updated when needed with new users and links.

This specific feature runs on a thread. This thread is updated on the current time and date and every morning, not on weekends, between 7-8 AM sends the relevant links to everybody on the server via a private message on Discord.

![links](/assets/linkspic.png "The message format you receive")

### VR Updates 🕶️
As a casual VR player and a pirate at that 🏴‍☠️, I constantly check forums (e.a https://cs.rin.ru) for new releases of VR games. Therefore, I've created an automated process which scans the first X pages of the forum and searches for ones with the VR tag, then it constructs a Microsoft Word file with the names of the found game titles and the links to the specific page in the forum. The links are created using Hyperlinks in the word document, the specific implementation of the function is from here: https://stackoverflow.com/questions/49192481/how-to-create-hyperlinks-in-apache-poi-word.

The process is to write the command '!seekVR' on the server and then the CoronaBot will start the process and send you the file and reply in a private message on Discord:

![vrmessage](/assets/vrupdatepic.png "seekVR private reply message")

And here is an example of an output file.

The titles are also being saved and compared each time to a txt file with previous finds and that way the new titles are marked with red:

⚫ is regular titles. 🔴 is new titles.

![vrdoc](/assets/wordvrpic.png "VR titles output file example")

## AfekaLands 🌲🧝‍
This part is going to explore the 'Chat-RPG' that I started writing when being a first-year student. I worked on it throughout my first year and eventually left it to work on other projects that will be available to explore throught my GitHub page, **everything is written from scratch.** This project gave me great inspiration to explore different topics and practice my coding skills as well as my understanding of various topics though at the academia.

### General Feature Introduction 🌠
The game is called AfekaLands - Afeka being the name of the Engineering College I studied in. The general idea was to create a sort of randomly generated world and spawn enemies and shops on it that the player can fight and explore trying to reach the highest level possible. All of the game's controls are done by writting commands in the Discord chat and all of the 'UI' and feedback is done by drawing different menus - from fighting menus to shop menus to game stats.

![intro](/assets/AfekaLands/intro.png "The intro you get when creating a new character")

### The Map 🌳🟦⛰️
The map is created by generation a TXT file that contains various symbols to represent 'tiles' that later will be translated to emojis in chat to represent the game map. The map contains trees, mountains and water - the latter being untraversable (either by enemies or the player), there are also reeds that can be generated close to water and the water tiles themselves are generated as a group using a function that 'spreads' it across the map array.

![map](/assets/AfekaLands/map.png "All the various tiles")

Traversing the map is done by using the ![wasd] keys and each move the CoronaBot updates/edits the same message in chat to not overflow it with messages and keep everything tidy and neat.

![mapgif](/assets/AfekaLands/moveOnMap.gif "Moving around")

### Shops 🏘️
The map also contains a shop tile that is also sort of randomly generated, from its name to the owner's name, to the items it sells, to its stock even. The player gathers coins around the map by looting enemies after defeating them around the map - these in turn can be used to purchase things in the shops.

![shop](/assets/AfekaLands/shop.png "A random shop")

**Here is the looting process:**

First we kill: ![deadmob](/assets/AfekaLands/deadmob.png "Die you crazy bat")

Then we loot: ![loot](/assets/AfekaLands/loot.png "Looting some things")

---

### Fighting ⚔️
There are 2 kinds of attacks: swords and spells (currently there's only one implemented being the fireball🔥, but the basis code is written to support more spells - You can edit the spells.txt file and implement different spells with different damage, emojies and overall stats). Using these attacks you engage in battles in AfekaLands and fight various randomly generated enemies.

![fight](/assets/AfekaLands/fastFight.gif "Swift encounter")

#### Random Stats 🧮
The game includes Enemies, Weapons & Armor that, when created, generate with random stats and modifiers. Thus, Enemies can be: Though, Strong, Crazy, Small, Weak etc... Weapons & Armor can be Strong, Epic, Legendary, Weak etc...

![enemy stats](/assets/AfekaLands/fight.png "A random enemy")

### Equipment 🛡️
The player has an option to view his inventory, wear and takeoff equipment and see the status effects of each one, all of this by writing specific chat commands.

![inv](/assets/AfekaLands/inventory.png "A pretty empty inventory")

![status](/assets/AfekaLands/status.png "Current status")
![equip](/assets/AfekaLands/equipment.png "What am I wearing?")
