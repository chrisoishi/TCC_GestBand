/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Gestures;
import TCP.ClientInSocket;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import APP.Main;
import static java.lang.System.in;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author rodri
 */
public class MainController implements Initializable {

    @FXML
    private GridPane panel_connect, panel_configure_wifi, panel_gesture;
    @FXML
    private TextField ssid, pass, ip;

    @FXML
    private Label label_connection;

    private String s = "";

    @FXML
    private void show_panel_connection(ActionEvent event) {
        panel_connect.setVisible(true);
        panel_configure_wifi.setVisible(false);
        panel_gesture.setVisible(false);
    }

    @FXML
    private void show_gesture(ActionEvent event) {
        panel_gesture.setVisible(true);
        panel_connect.setVisible(false);
        panel_configure_wifi.setVisible(false);
        ArrayList<Gestures> gestures = new ArrayList<Gestures>();
        Gestures g = new Gestures();
        g.name = "chris";
        gestures.add(g);
        g = new Gestures();
        g.name = "oishi";
        gestures.add(g);
        Label l = new Label();
        Button b = new Button();
        b.setText("Mostrar");
        l.setText(s);
        for (int i = 0; i < gestures.size(); i++) {
            g = gestures.get(i);
            l = new Label();
            l.setText(g.name);
            b = new Button();
            b.setPrefWidth(panel_gesture.getWidth()/2);
             b.setText("Mostrar");
            panel_gesture.add(l, 0, i+1);
            panel_gesture.add(b, 1, i+1);
        }
        //panel_gesture.set
    }

    @FXML
    private void show_saved_gesture(ActionEvent event) {
    }

    @FXML
    void connection_start(ActionEvent event) throws IOException {
        Main.connect_to_gestband(ip.getText());
        setConnection();
    }

    @FXML
    private void configure_wifi(ActionEvent event) throws IOException {
        if (Main.CONNECTION) {
            ClientInSocket.send("send_wifi;|");
            panel_connect.setVisible(false);
            panel_configure_wifi.setVisible(true);
        }
    }

    @FXML
    private void graph(ActionEvent event) throws IOException {
        ClientInSocket.send("send;|");
        Main.show_graph();
    }

    @FXML
    private void restart(ActionEvent event) throws IOException {
        ClientInSocket.send("restart;|");
        Main.CONNECTION = false;
        ClientInSocket.stop();
        setConnection();
    }

    @FXML
    private void send_wifi_settings(ActionEvent event) throws IOException {
        String s = "wifi;" + ssid.getText() + ";" + pass.getText() + ";|";
        ClientInSocket.send(s);
    }

    private void setConnection() {
        if (Main.CONNECTION) {
            label_connection.setText("Conectado");
        } else {
            label_connection.setText("Desconectado");
        }
    }

    public void set_wifi(String[] data) {
        ssid.setText(data[1].replace("ssid:", ""));
        pass.setText(data[2].replace("pass:", ""));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
