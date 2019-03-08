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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.event.KeyEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author rodri
 */
public class MainController implements Initializable {

    int index;
    TextField tf;
    @FXML
    private GridPane panel_connect, panel_configure_wifi, panel_gesture;
    @FXML
    private TextField ssid, pass, ip;

    @FXML
    private Label label_connection;

    private String s = "";

    private List<Button> buttonShow = new ArrayList<>();
    private List<Button> buttonDel = new ArrayList<>();
    public List<Gestures> gestos = new ArrayList<>();

    @FXML
    private void show_panel_connection(ActionEvent event) {
        panel_connect.setVisible(true);
        panel_configure_wifi.setVisible(false);
        panel_gesture.setVisible(false);
    }

    @FXML
    private void show_gesture(ActionEvent event) throws IOException {
        panel_gesture.setVisible(true);
        panel_connect.setVisible(false);
        Node n0 = panel_gesture.getChildren().get(0);
        Node n1 = panel_gesture.getChildren().get(1);
        panel_gesture.getChildren().clear();
        panel_gesture.add(n0, 0, 0);
        panel_gesture.add(n1, 1, 0);
        System.out.println(panel_gesture.getChildren().size());
        panel_configure_wifi.setVisible(false);
        GestureController.getGestures();

        Label l = new Label();
        Button b = new Button();
        for (int i = 0; i < GestureController.gestos.size(); i++) {
            int a = i;
            final Gestures g = GestureController.gestos.get(i);
            l = new Label();
            l.setText(g.name);
            b = new Button();
            final TextField tf = new TextField();
            b.setPrefWidth(panel_gesture.getWidth() / 3);
            b.setText("Mostrar");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Main.show_graph();
                        Main.Graph.setGesture(GestureController.gestos.get(a));
                        //System.out.println(a);
                        System.out.println(GestureController.gestos.get(a));
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            panel_gesture.add(l, 0, i + 1);
            panel_gesture.add(b, 1, i + 1);

            b = new Button();
            b.setPrefWidth(panel_gesture.getWidth() / 3);

            b.setText("Delete");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Gestures g2 = g;
                    GestureController.gestos.remove(g);
                    try {
                        GestureController.saveGesture();
                        show_gesture(event);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            panel_gesture.add(b, 2, i + 1);
            tf.setOnKeyPressed(key_event ->{
                tf.setText(key_event.getCode().name());
                g.default_action = key_event.getCode().name();
                
            });
            tf.setOnKeyReleased(key_event ->{
                tf.setText(key_event.getCode().name());
                
            });
            

            panel_gesture.add(tf, 3, i + 1);
        }
        //panel_gesture.set
    }

    @FXML
    private void show_saved_gesture(ActionEvent event) {
    }

    @FXML
    void connection_start(ActionEvent event) throws IOException {
        ConnectionController.connect_to_gestband(ip.getText());
        setConnection();
    }

    @FXML
    private void configure_wifi(ActionEvent event) throws IOException {
        if (ConnectionController.CONNECTION) {
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
        ConnectionController.CONNECTION = false;
        ClientInSocket.stop();
        setConnection();
    }

    @FXML
    private void send_wifi_settings(ActionEvent event) throws IOException {
        String s = "wifi;" + ssid.getText() + ";" + pass.getText() + ";|";
        ClientInSocket.send(s);
    }

    private void setConnection() throws IOException {
        if (ConnectionController.CONNECTION) {
            label_connection.setText("Conectado");
             ClientInSocket.send("send;|");
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
