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
    private static Thread ThreadListen = null;

    public interface Actions {

        public void run(char c);

    }

    public static boolean Start(Actions acts, String ip) {
        System.out.println("Connectando a GestBand");
        try {
            Server = new Socket(ip, 3322);
        } catch (IOException ex) {
            return false;
        }
        ThreadListen = new Thread() {
            public void run() {
                Scanner entrada;
                System.out.println("Escutando...");
                int c;
                try {

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(Server.getInputStream()));
                    while (true) {
                        c = inFromClient.read();
                        if (c > 0) {
                            acts.run((char) c);
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ServerInSocket.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        };
        ThreadListen.start();
        return true;
    }

    public static void send(String s) throws IOException {

        DataOutputStream dOut = new DataOutputStream(Server.getOutputStream());
        System.out.println("sending:" + s);
        dOut.writeBytes(s);
    }

    public static void stop() throws IOException {
        if (ThreadListen != null) {
            Server.close();
            ThreadListen.stop();
            ThreadListen = null;
        }

    }
}
