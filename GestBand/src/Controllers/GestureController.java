/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Gestures;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Chris
 */
public class GestureController {

    public static List<Gestures> gestos = new ArrayList<>();
    public static List<Gestures> gestos_current = new ArrayList<>();

    public static void saveGesture() {
        File file = new File("gestures.txt");
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            for (int i = 0; i < gestos.size(); i++) {
                writer.append(gestos.get(i).toString());
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GestureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getGestures() throws FileNotFoundException, IOException {
        System.out.println("read_gesto");
        gestos.clear();
        File file = new File("gestures.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st, s2[];
        s2 = new String[1];
        String name = "";
        String id = "";
        Gestures g;
        float[][] temp = new float[6][200];
        int count = 0;
        while ((st = reader.readLine()) != null) {

            if (count == 0) {
                temp = new float[6][200];
                s2 = st.split(";");
                id = "";
                if (s2.length > 1) {
                    name = s2[0];
                    id = s2[1];
                } else {
                    name = st;
                }
            } else {
                s2 = st.split(";");
                for (int i = 0; i < s2.length; i++) {

                    temp[count - 1][i] = Float.parseFloat(s2[i]);
                }

            }
            if (count == 6) {
                count = 0;
                g = new Gestures(name, s2.length, temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
                g.id = id;
                gestos.add(g);

            } else {
                count++;
            }
        }
        reader.close();
    }

    public static void clearCheck() {
        for (int i = 0; i < gestos.size(); i++) {
            final Gestures g = gestos.get(i);
            g.is_check = false;
        }
    }

    public static Gestures getById(String id) {
        for(Gestures g : gestos){
            if(g.id.equals(id))return g;
        }
        return null;
    }
}
