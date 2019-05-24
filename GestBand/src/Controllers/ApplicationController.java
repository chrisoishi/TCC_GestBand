/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author chris
 */
public class ApplicationController {
    public static List<Application> apps = new ArrayList<Application>();
    
    public static void load()throws FileNotFoundException, IOException {
        apps.clear();
        File file = new File("apps.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String st, s2[];
        s2 = new String[1];
        Application app = new Application();
        while ((st = reader.readLine()) != null) {
            if (st.contains("name:")) {
                app = new Application();
                app.name = st.replace("name:","");
            } else if (st.contains("--end")) {
                apps.add(app);
            } else if(st.contains("command:")){
                String[] data = st.replace("command:","").split(";");
                app.add(data[0],data[1]);
            }
        }
        reader.close();    
}
    
    
    public static class Application{
        public String name;
        public Map<String,String> actions;
        public Application(){
            actions = new HashMap<String,String>();
        }
        public void add(String command,String description){
            actions.put(command,description);
        }
    }
}
