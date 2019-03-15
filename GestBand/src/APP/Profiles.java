/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import Controllers.GestureController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Chris
 */
public class Profiles {

    public String name;
    public List<Boolean> gestos = new ArrayList<>();
    public List<String> comandos = new ArrayList<>();

    public Profiles(String name) {
        save(name);
    }

    public void save(String name) {
        this.name = name;
        gestos.clear();
        comandos.clear();
        for (int i = 0; i < GestureController.gestos.size(); i++) {
            final Gestures g = GestureController.gestos.get(i);
            gestos.add(g.is_check);
            comandos.add(g.default_action);
        }
    }
    public void clear(){
        gestos.clear();
        comandos.clear();
    }
}
