/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author User
 */
public class ServerThread implements Runnable{

    Socket socket;
    String transport;
    ServerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        try{
            String message = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((message = bufferedReader.readLine()) != null ){
                System.out.println(message);
            }
        }catch(IOException e){
            System.out.println("Problem");
        }
    
    }
}
