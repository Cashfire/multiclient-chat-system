package com.mao.chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class ChatClient extends Frame{
	
	private TextArea ta = new TextArea();
	private TextField tf = new TextField();
	private DataOutputStream dos= null;
	private DataInputStream dis = null;
	// A socket is an endpoint for communication between two machines.
	private Socket socket = null;
	private boolean bConnected = false;
	private Thread thread = null;
	
	public static void main(String[] args) {
		new ChatClient().clientFrm();

	}
	public void clientFrm(){
		setSize(400, 400);
		setLocation(400,300);
		add(ta, BorderLayout.NORTH);
		add(tf, BorderLayout.SOUTH);
		pack();
		
		tf.addActionListener(new TfListener());
		//close the winder listener
		this.addWindowListener(new WindowAdapter(){ //???
			public void windowClosing(WindowEvent we){
				disconnected(); //???
				System.exit(0);//???
			}
		});
		this.connect();//???
		setVisible(true);
	}
	/*
	 * connect server address??
	 */
	private void connect() {
		try{
			socket = new Socket("127.0.0.1", 1234);//???
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			thread = new Thread(new ChatThread());//???
			thread.start();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void disconnected() {
		bConnected = false;
		try{
			dos.close();
			dis.close();
			socket.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	/*
	 * event handler of "enter"???
	 */
	private class TfListener implements ActionListener{
	       public void actionPerformed(ActionEvent evt) {
	        String strMsg = tf.getText();
	        tf.setText("");
	        try{
	         dos.writeUTF(strMsg);
	         dos.flush(); //??????
	        }catch(IOException e) {
	         e.printStackTrace();
	        }
	       }	         
	      }
	
	private class ChatThread implements Runnable{
		public void run(){
			try{
				bConnected = true;
				while(bConnected){
					String msg = dis.readUTF();
					String taText = ta.getText();
					ta.setText(taText+ msg + "\n");
				}
			}catch(SocketException e){
				System.out.println("Exit");				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}


