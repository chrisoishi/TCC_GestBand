/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Gestures;
import APP.Drawer;
import TCP.ClientInSocket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import APP.Main;
import APP.Profiles;
import APP.Profiles.ProfileData;
import APP.Settings;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    Drawer drawer;

    public static final String C_RED = "#bf360c";
    public static final String C_GREEN = "#00c853";
    public static final String C_YELLOW = "#ffb300";
    public static final String C_BLUE = "#1e88e5";

    public boolean IS_CREATING_PROFILE = false;

    public interface Actions {

        public void run();
    }

    private void show_pulseira() {
        drawer.clear();
        title.setText("Pulseira");
        if (!ConnectionController.CONNECTION) {
            TextField ip = new TextField();
            ip.setText(Settings.get("last_ip"));
            Button b;
            if (ConnectionController.CONNECTING && ConnectionController.BLUETOOTH_STATUS == 0) {
                b = new Button("Cancelar");
                b.setOnAction(event -> {
                    ConnectionController.CONNECTING = false;
                    ConnectionController.thread.stop();
                    show_pulseira();
                    set_status();
                });
            } else {
                b = new Button("Conectar via Wifi");
                b.setOnAction(event -> {
                    try_connect(ip.getText());
                    show_pulseira();
                });
            }
            b.setPrefWidth(400);
            drawer.header("Conectar com a pulseira", 2, true);
            drawer.text("Escolha umas das forma de conexão", 2, true);
            drawer.header("Wifi", 2, true);
            drawer.println(new Label("IP GestBand"), 1);
            drawer.println(ip, 1);
            drawer.println(new Label("*Verifique na sua pulseira o ip"), 2);
            drawer.println(b, 2);
            drawer.header("Bluetooth", 2, true);
            b = new Button("Conectar via Bluetooth");
            b.setPrefWidth(400);
            b.setOnAction(event -> {
                try_connect_bluetooth();
                show_pulseira();
            });
            drawer.println(b, 2);
        } else {
            Button b;
            drawer.header("Geral", 2, true);
            set_activation();
            drawer.println(activation, 1);
            b = new Button("Desconectar");

            b.setPrefWidth(250);
            b.setOnAction(event -> {
                ConnectionController.disconnect();
                show_pulseira();
                set_status();
            });
            drawer.println(b, 1);

            drawer.header("Gestos", 2, true);
            b = new Button("Criar novo gesto");
            b.setPrefWidth(250);
            b.setOnAction(event -> {
                show_graph();
            });
            drawer.println(b, 1);

        }

    }

    private void show_graph() {
        ConnectionController.protocol.startDataSend();
        Main.show_graph("Criar gesto", false);
    }

    private void show_gestos() {
        drawer.clear();
        title.setText("Gestos");
        drawer.text("Abaixo está a lista de todos os gestos gravados", 2, true);
        ComboBox profiles = new ComboBox();
        Button b = new Button();
        Profiles p = ProfileController.getSet();

        for (int i = 0; i < GestureController.gestos.size(); i++) {
            int a = i;
            final Gestures g = GestureController.gestos.get(i);
            b = new Button();
            final TextField tf = new TextField();
            b.setPrefWidth(200);
            b.setText(g.name);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.show_graph(g.name, true);
                    Main.Graph.setGesture(GestureController.gestos.get(a));
                }
            });
            drawer.print(b, 1);

            b = new Button();
            b.setPrefWidth(100);

            b.setText("Excluir");
            b.setStyle("-fx-background-color:" + MainController.C_RED);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Deseja mesmo excluir esse gesto?");
                    //alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setResult(ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        GestureController.gestos.remove(g);
                        GestureController.saveGesture();
                        show_gestos();
                        ProfileController.sync_deleted_gesture(a);
                    }

                }
            });
            drawer.println(b, 1);
            tf.setOnKeyPressed(key_event -> {
                tf.setText(key_event.getCode().name());
                g.default_action = key_event.getCode().name();
                GestureController.saveGesture();

            });
            tf.setOnKeyReleased(key_event -> {
                tf.setText(key_event.getCode().name());

            });

        }

    }

    public void show_profiles() {
        drawer.clear();
        title.setText("Perfis");
        ComboBox profiles = new ComboBox();
        Button b = new Button();

        TextField tf_profilename = new TextField();
        Profiles p = ProfileController.getSet();
        if (p != null) {
            tf_profilename.setText(p.name);
        }

        profiles.setOnAction(value -> {
            ProfileController.set(profiles.getSelectionModel().getSelectedIndex());
            IS_CREATING_PROFILE = false;
            show_profiles();
            set_status();
        });

        for (int i = 0; i < ProfileController.perfis.size(); i++) {
            profiles.getItems().add(ProfileController.perfis.get(i).name);
        }
        Drawer drawer_select = new Drawer();
        drawer.text("Selecione um perfil:", 1, false);
        drawer.println(drawer_select.get(), 3);
        drawer_select.print(profiles, 1);
        b = new Button("+ Adicionar novo perfil");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProfileController.current = new Profiles("");
                ProfileController.set = -1;
                IS_CREATING_PROFILE = true;
                show_profiles();
                set_status();
            }
        });
        drawer_select.print(b, 1);

        if (ProfileController.set != -1 || IS_CREATING_PROFILE) {
            profiles.getSelectionModel().select(ProfileController.set);

            drawer.text("Nome do perfil", 1, false);
            ComboBox gestos = new ComboBox();
            gestos.setOnAction(value -> {
                ProfileController.current.set(GestureController.gestos.get(gestos.getSelectionModel().getSelectedIndex()).id, "", false);
                show_profiles();
            });

            for (int i = 0; i < GestureController.gestos.size(); i++) {
                gestos.getItems().add(GestureController.gestos.get(i).name);
            }
            tf_profilename.textProperty().set(ProfileController.current.name);
            tf_profilename.setOnKeyReleased(key_event -> {
                ProfileController.current.name = tf_profilename.textProperty().get();
            });
            drawer.println(tf_profilename, 1);
            drawer.text("Adicionar gesto", 1, false);
            drawer.println(gestos, 2);

            Set<String> chaves = ProfileController.current.data.keySet();
            for (String id : chaves) {
                final String ids = id;
                final ProfileData pd = ProfileController.current.data.get(id);
                final Gestures g = GestureController.getById(id);
                final CheckBox cb = new CheckBox();
                cb.selectedProperty().set(pd.active);
                final TextField tf = new TextField();
                tf.textProperty().set(pd.action);

                cb.setOnAction(value -> {
                    g.is_check = cb.selectedProperty().get();
                    ProfileController.current.set(ids, null, g.is_check);
                });
                b = new Button();

                drawer.text(g.name, 1, false);

                b = new Button();
                b.setPrefWidth(200);

                tf.setOnKeyPressed(key_event -> {
                    tf.setText(key_event.getCode().name());
                    g.default_action = key_event.getCode().name();
                    //GestureController.saveGesture();key_event.getCode().name();
                    cb.selectedProperty().set(true);
                    g.is_check = cb.selectedProperty().get();

                });
                tf.setOnKeyReleased(key_event -> {
                    tf.setText(key_event.getCode().name());
                    ProfileController.current.set(ids, key_event.getCode().name(), true);
                });
                if (ProfileController.set == -1) {
                    g.default_action = "";
                }
                //tf.setText(g.default_action);
                drawer.print(tf, 1);
                drawer.print(cb, 1);
                b = new Button("Remover");
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        ProfileController.current.remove(ids);
                        show_profiles();
                    }
                });
                drawer.println(b, 1);

            }
            Drawer drawer2 = new Drawer();
            drawer.print(drawer2.get(), 2);
            b = new Button();
            b.setPrefWidth(200);
            if (!IS_CREATING_PROFILE) {
                b.setText("Excluir");
                b.setStyle("-fx-background-color:" + MainController.C_RED);
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("Deseja mesmo excluir esse perfil?");
                        //alert.initModality(Modality.APPLICATION_MODAL);
                        alert.setResult(ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {
                            ProfileController.perfis.remove(ProfileController.set);
                            ProfileController.saveProfile();
                            System.out.println("Perfil deletado");
                            ProfileController.set = -1;
                            show_profiles();
                        }

                    }

                });

                drawer2.print(b, 1);
            }
            b  = new Button();
            b.setPrefWidth(200);
            b.setText("Salvar");
            b.setStyle("-fx-background-color:" + MainController.C_GREEN);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (tf_profilename.getText() != "") {
                        if (IS_CREATING_PROFILE) {
                            ProfileController.add();
                        } else {
                            ProfileController.update();
                        }
                        ProfileController.saveProfile();
                        GestureController.clearCheck();
                        IS_CREATING_PROFILE = false;
                        show_profiles();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Perfil salvo");
                        //alert.initModality(Modality.APPLICATION_MODAL);
                        alert.show();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Digite o nome do perfil");
                        //alert.initModality(Modality.APPLICATION_MODAL);
                        alert.show();
                    }
                }
            });

            drawer2.println(b, 2);

        }
    }

    private void show_config() {
        drawer.clear();
        title.setText("Configurações");
        if (ConnectionController.CONNECTION) {
            Button b;
            if (!ConnectionController.IS_BLUETOOTH) {

                ClientInSocket.send("send_wifi;|");

                drawer.header("Wifi", 2, true);
                drawer.print(new Label("SSID"), 1);
                drawer.println(ssid, 1);
                drawer.print(new Label("Senha"), 1);
                drawer.println(pass, 1);

                b = new Button("Reniciar");
                b.setOnAction(event -> {
                    ConnectionController.restart();
                    set_status();
                    show_pulseira();
                });
                b.setPrefWidth(250);
                drawer.print(b, 1);

                b = new Button("Salvar");
                b.setOnAction(event -> {
                    send_wifi_settings(ssid.getText(), pass.getText());
                });
                b.setPrefWidth(250);
                drawer.println(b, 1);
            }
            drawer.header("Testes", 2, true);
            b = new Button("Velocidade de transmissão");
            b.setOnAction(event -> {
            });
            drawer.print(b, 1);

        } else {
            drawer.print(new Label("Conecte-se com sua pulseira para poder realizar as configurações"), 1);
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

    public void set_status() {
        if (ConnectionController.BLUETOOTH_STATUS == 4) {
            set_status("Não foi possível encontrar a pulseira, acesse suas configurações de bluetooth", C_RED);
        } else if (ConnectionController.CONNECTING) {
            if (ConnectionController.BLUETOOTH_STATUS == 1) {
                set_status("Procurando pela pulsera...", C_YELLOW);
            } else if (ConnectionController.BLUETOOTH_STATUS == 2) {
                set_status("Pulseira encontrada, tentando conectar...", C_YELLOW);
            } else if (ConnectionController.BLUETOOTH_STATUS == 5) {
                set_status("Conectando via bluetooth..", C_YELLOW);
            } else {
                set_status("Conectando via wifi...", C_YELLOW);
            }
        } else if (!ConnectionController.CONNECTION) {
            set_status("GestBand Desconectada", C_RED);

        } else if (DTWController.ACTIVE) {
            if (ProfileController.set == -1) {
                set_status("Defina um perfil de ações para seus gestos", C_YELLOW);
            } else {
                set_status("PERFIL: " + ProfileController.getSet().name + " | Reconhecendo gestos...", C_BLUE);
            }
        } else {
            set_status("GestBand Conectada | Ative o reconhecimento para usar", C_GREEN);
        }
    }

    public void try_connect(String ip) {
        if (!ConnectionController.CONNECTING) {
            ConnectionController.CONNECTING = true;
            set_status();
            Settings.set("last_ip", ip);
            Task t = new Task() {
                @Override
                protected Object call() throws Exception {
                    return ConnectionController.connect_to_gestband(ip);
                }

                @Override
                protected void succeeded() {
                    ConnectionController.CONNECTING = false;
                    if (ConnectionController.CONNECTION) {
                        set_status();
                        //DTWController.ACTIVE = true;
                        show_pulseira();

                    } else {
                        set_status("Não foi possível se conectar. Verifique a conexão ou o IP", MainController.C_RED);
                    }

                }

            };
            ConnectionController.thread = new Thread(t);
            ConnectionController.thread.start();
        }
    }

    public void try_connect_bluetooth() {
        if (!ConnectionController.CONNECTING) {

            ConnectionController.CONNECTING = true;

            Actions acts = new Actions() {
                public void run() {
                    if (ConnectionController.BLUETOOTH_STATUS == 3) {
                        show_pulseira();
                    }
                    set_status();
                }
            };

            ConnectionController.connect_to_gestband_bluetooth(task(acts));
            //set_status();
        }
    }

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
        b = buttonMenu("Perfis");
        b.setOnAction(event -> {
            show_profiles();
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

    private void set_activation() {
        if (DTWController.ACTIVE) {
            ConnectionController.protocol.startDataSend();

            activation.setText("Parar reconhecimento");
            activation.setStyle("-fx-background-color:" + C_RED);

        } else {
            ConnectionController.protocol.stopDataSend();
            activation.setText("Iniciar reconhecimento");
            activation.setStyle("-fx-background-color:" + C_BLUE);
        }
        set_status();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawer = new Drawer(pane_content);
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
            //try_connect(Settings.get("last_ip"));
        }
    }

    public Actions task(Actions acts) {
        return new Actions() {
            public void run() {
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return true;
                    }

                    @Override
                    protected void succeeded() {
                        acts.run();
                    }

                };
                new Thread(t).start();
            }
        };

    }
}
