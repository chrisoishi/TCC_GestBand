/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLUETOOTH;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author chris
 */
public class ClientBluetooth {

    public static boolean isDiscovering = false;
    public static ArrayList<RemoteDevice> devices = new ArrayList<RemoteDevice>();
    private static StreamConnection streamConnection;
    private static OutputStream os;
    private static InputStream is;
    private static Thread ThreadListen = null;
    

    public interface Actions {

        public void run(char c);

    }

    public static void getDevices(Actions acts) {
        try {
            isDiscovering = true;
            devices.clear();
            LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
                @Override
                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    devices.add(btDevice);
                }

                @Override
                public void inquiryCompleted(int discType) {
                    System.out.println("fim da procura...");
                    isDiscovering = false;
                    acts.run('c');
                }

                @Override
                public void serviceSearchCompleted(int transID, int respCode) {
                }

                @Override
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                }
            });
        } catch (Exception e) {
            isDiscovering = false;
        }
    }

    public static boolean connect(String address) {
        try {
            System.out.println("conexao bem sucessida");
            streamConnection = (StreamConnection) Connector.open("btspp://" + address + ":1;authenticate=false;encrypt=false;master=false");
            os = streamConnection.openOutputStream();
            is = streamConnection.openInputStream();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static void disconnect() {
        try {
            System.out.println("sdgdsg");
            os.close();
            is.close();
            streamConnection.close();
            ThreadListen.stop();

        } catch (Exception e) {

        }
    }

    public static void send(String text) {
        try {
            os.write(text.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ClientBluetooth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setListening(Actions acts) {
        ThreadListen = new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (is.available() > 0) {
                            acts.run((char) is.read());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ClientBluetooth.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        ThreadListen.start();
    }
}
