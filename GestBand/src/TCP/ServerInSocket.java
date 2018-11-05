/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chris
 */
public class ServerInSocket {

    private static ServerSocket Server;
    private static Thread ThreadListen, ThreadClient;
    private static Socket Client;
    
    public interface Actions{
        public void run(char c);

}

    public static void Start(Actions acts) throws IOException {
        System.out.println("Iniciando servidor...");
        Server = new ServerSocket(3322);
        Client = new Socket();
        ThreadListen = new Thread() {
            public void run() {
                try {
                    System.out.println("Esperando conexão...");
                    Client = Server.accept();
                    System.out.println("Cliente conectado do IP");
                    ThreadClient = new Thread() {
                        public void run() {
                            Scanner entrada;
                            System.out.println("Escutando...");
                            try {
                                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(Client.getInputStream()));
                                while (true) {
                                    acts.run((char)inFromClient.read());
                                }

                            } catch (IOException ex) {
                                Logger.getLogger(ServerInSocket.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }

                    };
                    ThreadClient.start();

                } catch (IOException ex) {
                    Logger.getLogger(ServerInSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
                //ServerInSocket.Client = Server.accept();
                //ServerInSocket.Client = Server.accept();

            }

        };
        ThreadListen.start();

    }
}
