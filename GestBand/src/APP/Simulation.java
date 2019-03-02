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

    public static void pressKey(int key) {
        robot.keyPress(32);
        robot.delay(100);
        robot.keyRelease(32);
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
    public static void mouse(int vel_x,int vel_y){
        robot.mouseMove(MouseInfo.getPointerInfo().getLocation().x-vel_x, MouseInfo.getPointerInfo().getLocation().y+vel_y);
    }

}
