package com.mao.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private boolean started = false;
	private List<ChatThread> chatThreads = new ArrayList<ChatThread>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer().startServer();
	}

	private void startServer() {
		// TODO Auto-generated method stub
		try{
			//start a server socket
			ServerSocket seso = new ServerSocket(1234);
			started = true;
			while(started){
				//accept the request from client socket?
				Socket sos = seso.accept();
				System.out.println("Accept a client socket");
				//new a thread to deal with the client
				ChatThread ct = new ChatThread(sos);
				chatThreads.add(ct);//???
				new Thread(ct).start();//???
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private class ChatThread implements Runnable{
		private Socket socket;
		private DataInputStream din = null;
		private DataOutputStream don = null;
		private boolean bConnected = false;
		public ChatThread(Socket socket){
			super();
			this.socket = socket;
		}

		public void run(){
			try {
				din= new DataInputStream(socket.getInputStream());
				don = new DataOutputStream(socket.getOutputStream());
				//read and get the date
				bConnected = true;
				while(bConnected){
					String strMsgIn = din.readUTF();
					System.out.println(strMsgIn);
					//send the data to every client
					for(int i =0; i< chatThreads.size();i++){
						chatThreads.get(i).send(strMsgIn);
					}
				}
			} catch (IOException e) {
				try{
					socket.close();
					chatThreads.remove(this);
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}finally{
				try{
					din.close();
					don.close();
					socket.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}	
		
		private void send(String strMsgIn){
			try{
				don.writeUTF(strMsgIn);
				don.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}	
	
	
}
