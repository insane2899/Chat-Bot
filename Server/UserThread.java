package Server;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class UserThread extends Thread{
	private Socket socket;
	private ChatServer server;
	private PrintWriter pw;

	public UserThread(Socket socket,ChatServer server){
		this.socket = socket;
		this.server = server;
	}

	public void run(){
		try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
				pw = new PrintWriter(socket.getOutputStream(),true);
				printUsers();
				String userName = br.readLine();
				server.addUserName(userName);
				String serverMessage = "New user connected: "+userName;
				server.broadcast(serverMessage,this);
				String clientMessage;
				do{
					clientMessage = br.readLine();
					serverMessage = "["+userName+"]: "+clientMessage;
					server.broadcast(serverMessage,this);
				}while(!clientMessage.trim().equals("Exit Chat"));
				server.removeUser(userName,this);
				socket.close();
				serverMessage = userName+" has quitted.";
				server.broadcast(serverMessage,this);
			}catch(IOException ex){
				System.out.println("Error in UserThread: "+ex.getMessage());
				ex.printStackTrace();

			}
	}

	//Print Users
	public void printUsers(){
		if(server.hasUsers()){
			pw.println("Connected users: "+server.getUserNames());
		}
		else{
			pw.println("No other users connected");
		}
	}

	//Send Messages
	public void sendMessage(String message){
		pw.println(message);
		}
}

