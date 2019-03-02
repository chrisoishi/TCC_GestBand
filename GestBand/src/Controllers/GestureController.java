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



/**
 *
 * @author Chris
 */
public class GestureController {

    public static List<Gestures> gestos = new ArrayList<>();

    public static void saveGesture() throws IOException {
        File file = new File("gestures.txt");
        FileWriter fw = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fw);

        for (int i = 0; i < gestos.size(); i++) {
            writer.append(gestos.get(i).toString());
        }
        writer.close();

    }

    public static void getGestures() throws FileNotFoundException, IOException {
        System.out.println("read_gesto");
        gestos.clear();
        File file = new File("gestures.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st, s2[];
        s2 = new String[1];
        String name = "";
        Gestures g;
        float[][] temp = new float[6][200];
        int count = 0;
        while ((st = reader.readLine()) != null) {

            if (count == 0) {
                temp = new float[6][200];
                name = st;
            } else {
                s2 = st.split(";");
                for (int i = 0; i < s2.length; i++) {

                    temp[count - 1][i] = Float.parseFloat(s2[i]);
                }

            }
            if (count == 6) {
                count = 0;
                g = new Gestures(name, s2.length, temp[0], temp[1], temp[2], temp[3], temp[4], temp[5]);
                gestos.add(g);
            } else {
                count++;
            }
        }
        reader.close();
    }
}
