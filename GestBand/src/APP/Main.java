/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import Controllers.*;
import TCP.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rodri
 */
public class Main extends Application {

    public static String s = "";
    public static Graph Graph;
    public static Parent root;
    public static URL w_graph;
    public static MainController controller;

    public static boolean CONNECTION = false;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        root = loader.load();
        controller = loader.getController();
        w_graph = getClass().getResource("/FXML/blank.fxml");

        Scene scene = new Scene(root);
        stage.setTitle("GestBand App");
        //scene.getStylesheets().add("/javafxapplication8/Style.css");
        stage.setScene(scene);
        stage.setOnCloseRequest(WindowEvent -> {
            try {
                ClientInSocket.stop();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage.show();
        Main.Graph = new Graph();
        
                Gestures g = new Gestures();

        g.name = "sdgsdgsd";
        System.out.println(g.toString());

    }

    public static void show_graph() throws IOException {
        FXMLLoader loader  = new FXMLLoader(w_graph);
        Main.Graph = new Graph();
        
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(WindowEvent ->
        {
            try {
                ClientInSocket.send("stop;|");
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        Graph.start(stage);

    }

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
                Graph.print(data[1]);
                break;

        }
    }



    public static void main(String[] args) throws IOException {

        
        launch(args);

    }

}
