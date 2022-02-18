package MyBot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

abstract class Lectures {
	static File file = new File(CoronaBot.FS_PATH + "/Lectures.txt");
	static String lastReadDate;

	public static String getText(String name, String day) {
		day = day.toUpperCase();
		StringBuilder sb = new StringBuilder();
		try {
			Scanner read = new Scanner(file);
			read.next();
			while (read.hasNext())
				sb.append(read.nextLine() + "\n");
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String seg : sb.toString().split("#")) {
			if (seg.contains(name)) {
				for (String seg2 : seg.split("@"))
					if (seg2.contains(day))
						return seg2.replace(day+"\n", "").substring(seg2.indexOf("~")+1);
			}
		}

		return null;
	}

	public static void readLastRead() {
		try {
			Scanner read = new Scanner(file);
			lastReadDate = read.nextLine();
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeLastRead() {
		try {
			Scanner read = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			read.nextLine();
			while(read.hasNext())
				sb.append(read.nextLine()+"\n");
			sb.deleteCharAt(sb.length()-1);
			FileWriter fw = new FileWriter(file);
			fw.write(LocalDate.now().format(format)+"\n");
			fw.write(sb.toString());
			fw.close();
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
