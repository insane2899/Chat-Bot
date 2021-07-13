package Server;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;

public class ChatServer{
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();

	public ChatServer(int port){
		this.port = port;
	}

	public void start(){
		//try() is a try-with-resource statement which means that the resource initialized inside the brackets will close before the block ends no matter if exception is thrown or normal exit takes place from the try block
		try (ServerSocket serverSocket = new ServerSocket(port)){
			System.out.println("Chat Server is listening on port "+port);
			while(true){
				Socket socket = serverSocket.accept();
				System.out.println("New User connected");
				UserThread newUser = new UserThread(socket,this);
				userThreads.add(newUser);
				newUser.start();
			}
		}catch(IOException ex){
			System.out.println("Error in the server: "+ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args){
		if(args.length<1){
			System.out.println("Syntax: java ChatServer <port-number>");
			System.exit(0);
		}
		int port = Integer.parseInt(args[0]);
		ChatServer server = new ChatServer(port);
		server.start();
	}

	//Sending message to all users
	public void broadcast(String message,UserThread excludeUser){
		for(UserThread user: userThreads){
			if(user!=excludeUser){
				user.sendMessage(message);
			}
		}
	}

	//Storing username of the newly connected client
	public void addUserName(String userName){
		userNames.add(userName);
	}

	//Removing userName and userThread when disconnected
	public void removeUser(String userName,UserThread user){
		boolean removed = userNames.remove(userName);
		userThreads.remove(user);
		System.out.println("The user "+userName+" quitted");
	}

	public Set<String> getUserNames(){
		return this.userNames;
	}

	//Check if any user is online
	public boolean hasUsers(){
		return !this.userNames.isEmpty();
	}
}


