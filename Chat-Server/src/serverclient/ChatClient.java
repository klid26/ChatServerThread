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

/* Bietet Klassen für Netzwerkanwendungen*/
import java.net.*;

// Klasse im java.net --- Zeigt an, dass eine ungültige URL aufgetreten ist
import java.io.*;

public class ChatClient implements Runnable{
    
    // Vordefinierte Socket, mit dem wir eine Verbindung erstellen
    private Socket socket = null;
    
    // Vordefinierte Thread die wir erstellen
    private Thread thread = null;
    
    // Vordefinierte Objekte die wir verwenden Data zu senden und bekommen
    private DataInputStream  console = null;
    private DataOutputStream streamOut = null;
    
    // Vordefinierte Objekt von die Klasse ChatClientThread die client heißt
    private ChatClientThread client = null;
 
   // Konstruktor für diese Klasse
   public ChatClient( int serverPort){  
      System.out.println("Establishing connection. Please wait ...");
      try{ 
          
          // Erstellen ein Verbindung
          socket = new Socket("localhost",serverPort);
          System.out.println("Connected: " + socket);
          
          // Rufe die Methode start()
          start();
      }
      
      // Wenn es fehler gibt dann zeigt "Host unknown: " + uhe.getMessage() or "Unexpected exception: " + ioe.getMessage()
      catch(UnknownHostException uhe){  
          System.out.println("Host unknown: " + uhe.getMessage()); 
      }
      catch(IOException ioe){  
          System.out.println("Unexpected exception: " + ioe.getMessage()); 
      }  
   }
   
   // Die Methode run () wird nach dem Erstellen eines Threads aufgerufen und darin wird geschrieben, was in diesem Thread ausgeführt wird.
@Override
   public void run(){  
       
       // Überprüft, ob der Thread existiert
       while (thread != null){  
           try{ 
               // Liest die Daten von die console und sendet weiter
               streamOut.writeUTF(console.readLine());
               streamOut.flush();
            }catch(IOException ioe){  
               System.out.println("Sending error: " + ioe.getMessage());
               
               // Ruft Methode stop()
               stop();
            }
        }
    }
   
   // Diese Methode verwenden wir die Thread zu synchronizieren und die Messages anzeigen
   public void handle(String msg){  
       if (msg.equals(".bye")){  
           System.out.println("Good bye. Press RETURN to exit ...");
           stop();
       }
        else
           System.out.println(msg);
   }
   
   public void start() throws IOException{  
       
       // Wir verwenden diese Objekte Data zu senden und bekommen
       console   = new DataInputStream(System.in);
       streamOut = new DataOutputStream(socket.getOutputStream());
       
       // Überprüft, ob der Thread existiert
       if (thread == null){  
           
           // Erstellt eine neue Client Thread 
           client = new ChatClientThread(this, socket);
           thread = new Thread(this);                   
           thread.start();
      }
    }
   
   public void stop(){  
       
       // Überprüft, ob der Thread existiert
       if (thread != null){  
           
           // Stopt das Thread und setzt sie null
           thread.stop();  
           thread = null;
      }
       
      // Schließt alle Verbindungen diese Objekte machen
      try{  
          if (console   != null)  console.close();
          if (streamOut != null)  streamOut.close();
          if (socket    != null)  socket.close();
      }catch(IOException ioe){  
          System.out.println("Error closing ..."); 
      }
      // Schließt die Client und stopt das Program
          client.close();  
          client.stop();
   }
   public static void main(String args[])
   {  
       //Es erstellt ein Objekt, ruft den Konstruktor auf und gibt den Port 4444 an.
       ChatClient client = new ChatClient(4444);
   }
}