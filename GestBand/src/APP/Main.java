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

    public static Graph Graph;
    public static Parent root;
    public static URL w_graph;
    public static MainController controller;

    @Override
    public void start(Stage stage) throws Exception {
        Settings.read();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));

        root = loader.load();

        w_graph = getClass().getResource("/FXML/blank.fxml");

        Scene scene = new Scene(root);
        stage.setTitle("GestBand App");
        //scene.getStylesheets().add("/javafxapplication8/Style.css");
        stage.setScene(scene);
        stage.setOnCloseRequest(WindowEvent -> {
            try {
                Settings.save();
                ClientInSocket.stop();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage.show();
        controller = loader.getController();
        Main.Graph = new Graph();
        Simulation.init();
        DTWController.init();
        GestureController.getGestures();

    }

    public static void show_graph() {
        FXMLLoader loader = new FXMLLoader(w_graph);
        Main.Graph = new Graph();

        Scene scene;
        try {
            scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest(WindowEvent
                    -> {
                if (ConnectionController.CONNECTION) {
                    //ClientInSocket.send("stop;|");
                }

            });
            Graph.start(stage);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws IOException {

        launch(args);

    }

}
