/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import static Controllers.GestureController.gestos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class Settings {

    public static HashMap<String, String> data = new HashMap<String, String>();

    public static void read() {
        data.clear();
        File file = new File("settings.ini");
        String st;
        String[] attr;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((st = reader.readLine()) != null) {
                attr = st.split("=", 2);
                data.put(attr[0], attr[1]);
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void save() {
        File file = new File("settings.ini");
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            for (String key : data.keySet()) {
                writer.append(key + "=" + data.get(key));
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String get(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        } else {
            return "undefined";
        }

    }

    public static void set(String key, String value) {
        if (data.containsKey(key)) {
            data.replace(key, value);
        } else {
            data.put(key, value);
        }
    }
}
