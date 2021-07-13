package Client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class WriteThread extends Thread{
	private PrintWriter pw;
	private Socket socket;
	private ChatClient client;

	public WriteThread(Socket socket,ChatClient client){
		this.socket = socket;
		this.client = client;

		try{
			pw = new PrintWriter(socket.getOutputStream(),true);
		}catch(Exception ex){
			System.out.println("Error getting output stream: "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void run(){
		Console console = System.console();
		String userName = console.readLine("\nEnter your name: ");
		client.setUserName(userName);
		pw.println(userName);

		String text;

		do{
			text = console.readLine("["+userName+"]: ");
			pw.println(text);
		}while(!text.equals("Exit Chat"));
		try{
			socket.close();
		}catch(IOException ex){
			System.out.println("Error writing to server: "+ex.getMessage());
		}
	}
}
