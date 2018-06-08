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

// Bietet Klassen für Netzwerkanwendungen
import java.net.*;

// Klasse im java.net --- Zeigt an, dass eine ungültige URL aufgetreten ist.
import java.io.*;

// Runnable ist eine Interface und es wird von jeder Klasse implementiert, deren Instanzen von einem Thread ausgeführt werden sollen.
public class ChatServer implements Runnable{  
   
   // Dies ist ein Array der Klasse Chat ServerThread, in dem alle Clients gespeichert sind und die maximale Anzahl von Clients bestimmt.
   private ChatServerThread clients[] = new ChatServerThread[50];
   
   // Der Socket, mit dem wir eine Verbindung erstellen
   private ServerSocket server = null; 
   
   // Vordefinierte Thread 
   private Thread thread = null;
   
   // Vordefinierte clientcount
   private int clientCount = 0;

   // Konstruktor für diese Klasse
   public ChatServer(int port){  
       try{  
           System.out.println("Binding to port " + port + ", please wait  ...");
           
           // Wir machen eine neue Verbindung
           server = new ServerSocket(port);  
           
           // Zeigt die Addresse und auch die port number
           System.out.println("Server started: " + server);
           
           // Ruft die Methode start()
           start(); 
       }
       // Wenn das Verbindung nicht erstellt wird, zeigt was drinen System.out.println ist 
        catch(IOException ioe){  
          System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
   }
   
   // Die Methode run () wird nach dem Erstellen eines Threads aufgerufen und darin wird geschrieben, was in diesem Thread ausgeführt wird.
   public void run(){  
       
       // Überprüft, ob der Thread existiert
       while (thread != null){  
           try{  
               System.out.println("Waiting for a client ..."); 
               
               // Ruft die Methote addThread 
               addThread(server.accept()); // server.accept() -- Hört auf eine Verbindung zu diesem Socket und akzeptiert sie.
           }catch(IOException ioe){  
               System.out.println("Server accept error: " + ioe); stop(); 
           }
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
       
       //  Überpruft ob es gibt ein Thread
       if (thread != null){  
           
           // Mit diese Befahlt stopt ein Thread und setzt sie null
           thread.stop();
           thread = null;
      } 
   }
   
   // Wenn wir die Client finden wollen, rufen diese Methode und senden als Parameter die ID und sie gibt züruck der Position in clients array.
   private int findClient(int ID){  
       for (int i = 0; i < clientCount; i++){
         if (clients[i].getID() == ID)
            return i;
       }
      return -1;
   }
   
   // Diese Methode verwenden wir die Thread zu synchronizieren und die Messages anzeigen
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
   // Diese MeThode entfernt die Synchronisation.
   public synchronized void remove(int ID){  
       int pos = findClient(ID);
       if (pos >= 0){
           
           // Estellt ein Objekt ChatServerThread mit der name toTerminate
           ChatServerThread toTerminate = clients[pos];
           System.out.println("Removing client thread " + ID + " at " + pos);
           
           // Dies überprüft, ob der Thread an der letzten Stelle gespeichert wurde. Wenn nicht, dann löscht es und schiebt die Werte um eine Position zurück.
           if (pos < clientCount-1)
               for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
               clientCount--;
         try{  
             
             // Beendet das Objekt 
             toTerminate.close(); 
             
         }catch(IOException ioe){  
             System.out.println("Error closing thread: " + ioe); 
         }
         
         // Das Thread stopen
         toTerminate.stop(); 
       }
   }
   
   private void addThread(Socket socket){ 
       
       // Prüft, ob die Anzahl der Clients kleiner als der definierte Wert ist
       if (clientCount < clients.length){  
           System.out.println("Client accepted: " + socket);
           
           // Fügt dem Array den neuen Thread hinzu
           clients[clientCount] = new ChatServerThread(this, socket);
           try{
               
               // Öffnet das Thread 
               clients[clientCount].open(); 
               
               //Started das Thread
               clients[clientCount].start();
               
               // clientcount wird mit 1 erhöht
               clientCount++; 
           }catch(IOException ioe){  
               System.out.println("Error opening thread: " + ioe); } }
        else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   
   public static void main(String args[]) { 
       
       //Es erstellt ein Objekt, ruft den Konstruktor auf und gibt den Port 4444 an.
        ChatServer server = new ChatServer(4444);}
}