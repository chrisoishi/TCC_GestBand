/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import Controllers.*;
import TCP.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import static javafx.application.Application.launch;
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
    public static List<Gestures> gestos = new ArrayList<>();

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

    public static void saveGesture() throws IOException {
        File file = new File("myfile.txt");
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);

        for (int i = 0; i < gestos.size(); i++) {
            writer.append(gestos.get(i).toString());
        }
        writer.close();

    }

    public static void getGestures() throws FileNotFoundException, IOException {
        gestos.clear();
        File file = new File("myfile.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st, s2[];
        String name = "";
        Gestures g;
        float[][] temp = new float[6][30];
        int count = 0;
        while ((st = reader.readLine()) != null) {
            if (count == 0) {
                name = st;
                System.out.println(st);
            } else {
                s2 = st.split(";");
                for (int i = 0; i < s2.length; i++) {

                    temp[count - 1][i] = Float.parseFloat(s2[i]);
                   // System.out.print(temp[count - 1][i] + " ");
                }

               // System.out.println();
            }
            if (count == 6) {
                count = 0;
                g = new Gestures(name, temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
                gestos.add(g);

            } else {
                count++;
            }
        }

        reader.close();
    }

    public static void show_graph() throws IOException {
        FXMLLoader loader = new FXMLLoader(w_graph);
        Main.Graph = new Graph();

        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(WindowEvent
                -> {
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
