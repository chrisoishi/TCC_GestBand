/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.DTW;
import APP.Gestures;
import static APP.Main.Graph;
import APP.Simulation;

/**
 *
 * @author Chris
 */
public class DTWController {

    public static final int MAX_SIZE = 200;
    public static final int PERIOD = 1;

    public static DTW lDTW = new DTW();
    public static float[][] data;

    private static String buff[];
    private static int i_current, i_period;

    public static boolean ACTIVE = false;
    public static boolean GB_MOVE = false;
    public static boolean GB_INITIAL = false;
    public static int buff_moving = 0;
    public static int buff_position = 0;

    public static void init() {
        data = new float[6][MAX_SIZE];
        i_current = 0;
    }

    public static void receive(String s) {

        try {
            buff = s.split(":");
            if (buff[0].equals("A")) {
                add_data(buff[1], 0);
                add_data(buff[2], 1);
                add_data(buff[3], 2);
                Graph.print(0, buff);
                position(buff);
            } else {
                add_data(buff[1], 3);
                add_data(buff[2], 4);
                add_data(buff[3], 5);
                Graph.print(1, buff);
                moving(buff);
                execute();
            }
        } catch (Exception e) {
            System.out.println("erros nos dados....");
        }

    }

    public static void position(String[] d) {
        float[] f = new float[3];
        f[0] = Float.parseFloat(d[1]);
        f[1] = Float.parseFloat(d[2]);
        f[2] = Float.parseFloat(d[3]);
        if (f[2] > 27) {
            if (!GB_INITIAL) {
                buff_position += 1;
                if (buff_position == 70) {
                    GB_INITIAL = true;
                    // System.out.println("inicio");
                }
            }
        } else if (GB_INITIAL) {
            //System.out.println("inicio cancelado");
            GB_INITIAL = false;
            buff_position = 0;
        }

    }

    public static void moving(String[] d) {
        float[] f = new float[3];
        float m;
        f[0] = Float.parseFloat(d[1]);
        f[1] = Float.parseFloat(d[2]);
        f[2] = Float.parseFloat(d[3]);
        if (f[0] < 0) {
            f[0] = f[0] * -1;
        }
        if (f[1] < 0) {
            f[1] = f[1] * -1;
        }
        if (f[2] < 0) {
            f[2] = f[2] * -1;
        }
        m = (f[0] + f[1] + f[2]);
        if (m > 9 & !GB_MOVE) {
            buff_moving += 2;
            if (buff_moving == 20 & !GB_MOVE) {
                GB_MOVE = true;
                //System.out.println(GB_MOVE);
            }
        } else if (m < 9 & GB_MOVE) {
            buff_moving--;
            if (buff_moving == 0 & GB_MOVE) {
                GB_MOVE = false;
                //validate();
                //System.out.println(GB_MOVE);
            }
        }
    }

    public static void add_data(String value, int eixo) {
        data[eixo][i_current] = Float.parseFloat(value);
        if (eixo == 5) {
            i_current++;
            if (i_current == MAX_SIZE) {
                i_current = 0;
            }
        }
    }

    public static float[][] get_data(int size) {
        int remaind = size;
        int i = i_current;
        float[][] test_data = new float[6][size];
        while (remaind > 0) {
            i--;
            if (i < 0) {
                i = MAX_SIZE - 1;
            }
            remaind--;
            test_data[0][remaind] = data[0][i];
            test_data[1][remaind] = data[1][i];
            test_data[2][remaind] = data[2][i];
            test_data[3][remaind] = data[3][i];
            test_data[4][remaind] = data[4][i];
            test_data[5][remaind] = data[5][i];

        }
        return test_data;
    }

    public static void execute() {
        if (ACTIVE) {
            i_period++;
            //print(50);
            if (i_period == PERIOD) {
                validate();
                i_period = 0;
            }
        }
    }

    public static void validate() {
        int m, min;
        String certo;
        float[][] d;
        Gestures g;
        min = 70;
        certo = "";
        for (String id : ProfileController.current.data.keySet()) {
            m = 0;
            g = GestureController.getById(id);
            d = get_data(g.size);
            m += lDTW.compute(g.acX, d[0]).getDistance();
            m += lDTW.compute(g.acY, d[1]).getDistance();
            m += lDTW.compute(g.acZ, d[2]).getDistance();
            m += lDTW.compute(g.gX, d[3]).getDistance();
            m += lDTW.compute(g.gY, d[4]).getDistance();
            m += lDTW.compute(g.gZ, d[5]).getDistance();
            m = m / 6;
            if (m < min) {
                certo = id;
                min = m;

            } else {
                //sSystem.out.println("TESTE:" + m + "   " + g.name);
            }
        }
        if (min < 70) {
            g = GestureController.getById(certo);
            System.out.println("Gesto:" + g.name);
            clear();
            if (!g.default_action.equals("")) {
                Simulation.pressKey(g.default_action);
            }
        }
    }

    public static void clear() {
        i_current = 0;
        for (int i = 0; i < MAX_SIZE; i++) {
            data[0][i] = 0;
            data[1][i] = 0;
            data[2][i] = 0;
            data[3][i] = 0;
            data[4][i] = 0;
            data[5][i] = 0;
        }
    }

    public static void print(int size) {
        for (int i = 0; i < size; i++) {
            System.out.print(data[0][i] + ";");
        }
        System.out.println("");
    }
}
