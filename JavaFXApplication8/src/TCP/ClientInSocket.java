package TCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;/*



/**
 *
 * @author chris
 */
public class ClientInSocket {

    private static Socket Server;
    private static Thread ThreadListen;

    public interface Actions {

        public void run(char c);

    }

    public static void Start(Actions acts) throws IOException {
        System.out.println("Connectando a GestBand");
        Server = new Socket("192.168.4.1", 1234);
        ThreadListen = new Thread() {
            public void run() {
                Scanner entrada;
                System.out.println("Escutando...");
                try {
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(Server.getInputStream()));
                    while (true) {
                        acts.run((char) inFromClient.read());
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ServerInSocket.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        };
        ThreadListen.start();

    }
    public static void send(String s) throws IOException{
           DataOutputStream dOut = new DataOutputStream(Server.getOutputStream());
           dOut.write(s.getBytes());
    }
}
