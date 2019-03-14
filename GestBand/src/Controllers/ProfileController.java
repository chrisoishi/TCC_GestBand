/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Gestures;
import APP.Profiles;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class ProfileController {
    public static int set = -1;
    public static List<Profiles> perfis = new ArrayList<>();
    
    public static void set(int index){
        set = index;
        Profiles p = perfis.get(index);
        GestureController.gestos_current.clear();
        for (int i = 0; i < GestureController.gestos.size(); i++) {
            final Gestures g =  GestureController.gestos.get(i);
            g.is_check = p.gestos.get(i);
            g.default_action = p.comandos.get(i);
            if(g.is_check)GestureController.gestos_current.add(g);
        }
    }
    public static Profiles getSet(){
        if(set>-1)return perfis.get(set);
        else return null;
    }
}
