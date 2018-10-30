/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication8;

import TCP.ClientInSocket;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import static javafxapplication8.JavaFXApplication8.s;

/**
 *
 * @author rodri
 */
public class FXMLDocumentController implements Initializable {

    
    @FXML private GridPane panel_connect;
    @FXML private GridPane panel_connect_start;
    @FXML private TextField ssid,pass,ip;

    private String s="";

    @FXML
    private void show_connect1(ActionEvent event) {
        panel_connect_start.setVisible(true);
         panel_connect.setVisible(false);
    }

    @FXML
    private void connect_start(ActionEvent event) throws IOException {
        //connection_settings();
        panel_connect_start.setVisible(false);
        panel_connect.setVisible(true);
    }
    
    @FXML
    private void graph(ActionEvent event) throws IOException {
        JavaFXApplication8.show_graph();
    }
    
 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //panel_connect.setVisible(false);
    }
    
    public void connection_settings() throws IOException {
        ClientInSocket.Actions actionsReceive = new ClientInSocket.Actions() {
            public void run(char c) {
                
                if (c == '|') {
                String data[] = s.split(";");
                ssid.setText(data[0].replace("ssid:",""));
                pass.setText(data[1].replace("pass:",""));
                ip.setText(data[2].replace("ip:",""));
                s="";
                }
                else s = s + Character.toString(c);
            }
        };
        ClientInSocket.Start(actionsReceive);
    }

}
