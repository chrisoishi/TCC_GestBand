/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APP;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 *
 * @author rodri
 */
public class Simulation {
    
    public int button;
    
    public static void pressKey(int key) throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(32);
    robot.delay(100);
    robot.keyRelease(32); 
}
    public static void pressSpace() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_SPACE);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_SPACE); 
}    
    public static void pressEnter() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_ENTER); 
}
    public static void pressArrowLeft() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_KP_LEFT);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_KP_LEFT); 
}    
    public static void pressArrowDown() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_KP_DOWN);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_KP_DOWN); 
}
    public static void pressArrowUp() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_KP_UP);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_KP_UP);   
}
    public static void pressArrowRight() throws AWTException {
    Robot robot = new Robot(); 
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_KP_RIGHT);
    robot.delay(100);
    robot.keyRelease(KeyEvent.VK_KP_RIGHT);   
}
    
}
