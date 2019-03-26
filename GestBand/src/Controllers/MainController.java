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
import APP.Profiles;
import APP.Settings;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

/**
 *
 * @author rodri
 */
public class MainController implements Initializable {

    @FXML
    private GridPane pane_menu, pane_content;

    @FXML
    private Label title, status;

    TextField ssid, pass;
    Button activation;

    public static final String C_RED = "#bf360c";
    public static final String C_GREEN = "#00c853";
    public static final String C_YELLOW = "#ffb300";
    public static final String C_BLUE = "#1e88e5";

    private int content_i, content_j;

    private void show_pulseira() {
        clear();
        title.setText("Pulseira");
        if (!ConnectionController.CONNECTION) {
            TextField ip = new TextField();
            ip.setText(Settings.get("last_ip"));
            Button b = new Button("Conectar");
            b.setOnAction(event -> {
                try_connect(ip.getText());
            });
            b.setPrefWidth(400);
            println(lHeader("Conectar com a pulseira"), 2);
            println(new Label("IP GestBand"), 1);
            println(ip, 1);
            println(new Label("*Verifique na sua pulseira o ip"), 2);
            println(b, 1);
        } else {
            Button b;
            println(lHeader("Geral"), 2);
            set_activation();
            println(activation, 1);
            b = new Button("Desconectar");

            b.setPrefWidth(250);
            b.setOnAction(event -> {
                ConnectionController.disconnect();
                show_pulseira();
                set_connection();
            });
            println(b, 1);

            println(lHeader("Gestos"), 2);
            b = new Button("Criar novo gesto");
            b.setPrefWidth(250);
            b.setOnAction(event -> {
                show_graph();
            });
            println(b, 1);

        }

    }

    private void show_graph() {
        ClientInSocket.send("send;|");
        Main.show_graph();
    }

    private void show_gestos() {
        clear();
        title.setText("Gestos");
        ComboBox profiles = new ComboBox();
        Label l = new Label();
        Button b = new Button();
        l.setText("Nome do perfil:");
        TextField tf_profilename = new TextField();
        Profiles p = ProfileController.getSet();
        if (p != null) {
            tf_profilename.setText(p.name);
        }

        print(l, 1);
        println(tf_profilename, 1);

        for (int i = 0; i < GestureController.gestos.size(); i++) {
            int a = i;
            final Gestures g = GestureController.gestos.get(i);
            final CheckBox cb = new CheckBox();
            if (p != null) {
                cb.setSelected(p.gestos.get(i));
            }
            cb.setOnAction(value -> {
                g.is_check = cb.selectedProperty().get();
            });
            l = new Label();
            l.setText(g.name);
            b = new Button();
            final TextField tf = new TextField();
            b.setPrefWidth(200);
            b.setText("Mostrar");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.show_graph();
                    Main.Graph.setGesture(GestureController.gestos.get(a));
                }
            });
            print(l, 1);
            print(b, 1);

            b = new Button();
            b.setPrefWidth(200);

            b.setText("Delete");
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Gestures g2 = g;
                    GestureController.gestos.remove(g);
                    GestureController.saveGesture();
                    show_gestos();

                }
            });
            print(b, 1);
            tf.setOnKeyPressed(key_event -> {
                tf.setText(key_event.getCode().name());
                g.default_action = key_event.getCode().name();
                GestureController.saveGesture();

            });
            tf.setOnKeyReleased(key_event -> {
                tf.setText(key_event.getCode().name());

            });
            tf.setText(g.default_action);
            print(tf, 1);
            println(cb, 1);

        }

        b = new Button();
        b.setPrefWidth(200);

        b.setText("Deletar Perfil");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (ProfileController.perfis.size() > 0) {
                    if (ProfileController.set != -1) {
                        ProfileController.perfis.remove(ProfileController.set);
                        ProfileController.saveProfile();
                        System.out.println("Perfil deletado");
                        ProfileController.set = -1;
                        show_gestos();
                    } else if (ProfileController.set == -1) {
                        System.out.println("Nenhum perfil selecionado");
                    } else {
                        System.out.println("Perfil nao existente");
                    }
                } else {
                    System.out.println("Lista de perfils vazia");
                }

            }

        });

        profiles.setOnAction(value -> {
            ProfileController.set(profiles.getSelectionModel().getSelectedIndex());
            show_gestos();
        });

        
        for (int i = 0; i < ProfileController.perfis.size(); i++) {
            profiles.getItems().add(ProfileController.perfis.get(i).name);
        }
        
        if(ProfileController.set != -1){
            System.out.println("dfhdfhdf");
            profiles.getSelectionModel().select(ProfileController.set);
        }
        print(profiles, 1);

        print(b, 1);

        b = new Button();
        b.setPrefWidth(200);

        b.setText("Criar Perfil");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (p == null) {
                    ProfileController.perfis.add(new Profiles(tf_profilename.getText()));
                } else {
                    p.save(tf_profilename.getText());
                }
                ProfileController.saveProfile();
                System.out.println("Perfil Criado");
                GestureController.clearCheck();
                show_gestos();

            }
        });

        println(b, 2);
    }

    private void show_app() {
        pane_content.getChildren().clear();
        title.setText("Aplicações");

    }

    private void show_config() {
        clear();
        title.setText("Configurações");
        if (ConnectionController.CONNECTION) {
            ClientInSocket.send("send_wifi;|");
            Button b;
            println(lHeader("Wifi"), 2);
            print(new Label("SSID"), 1);
            println(ssid, 1);
            print(new Label("Senha"), 1);
            println(pass, 1);

            b = new Button("Reniciar");
            b.setOnAction(event -> {
                ConnectionController.restart();
                set_connection();
                show_pulseira();
            });
            b.setPrefWidth(250);
            print(b, 1);

            b = new Button("Salvar");
            b.setOnAction(event -> {
                send_wifi_settings(ssid.getText(), pass.getText());
            });
            b.setPrefWidth(250);
            println(b, 1);

        } else {
            print(new Label("Conecte-se com sua pulseira para poder realizar as configurações"), 1);
        }
    }

    private void send_wifi_settings(String ssid, String pass) {
        String s = "wifi;" + ssid + ";" + pass + ";|";
        ClientInSocket.send(s);
    }

    public void set_wifi(String[] data) {
        ssid.setText(data[1].replace("ssid:", ""));
        pass.setText(data[2].replace("pass:", ""));
    }

    public void set_status(String text, String color) {
        status.setText(text);
        status.setStyle("-fx-background-color:" + color + ";-fx-alignment:center");
    }

    public void set_connection() {
        if (ConnectionController.CONNECTION) {
            set_status("GestBand Conectada", C_GREEN);
            //ClientInSocket.send("send;|");
        } else {
            set_status("GestBand Desconectada", C_RED);
        }
    }

    public void try_connect(String ip) {
        set_status("Conectando...", C_YELLOW);
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                return ConnectionController.connect_to_gestband(ip);
            }

            @Override
            protected void succeeded() {
                if (ConnectionController.CONNECTION) {
                    set_connection();
                    Settings.set("last_ip", ip);
                    DTWController.ACTIVE = true;
                    show_pulseira();

                } else {
                    set_status("Não foi possível se conectar. Verifique a conexão ou o IP", MainController.C_RED);
                }
            }

        };
        new Thread(t).start();
    }

    public void print(Node o, int span) {
        pane_content.add(o, content_j, content_i, span, 1);
        content_j += span;
    }

    public void println(Node o, int span) {
        print(o, span);
        content_i++;
        content_j = 0;
    }

    public void clear() {
        content_i = 0;
        content_j = 0;
        pane_content.getChildren().clear();
    }

    /*
    





     @FXML
     private void send_wifi_settings(ActionEvent event) throws IOException {
     String s = "wifi;" + ssid.getText() + ";" + pass.getText() + ";|";
     ClientInSocket.send(s);
     }





     */
    public void setMenu() {
        Button b = buttonMenu("Pulseira");
        b.setOnAction(event -> {
            show_pulseira();
        });
        pane_menu.add(b, 0, 0);
        b = buttonMenu("Gestos");
        b.setOnAction(event -> {
            show_gestos();
        });
        pane_menu.add(b, 0, 1);
        b = buttonMenu("Aplicações");
        b.setOnAction(event -> {
            show_app();
        });
        pane_menu.add(b, 0, 2);
        b = buttonMenu("Configurações");
        b.setOnAction(event -> {
            show_config();
        });
        pane_menu.add(b, 0, 3);
    }

    public Button buttonMenu(String text) {
        Button b = new Button();
        b.setText(text);
        b.setPrefWidth(250);
        b.setPrefHeight(50);
        b.getStyleClass().add("button-menu");
        return b;
    }

    public static Label lHeader(String text) {
        Label l = new Label();
        l.setText(text);
        l.getStyleClass().add("header");
        return l;
    }

    private void set_activation() {
        if (DTWController.ACTIVE) {
            ClientInSocket.send("send;|");

            activation.setText("Parar reconhecimento");
            activation.setStyle("-fx-background-color:" + C_RED);
            set_status("A pulseira está reconhecendo os seus gestos..", C_BLUE);
        } else {
            ClientInSocket.send("stop;|");
            activation.setText("Iniciar reconhecimento");
            activation.setStyle("-fx-background-color:" + C_BLUE);
            set_connection();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMenu();
        show_pulseira();
        ssid = new TextField();
        pass = new TextField();
        ssid.setPrefWidth(250);
        pass.setPrefWidth(250);

        activation = new Button();
        activation.setPrefWidth(250);
        activation.setOnAction(event -> {
            DTWController.ACTIVE = !DTWController.ACTIVE;
            set_activation();
        });

        if (Settings.get("last_ip") != "undefined") {
            try_connect(Settings.get("last_ip"));
        }
    }

}
