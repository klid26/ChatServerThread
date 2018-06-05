/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author User
 */
public class Server {
    public static final int PORT = 4444;
    public static void main(String[] args) throws IOException{
        new Server().runServer();
    }
    public void runServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server UP & Ready for connection");
        while(true){
           Socket socket = serverSocket.accept();
           (new Thread(new ServerThread(socket))).start();
         
        }
    }
}
