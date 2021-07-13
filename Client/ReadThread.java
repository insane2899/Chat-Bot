package Client;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ReadThread extends Thread{
	private BufferedReader br;
	private Socket socket;
	private ChatClient client;

	public ReadThread(Socket socket,ChatClient client){
		this.socket = socket;
		this.client = client;
		try{
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(IOException ex){
			System.out.println("Error getting input stream: "+ex.getMessage());
			ex.printStackTrace();
		}
	}
	public void run(){
		while(true){
			try{
				String response = br.readLine();
				System.out.println("\n"+response);

				if(client.getUserName()!=null){
					System.out.print("["+client.getUserName()+"]: ");
				}
			}catch(IOException ex){
				System.out.println("Error reading from server: "+ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}
}
