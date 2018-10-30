/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication8;

import TCP.*;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rodri
 */
public class JavaFXApplication8 extends Application {

    public static String s = "";
    public static AnimatedLineChart Graph;
    public static Parent root,w_graph;

    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        w_graph = FXMLLoader.load(getClass().getResource("blank.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("GestBand App");
        //scene.getStylesheets().add("/javafxapplication8/Style.css");
        stage.setScene(scene);
        stage.show();
        JavaFXApplication8.Graph = new AnimatedLineChart();
        
    }
    
    public static void show_graph() throws IOException{
        Scene scene = new Scene(w_graph);
        Stage stage = new Stage();
        stage.setScene(scene);
        Graph.start(stage);
        startConnection();
        
        
    }
    
    
    private static void startConnection() throws IOException {
        ServerInSocket.Actions actionsReceive = new ServerInSocket.Actions() {
            public void run(char c) {

                if (c == '|') {
                    Graph.print(s);
                    s = "";
                } else {

                    s = s + Character.toString(c);
                }
            }
        };
        ServerInSocket.Start(actionsReceive);
    }
    

    public static void main(String[] args) throws IOException {
        
        launch(args);

    }

}
