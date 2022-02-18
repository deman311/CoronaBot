package MyBot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

abstract class GetText {
	public static Random rand = new Random();
	
	public static String GetRandomText(String desired) {
		String[] Text = null;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(desired)),"UTF8"));
			while((line = reader.readLine())!=null) {
				sb.append(line+"\n");
			}
			Text = sb.toString().split("\n");
			reader.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported Format!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error Reading From The File!");
			e.printStackTrace();
		}
		return Text[rand.nextInt(Text.length)];
	}
	
	public static String GetRandomText(String specific,String desired) {
		String[] Text = null;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFileName(desired)),"UTF8"));
			while((line = reader.readLine())!=null)
				if(line.startsWith(specific)) {
					sb.append(line.replace(specific + ": ","") + "\n");
					Text = sb.toString().split("\n");
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported Format!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error Reading From The File!");
			e.printStackTrace();
		}
		
		return Text[rand.nextInt(Text.length)];
	}
	
	private static String getFileName(String desired) {
		String fileName=null;
		switch(desired) {
		case "BokerTov":
			fileName = CoronaBot.FS_PATH + "/BokerTov.txt";
			break;
		case "ResToCall":
			fileName = CoronaBot.FS_PATH + "/ResToCall.txt";
			break;
		}
		
		return fileName;
	}
}
