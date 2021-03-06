/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import java.io.Serializable;
import java.util.Random;
import javafx.scene.input.KeyCode;

/**
 *
 * @author chris
 */
public class Gestures {
    public String id;
    public String name;
    public int size;
    public String default_action;
    public boolean is_check;
    public float[] acX = new float[1];
    public float[] acY = new float[1];
    public float[] acZ = new float[1];
    public float[] gX = new float[1];
    public float[] gY = new float[1];
    public float[] gZ = new float[1];
    
    public Gestures(String name,int size,float[] acX, float[] acY, float[] acZ, float[] gX, float[] gY, float[] gZ ){
        this.default_action = "";
        this.size = size;
        this.name= name;
        this.acX = acX;
        this.acY = acY;
        this.acZ = acZ;
        this.gX  = gX;
        this.gY  = gY;
        this.gZ  = gZ;
        this.id = newId();
    }
        public Gestures(String name,float[][] data){
        this.name= name;
        this.acX = data[0];
        this.acY = data[1];
        this.acZ = data[2];
        this.gX  = data[3];
        this.gY  = data[4];
        this.gZ  = data[5];
        this.id = newId();
    }

    public Gestures() {
    }

    public String toString() {
        String s = "";
        if(id.isEmpty())id=newId();
        s += name+";"+id;
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(acX[i]) + ";";
        }
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(acY[i]) + ";";
        }
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(acZ[i]) + ";";
        }
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(gX[i]) + ";";
        }
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(gY[i]) + ";";
        }
        s+="\r\n";
        for (int i = 0; i < size; i++) {
            s += Float.toString(gZ[i]) + ";";
        }
        s+="\r\n";
        return s;
    }
    public String newId(){
        Random r = new Random();
        return Integer.toString(r.nextInt((9999999 - 1000000) + 1) + 1000000);
    }
}
