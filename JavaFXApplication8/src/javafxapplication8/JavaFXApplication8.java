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


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("GestBand App");
        //scene.getStylesheets().add("/javafxapplication8/Style.css");
        stage.setScene(scene);
        stage.show();

    }

    

    public static void main(String[] args) throws IOException {
        launch(args);

    }

}
