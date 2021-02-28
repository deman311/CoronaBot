package AfekaLands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public abstract class Events {

	public static String giveAnswer(char answer) {
		File file = new File("./Files/AfekaLands/CurrentEvent.txt");
		StringBuilder text = new StringBuilder();
		String[] eventLines, answers = new String[0], replys = new String[0], status;
		Scanner readFile;
		int answerIndex = 0;
		try {
			readFile = new Scanner(file);
			while (readFile.hasNext()) {
				text.append(readFile.nextLine() + "\n");
			}
			eventLines = text.toString().split("\n");
			for (String line : eventLines) {
				if (!line.isEmpty()) {
					if ((int) line.charAt(0) >= 97 && (int) line.charAt(0) <= 122 && line.charAt(1) == ')') {
						answers = Arrays.copyOf(answers, answers.length + 1);
						answers[answers.length - 1] = line.substring(3);
					}
					if ((int) line.charAt(0) >= 65 && (int) line.charAt(0) <= 90 && line.charAt(1) == ')') {
						replys = Arrays.copyOf(replys, replys.length + 1);
						replys[replys.length - 1] = line.substring(3);
					}
				}
			}
			answerIndex = (int) answer - 97;
			status = answers[answerIndex].split(" ");
			for (String word : status) {
				if (word.contains("HP[")) {
					AfekaLandsUI.updateStatus(Integer.parseInt(word.substring(word.indexOf("[") + 1, word.indexOf("]"))),
							0);
				}
				if (word.contains("ENG[")) {
					AfekaLandsUI.updateStatus(0,
							Integer.parseInt(word.substring(word.indexOf("[") + 1, word.indexOf("]"))));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return replys[answerIndex];
	}

	public static String getEvent() {
		File eventsFile = new File("./Files/AfekaLands/Events.txt");
		File currentFile = new File("./Files/AfekaLands/CurrentEvent.txt");
		StringBuilder textPure = new StringBuilder();
		StringBuilder text = new StringBuilder();
		String[] events;
		Random rand = new Random();
		String line, event = null;
		Scanner readEvents, readCurrent;
		try {
			readEvents = new Scanner(eventsFile);
			readCurrent = new Scanner(currentFile);
			PrintWriter write = new PrintWriter("./Files/AfekaLands/CurrentEvent.txt");
			while (readEvents.hasNext()) {
				line = readEvents.nextLine();
				textPure.append(line + "\n");
			}
			events = textPure.toString().split("###");
			event = events[rand.nextInt(events.length)].toString();
			write.append(event);
			write.flush();
			write.close();
			while (readCurrent.hasNext()) {
				line = readCurrent.nextLine();
				if (line.contains("|"))
					text.append(line.substring(0, line.indexOf("|")) + "\n");
				else
					text.append(line + "\n");
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = text.toString().substring(0, text.toString().indexOf('~'));
		return event;
	}
}
