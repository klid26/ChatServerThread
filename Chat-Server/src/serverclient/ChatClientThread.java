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
import java.util.Scanner;

public class ChatClientThread extends Thread{ 
    
    // Vordefinierte Socket, mit dem wir eine Verbindung erstellen
    private Socket socket = null; 
    
    // Vordefinierte Objekt von die Klasse ChatClientThread die client heißt
    private ChatClient client = null;
    
    // Vordefinierte Objekt die wir verwenden Data zu bekommen
    private DataInputStream streamIn = null;

   public ChatClientThread(ChatClient _client, Socket _socket){
       client   = _client;
       socket   = _socket;
       
       //Ruft die Methode open() und start()
       open();  
       start();
   }
 
   public void open(){
       try{
           // Geben die Patameter um die Daten zu lesen
           streamIn  = new DataInputStream(socket.getInputStream());
           
           // Wenn Probleme hat wird "Error \"getting input stream: " + ioe gezeigt
      }catch(IOException ioe){  
          System.out.println("Error \"getting input stream: " + ioe);
          client.stop();
      }
   }
   public void close(){  
       try{
           // Überpruft ob es gibt eine DataInputStream  in Benutzung und stopt es
           if (streamIn != null) streamIn.close();
      }catch(IOException ioe){  
          System.out.println("Error closing input stream: " + ioe);
      }
   }
   
   // Die Methode run () wird nach dem Erstellen eines Threads aufgerufen und darin wird geschrieben, was in diesem Thread ausgeführt wird.
   public void run(){  
       while (true){
           try{  
               // Ruft die Methode handle von ChatServer wo die ID und String von InputStream als parameter gegeben sind
               client.handle(streamIn.readUTF());
               
               // Wenn Probleme gibt dann wird in die Console "Listening error: " + ioe.getMessage() gezeigt
           }catch(IOException ioe){  
               System.out.println("Listening error: " + ioe.getMessage());
               client.stop();
         }
      }
   }
}
