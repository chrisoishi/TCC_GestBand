/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Main;
import static APP.Main.Graph;
import static APP.Main.controller;
import TCP.ClientInSocket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author Chris
 */
public class ConnectionController {

    private static String s = "";
    public static boolean CONNECTION = false;
    public static boolean CONNECTING = false;
    public static Thread thread;

    public static boolean connect_to_gestband(String ip) throws IOException {
        ClientInSocket.Actions actionsReceive = new ClientInSocket.Actions() {
            public void run(char c) {
                if (c == '|') {
                    handler_receive(s);
                    s = "";
                } else {
                    s = s + Character.toString(c);
                }
            }
        };
        CONNECTION = ClientInSocket.Start(actionsReceive, ip);
        return CONNECTION;
    }

    public static void disconnect() {
        try {
            ClientInSocket.stop();
            CONNECTION = false;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void restart() {
        ClientInSocket.send("restart;|");
        DTWController.ACTIVE = false;
        CONNECTION = false;
        disconnect();
    }

    private static void handler_receive(String s) {
        String data[] = s.split(";");
        switch (data[0]) {
            case "wifi":
                controller.set_wifi(data);
                break;
            case "data":
                DTWController.receive(data[1]);

                break;
            case "profile":
                ProfileController.nextSet();
                ClientInSocket.send("profile;" + ProfileController.getSet().name + ";|");
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return true;
                    }

                    @Override
                    protected void succeeded() {
                        controller.set_status();
                        controller.show_profiles();
                    }
                };
                new Thread(t).start();

                break;

        }
    }
}
