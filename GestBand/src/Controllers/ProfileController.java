/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Gestures;
import APP.Main;
import APP.Profiles;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class ProfileController {

    public static int set = -1;
    public static List<Profiles> perfis = new ArrayList<>();

    public static void set(int index) {
        set = index;
        Profiles p = perfis.get(index);
        GestureController.gestos_current.clear();
        for (int i = 0; i < GestureController.gestos.size(); i++) {
            final Gestures g = GestureController.gestos.get(i);
            if(i<p.gestos.size())g.is_check = p.gestos.get(i);
            else g.is_check  = false;
            if(i<p.comandos.size())g.default_action = p.comandos.get(i);
            else g.default_action="";
            
            if (g.is_check) {
                GestureController.gestos_current.add(g);
            }
        }
    }
    public static void nextSet(){
        set++;
        if(set==perfis.size())set(0);
        else {
            set(set);
        }
    }

    public static Profiles getSet() {
        if (set > -1) {
            return perfis.get(set);
        } else {
            return null;
        }
    }

    public static void saveProfile() {
        File file = new File("profiles.txt");
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            for (int i = 0; i < perfis.size(); i++) {
                writer.append(perfis.get(i).name);
                writer.append(System.getProperty("line.separator"));
                writer.append(perfis.get(i).gestos.toString());
                writer.append(System.getProperty("line.separator"));
                writer.append(perfis.get(i).comandos.toString());
                writer.append(System.getProperty("line.separator"));
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GestureController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void getProfiles() throws FileNotFoundException, IOException {
        System.out.println("read_profile");
        perfis.clear();
        File file = new File("profiles.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st, s2[];
        s2 = new String[1];
        Profiles p = new Profiles("");
        int count = 0;
        float[][] temp = new float[6][200];
        boolean btemp;
        while ((st = reader.readLine()) != null) {
            if (count == 0) {
                p = new Profiles(st);
                count++;
                p.clear();
            } else if (count == 1) {
                st = st.substring(1, st.length() - 1);
                s2 = st.split(", ");
                for (int i = 0; i < s2.length; i++) {
                    btemp = s2[i].equals("true")?true:false;
                    if(i<GestureController.gestos.size())p.gestos.add(btemp);
                }
                count++;
            } else if (count == 2) {
                st = st.substring(1, st.length() - 1);
                s2 = st.split(", ");
                System.out.println(s2.length);
                for (int i = 0; i < s2.length; i++) {
                    if(i<GestureController.gestos.size())p.comandos.add(s2[i]);
                }
                count = 0;
                perfis.add(p);
            }

        }
        reader.close();
    }
    
    public static void sync_deleted_gesture(int index){
        List<Profiles> aux_perfis = new ArrayList<>();
        Profiles aux;
        for(int i=0;i<perfis.size();i++){
            aux = new Profiles(perfis.get(i).name);
            for(int j=0;j<perfis.get(i).gestos.size();j++){
                if(j!= index && j<GestureController.gestos.size()){
                    aux.gestos.add(perfis.get(i).gestos.get(j));
                    aux.comandos.add(perfis.get(i).comandos.get(j));
                }
            }
            aux_perfis.add(aux);
        }
        perfis = aux_perfis;
        saveProfile();
    }
}
