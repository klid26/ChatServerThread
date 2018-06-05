/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    private static String message;
    
    public static void main(String[] args) throws UnknownHostException, IOException{
        String name;
        
        Socket socket = new Socket("localhost", 4444);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        System.out.println("Please write your name:");
        Scanner sc = new Scanner(System.in);
        name = sc.nextLine();
        System.out.println("Chat now");
        while(true){
            printWriter.println(name + ": " + sc.nextLine());
            printWriter.flush();
            
        }
        }


}

