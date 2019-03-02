/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javafx.scene.input.KeyCode;

/**
 *
 * @author rodri
 */
public class Simulation {

    public int button;
    public static Robot robot;

    public static void init() throws AWTException {
        robot = new Robot();
    }

    public static void pressKey(String key) {
        int keycode = map(key);
        robot.keyPress(keycode);
        robot.delay(100);
        robot.keyRelease(keycode);
    }

    public static void pressSpace() {
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_SPACE);
    }

    public static void pressEnter() {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void pressArrowLeft() {
        robot.keyPress(KeyEvent.VK_KP_LEFT);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_KP_LEFT);
    }

    public static void pressArrowDown() {
        robot.keyPress(KeyEvent.VK_KP_DOWN);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_KP_DOWN);
    }

    public static void pressArrowUp() {
        robot.keyPress(KeyEvent.VK_KP_UP);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_KP_UP);
    }

    public static void pressArrowRight() {
        robot.keyPress(KeyEvent.VK_KP_RIGHT);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_KP_RIGHT);
    }

    public static void mouse(int vel_x, int vel_y) {
        robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x - vel_x, MouseInfo.getPointerInfo().getLocation().y + vel_y);
    }

    public static int map(String key) {
        switch (key) {
            case "SPACE":
                return KeyEvent.VK_SPACE;
            case "BACK_SPACE":
                return KeyEvent.VK_BACK_SPACE;
            case "UP":
                return KeyEvent.VK_UP;
            case "DOWN":
                return KeyEvent.VK_DOWN;
            case "LEFT":
                return KeyEvent.VK_LEFT;
            case "RIGHT":
                return KeyEvent.VK_RIGHT;
            case "ESCAPE":
                return KeyEvent.VK_ESCAPE;
            case "SHIFT":
                return KeyEvent.VK_SHIFT;
            case "CONTROL":
                return KeyEvent.VK_CONTROL;
            case "ALT":
                return KeyEvent.VK_ALT;
            case "TAB":
                return KeyEvent.VK_TAB;

            case "F1":
                return KeyEvent.VK_F1;
            case "F2":
                return KeyEvent.VK_F2;
            case "F3":
                return KeyEvent.VK_F3;
            case "F4":
                return KeyEvent.VK_F4;
            case "F5":
                return KeyEvent.VK_F5;
            case "F6":
                return KeyEvent.VK_F6;
            case "F7":
                return KeyEvent.VK_F7;
            case "F8":
                return KeyEvent.VK_F8;
            case "F9":
                return KeyEvent.VK_F9;
            case "F10":
                return KeyEvent.VK_F10;
            case "F11":
                return KeyEvent.VK_F11;
            case "F12":
                return KeyEvent.VK_F12;

            case "A":
                return KeyEvent.VK_A;
            case "B":
                return KeyEvent.VK_B;
            case "C":
                return KeyEvent.VK_C;
            case "D":
                return KeyEvent.VK_D;
            case "E":
                return KeyEvent.VK_E;
            case "F":
                return KeyEvent.VK_F;
            case "G":
                return KeyEvent.VK_G;
            case "H":
                return KeyEvent.VK_H;
            case "I":
                return KeyEvent.VK_I;
            case "J":
                return KeyEvent.VK_J;
            case "K":
                return KeyEvent.VK_K;
            case "L":
                return KeyEvent.VK_L;
            case "M":
                return KeyEvent.VK_M;
            case "N":
                return KeyEvent.VK_N;
            case "O":
                return KeyEvent.VK_O;
            case "P":
                return KeyEvent.VK_P;
            case "Q":
                return KeyEvent.VK_Q;
            case "R":
                return KeyEvent.VK_R;
            case "S":
                return KeyEvent.VK_S;
            case "T":
                return KeyEvent.VK_T;
            case "U":
                return KeyEvent.VK_U;
            case "V":
                return KeyEvent.VK_V;
            case "X":
                return KeyEvent.VK_X;
            case "W":
                return KeyEvent.VK_W;
            case "Y":
                return KeyEvent.VK_Y;
            case "Z":
                return KeyEvent.VK_Z;
            case "DIGIT0":
                return KeyEvent.VK_0;
            case "DIGIT1":
                return KeyEvent.VK_1;
            case "DIGIT2":
                return KeyEvent.VK_2;
            case "DIGIT3":
                return KeyEvent.VK_3;
            case "DIGIT4":
                return KeyEvent.VK_4;
            case "DIGIT5":
                return KeyEvent.VK_5;
            case "DIGIT6":
                return KeyEvent.VK_6;
            case "DIGIT7":
                return KeyEvent.VK_7;
            case "DIGIT8":
                return KeyEvent.VK_8;
            case "DIGIT9":
                return KeyEvent.VK_9;
        }
        return 0;
    }

}
