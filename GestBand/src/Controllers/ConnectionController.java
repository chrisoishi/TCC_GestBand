/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import APP.Main;
import static APP.Main.Graph;
import static APP.Main.controller;
import APP.Settings;
import TCP.ClientInSocket;
import BLUETOOTH.ClientBluetooth;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author Chris
 */
public class ConnectionController {

    private static String s = "";
    public static boolean CONNECTION = false;
    public static boolean CONNECTING = false;
    public static boolean IS_BLUETOOTH = false;
    public static int BLUETOOTH_STATUS = 0;
    public static Protocol protocol = new Protocol();

    private static long MEDIA_LATENCY;
    private static int MEDIA_LATENCY_I;
    private static int AMOSTRAS = 0;

    public static Thread thread;

    public static boolean connect_to_gestband(String ip) throws IOException {
        BLUETOOTH_STATUS = 0;
        IS_BLUETOOTH = false;
        ClientInSocket.Actions actionsReceive = new ClientInSocket.Actions() {
            public void run(char c) {
                if (c == '|') {
                    handler_receive(s);
                    s = "";
                } else {
                    s = s + Character.toString(c);
                }
            }
        };
        CONNECTION = ClientInSocket.Start(actionsReceive, ip);
        return CONNECTION;
    }

    public static void connect_to_gestband_bluetooth(MainController.Actions t) {
        CONNECTING = true;
        IS_BLUETOOTH = true;

        String btAddress = Settings.get("last_bluetooth_address");
        if (btAddress != "undefined") {
            BLUETOOTH_STATUS = 5;
            t.run();
            if (CONNECTION = ClientBluetooth.connect(btAddress)) {
                ClientBluetooth.Actions act2 = new ClientBluetooth.Actions() {
                    public void run(char c) {
                        if (c == '|') {
                            handler_receive(s);
                            s = "";
                        } else {
                            s = s + Character.toString(c);
                        }
                    }
                };
                ClientBluetooth.setListening(act2);
                IS_BLUETOOTH = true;
                BLUETOOTH_STATUS = 3;
                CONNECTING = false;
                t.run();
                return;
            }
        }
        BLUETOOTH_STATUS = 1;
        t.run();
        ClientBluetooth.Actions act = new ClientBluetooth.Actions() {
            public void run(char c) {
                BLUETOOTH_STATUS = 2;
                t.run();
                for (RemoteDevice r : ClientBluetooth.devices) {
                    try {
                        System.out.println(r.getFriendlyName(false));
                        if (r.getFriendlyName(false).contains("LASER")) {
                            if (CONNECTION = ClientBluetooth.connect(r.getBluetoothAddress())) {
                                System.out.println("conectado");
                                ClientBluetooth.Actions act2 = new ClientBluetooth.Actions() {
                                    public void run(char c) {
                                        if (c == '|') {
                                            handler_receive(s);
                                            s = "";
                                        } else {
                                            s = s + Character.toString(c);
                                        }
                                    }
                                };
                                ClientBluetooth.setListening(act2);
                                Settings.set("last_bluetooth_address", r.getBluetoothAddress());
                                IS_BLUETOOTH = true;
                                BLUETOOTH_STATUS = 3;
                                t.run();
                            } else {
                                BLUETOOTH_STATUS = 4;
                                t.run();
                            }
                            CONNECTING = false;
                            break;
                        }
                    } catch (IOException ex) {
                        CONNECTING = false;
                        Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (BLUETOOTH_STATUS == 2) {
                    BLUETOOTH_STATUS = 4;
                    t.run();
                }
            }
        };
        ClientBluetooth.getDevices(act);

        //CONNECTION = ClientInSocket.Start(actionsReceive, ip);
    }

    public static void disconnect() {
        if (IS_BLUETOOTH) {
            CONNECTION = false;
            IS_BLUETOOTH = false;
            ClientBluetooth.disconnect();
        } else {
            try {
                ClientInSocket.stop();
                CONNECTION = false;
            } catch (IOException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void restart() {
        send("restart;|");
        DTWController.ACTIVE = false;
        CONNECTION = false;
        disconnect();
    }

    public static void send(String text) {
        if (CONNECTION) {
            if (IS_BLUETOOTH) {
                ClientBluetooth.send(text);
            } else {
                ClientInSocket.send(text);
            }
        }
    }

    private static void handler_receive(String s) {
        if (protocol.ACK == 1) {
            protocol.ACK = 0;
            //System.out.println("###### TIMEEEE ############:"+protocol.time());
        } else if (protocol.ACK == 2) {
            protocol.ACK = 3;
        }
        String data[] = s.split(";");
        //System.out.println(s);
        switch (data[0]) {
            case "wifi":
                controller.set_wifi(data);
                break;
            case "data":
                long lat = protocol.latency();
                if (MEDIA_LATENCY_I > 10) {
                    AMOSTRAS++;
                    System.out.println(new Date()+" "+MEDIA_LATENCY/10);
                    //System.out.println("########### LATENCIA MEDIA DATA:" + MEDIA_LATENCY / 10 + " ms");
                    //if(AMOSTRAS==100)System.out.println("DEU 100 AMOSTRAS");
                    MEDIA_LATENCY = 0;
                    MEDIA_LATENCY_I = 0;
                } else {
                    if (lat > 10) {
                        MEDIA_LATENCY += lat;
                        MEDIA_LATENCY_I++;
                    }

                }

                DTWController.receive(data[1]);

                break;
            case "profile":
                ProfileController.nextSet();
                ConnectionController.send("profile;" + ProfileController.getSet().name + ";|");
                Task t = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        return true;
                    }

                    @Override
                    protected void succeeded() {
                        controller.set_status();
                        controller.show_profiles();
                    }
                };
                new Thread(t).start();

                break;

        }
    }

    public static class Protocol {

        private int ACK = 0;
        private long START;
        private long TIME_CONNECTED;
        private long LATENCY;
        private long INIT_SEND;
        private long last;
        
        private Thread test;

        public void startDataSend() {
            ConnectionController.AMOSTRAS=0;
            ACK = 1;
            START = new Date().getTime();
            TIME_CONNECTED = START;
            LATENCY = START;
            ConnectionController.send("send;|");
            await();
            test_connection();
        }

        public void stopDataSend() {
            ACK = 2;
            START = new Date().getTime();
            ConnectionController.send("stop;|");
            await();
        }

        public long time() {
            long diffInMillies = new Date().getTime() - START;
            last = TimeUnit.NANOSECONDS.convert(diffInMillies, TimeUnit.NANOSECONDS);
            last = diffInMillies;
            return last;
        }

        public long latency() {
            long diffInMillies = new Date().getTime() - LATENCY;
            LATENCY = new Date().getTime();
            return diffInMillies;
        }

        public boolean is_connected() {
            long diffInMillies = new Date().getTime() - LATENCY;
            if (diffInMillies < 5000) {
                return true;
            } else {
                TIME_CONNECTED = (new Date().getTime() - TIME_CONNECTED)/60000;
                System.out.println("##### PULSEIRA RECONHECEU POR :"+TIME_CONNECTED+" minutos");
                return false;
            }
        }

        public void await() {
            new Thread() {
                public void run() {
                    try {
                        sleep(500);
                        if (ACK != 0) {
                            if (ACK == 1) {
                                startDataSend();
                            } else if (ACK == 3) {
                                stopDataSend();
                            } else {
                                ACK = 0;
                            }

                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }

        public void test_connection() {
            if(test != null)test.stop();
            test = new Thread() {
                public void run() {
                    try {
                        while (is_connected()) {
                            //System.out.println("testado,,");
                            sleep(1000);
                        }

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            test.start();
        }

    }
}
