/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

/**
 *
 * @author User
 */
import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread{  
    
    // Definieren ein Objekt 
    private ChatServer server = null; 
    
    // Definieren ein Socket
    private Socket socket = null;
    
    // Hier werden wir die ID von die Socket speichern
    private int ID = -1;
    
    // Data senden
    private DataInputStream  streamIn = null;
    
    // Data bekommen
    private DataOutputStream streamOut = null;

   public ChatServerThread(ChatServer server, Socket socket)
   {  
      super();
      this.server = server;
      this.socket = socket;
      
       // Nimmt die ID von den Socket
      ID = socket.getPort();
   }
   
   public void send(String msg){   
       try{  
           // Es ist ein Methode um ein String in der Outputstream mit eine UTF-8 Codierung schreiben
           streamOut.writeUTF(msg);
           
           streamOut.flush();
       }catch(IOException ioe){
           System.out.println(ID + " ERROR sending: " + ioe.getMessage());
           
           // Verwenden wir um die Socket zu löschen
           server.remove(ID);
           stop();
        }
   }
   
   // Verwenden wenn wir das ID wollen 
   public int getID(){  
       return ID;
   }
   
   public void run(){  
       System.out.println("Server Thread " + ID + " running.");
      while (true){  
          try{  
              // Ruft die Methode handle von ChatServer wo die ID und String von InputStream als parameter gegeben sind
              server.handle(ID, streamIn.readUTF());
              
         }catch(IOException ioe){  System.out.println(ID + " ERROR reading: " + ioe.getMessage());
            server.remove(ID);
            stop();
         }
      }
   }
   
   public void open() throws IOException{
       
       // Wir wevenden diese Classen um Daten auf der Socket zu lesen und zu schreiben
       streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
       streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
   
   public void close() throws IOException{
       
       // Wenn wir sind mit det Chat fertig, rufen diese Methode um die Socket, InputStream und OutputStream zu beenden. 
       if (socket != null)    socket.close();
       if (streamIn != null)  streamIn.close();
       if (streamOut != null) streamOut.close();
   }
}
