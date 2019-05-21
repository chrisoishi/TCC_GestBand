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
import java.util.Map;

/**
 *
 * @author Chris
 */
public class Profiles {

    public String name;
    public Map<String, ProfileData> data = new HashMap<String, ProfileData>();
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

    public void clear() {
        data.clear();
        gestos.clear();
        comandos.clear();
    }

    public void set(String id, String action, boolean active) {
        if (!data.containsKey(id)) {
            data.put(id, new ProfileData(action,active));
        } else {
            ProfileData pd= data.get(id);
            if(action==null)action = pd.action;
            data.replace(id, new ProfileData(action,active));
        }
    }
    public void remove(String id){
        data.remove(id);
    }
    
    public List<String> listString(){
        List<String> s = new ArrayList<>();
        for(String key : data.keySet()){
            s.add(key+":"+data.get(key).toString());
        }
        return s;
    }

    public class ProfileData {
        public String action;
        public boolean active;
        public ProfileData(String action,boolean active){
            this.action = action;
            this.active = active;
        }
        public String toString(){
            return action+";"+Boolean.toString(active);
        };
    }
}
