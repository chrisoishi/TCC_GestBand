/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.DTW;

/**
 *
 * @author Chris
 */
public class DTWController {

    public static DTW lDTW = new DTW();
    public static float[][] data;
    public static int MAX_SIZE = 200;

    
    public static void init(){
        data = new float[6][MAX_SIZE];
    }
        
    
    
    public static void test(float[][] data) {
        int m;
        for (int i = 0; i < GestureController.gestos.size(); i++) {
            m = 0;
            m += lDTW.compute(GestureController.gestos.get(i).acX, data[0]).getDistance();
            m += lDTW.compute(GestureController.gestos.get(i).acY, data[1]).getDistance();
            m += lDTW.compute(GestureController.gestos.get(i).acZ, data[2]).getDistance();
            m += lDTW.compute(GestureController.gestos.get(i).gX, data[3]).getDistance();
            m += lDTW.compute(GestureController.gestos.get(i).gY, data[4]).getDistance();
            m += lDTW.compute(GestureController.gestos.get(i).gZ, data[5]).getDistance();
            m = m / 6;
            if (m < 20) {
                System.out.println("Gesto:" + GestureController.gestos.get(i).name);
            }
        }
    }
}
