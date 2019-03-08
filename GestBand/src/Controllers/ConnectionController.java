/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static APP.Main.Graph;
import static APP.Main.controller;
import TCP.ClientInSocket;
import java.io.IOException;

/**
 *
 * @author Chris
 */
public class ConnectionController {

    private static String s = "";
    public static boolean CONNECTION = false;

    public static void connect_to_gestband(String ip) throws IOException {
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

        }
    }
}
