/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

/**
 *
 * @author Klid Selhani
 */
import java.net.*;
import java.io.*;

public class ChatServer implements Runnable{  
   
   private ChatServerThread clients[] = new ChatServerThread[50];
   
   // Die Socket die wir verwenden um eine Verbinddung zu machen
   private ServerSocket server = null; 
   
   // Vordefinierte Thread 
   private Thread thread = null;
   
   // Die Zahl wie viel Clients wir haben 
   private int clientCount = 0;

   public ChatServer(int port){  
       try{  
           System.out.println("Binding to port " + port + ", please wait  ...");
           
           // Machen wir eine neue Verbindung
           server = new ServerSocket(port);  
           
           // Zeigt die Addresse und auch die port number
           System.out.println("Server started: " + server);
           
           start(); 
       }
      catch(IOException ioe)
      {  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
   }
   public void run()
   {  while (thread != null)
      {  try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept()); }
         catch(IOException ioe)
         {  System.out.println("Server accept error: " + ioe); stop(); }
      }
   }
   
   public void start(){ 
       
       // Wenn die Socket keine Thread hat dann wird eine neue Thread erstellt
       if (thread == null){  
           thread = new Thread(this); 
        
        //Mit diese Befehl startet das Thread
           thread.start();
      } 
   }
   
   public void stop(){
       
       // Wenn wir wollen das Thread stoppen, überpruft ob es gibt eine und dann stoppet es
       if (thread != null){  
           
           // Mit diese Befahlt stopt ein Thread
           thread.stop();
           
           thread = null;
      } 
   }
   
   // Wenn wir die Client finden wollen, rufen diese Methode und senden als Parameter die ID
   private int findClient(int ID){  
       for (int i = 0; i < clientCount; i++){
         if (clients[i].getID() == ID)
            return i;
       }
      return -1;
   }
   
   public synchronized void handle(int ID, String input){
       if (input.equals(".bye")){
           clients[findClient(ID)].send(".bye");
           remove(ID); 
       }
       else{
         for (int i = 0; i < clientCount; i++)
            clients[i].send(ID + ": " + input); 
       }
   }
   
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   
   private void addThread(Socket socket)
   {  if (clientCount < clients.length)
      {  System.out.println("Client accepted: " + socket);
         clients[clientCount] = new ChatServerThread(this, socket);
         try
         {  clients[clientCount].open(); 
            clients[clientCount].start();  
            clientCount++; }
         catch(IOException ioe)
         {  System.out.println("Error opening thread: " + ioe); } }
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   
   public static void main(String args[]) { 
        ChatServer server = new ChatServer(4444);}
}