/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author chris
 */
public class Drawer {

    private GridPane pane;
    private int content_i, content_j;

    public Drawer(GridPane p) {
        pane = p;
    }

    public Drawer() {
        pane = new GridPane();
        pane.setStyle("-fx-hgap: 10;-fx-vgap: 10;");
    }

    public GridPane get() {
        return pane;
    }

    public void print(Node o, int span) {
        pane.add(o, content_j, content_i, span, 1);
        content_j += span;
    }

    public void println(Node o, int span) {
        print(o, span);
        content_i++;
        content_j = 0;
    }

    public void text(String s, int span, boolean breakLine) {
        Label l = new Label(s);
        if (breakLine) {
            println(l, span);
        } else {
            print(l, span);
        }
    }

    public void header(String text, int span, boolean breakLine) {
        Label l = new Label();
        l.setText(text);
        l.getStyleClass().add("header");
        if (breakLine) {
            println(l, span);
        } else {
            print(l, span);
        }
    }

    public void clear() {
        content_i = 0;
        content_j = 0;
        pane.getChildren().clear();
    }
}
