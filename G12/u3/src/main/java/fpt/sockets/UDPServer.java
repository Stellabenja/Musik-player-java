package fpt.sockets;

import fpt.controller.ControllerClient;
import fpt.controller.ControllerServer;
import fpt.interfaces.Song;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by corin on 05.07.2017.
 */
public class UDPServer extends Thread{
    private boolean run = true;
    private DatagramSocket socket;

    public void run(){
                    // Socket erstellen unter dem der Server erreichbar ist
                    try  {
                        socket = new DatagramSocket(3141);
                        socket.setSoTimeout(1200000);
                        while (isRunning()) {
                            // Neues Paket anlegen
                            DatagramPacket packet = new DatagramPacket(new byte[5], 5);
                            // Auf Paket warten
                            try {
                                socket.receive(packet);
                                System.out.println("packet received");
                            } catch (Exception e) {
                                continue;
                            }
                            // Daten auslesen
                            InetAddress address = packet.getAddress();
                            int port = packet.getPort();
                            int len = packet.getLength();
                            byte[] datab = packet.getData();

                            System.out.printf("Anfrage von %s vom Port %d mit der L�nge %d:%n%s%n",
                                    address, port, len, new String(datab, 0, len));
                            String data = new String(packet.getData());

                            if (data.equals("{" + "\"cmd\"" + ":" + "\"time\"" + "}")) {
                                byte[] b = ControllerServer.modifyTemps(null).getBytes();
                                // Paket mit neuen Daten (Datum) als Antwort vorbereiten
                                packet = new DatagramPacket(b, b.length,
                                        address, port);
                                // Paket versenden
                                socket.send(packet);
                                System.out.println("packet sent");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        socket.close();
                    }


            }



    public synchronized void shutdown() {
        run = false;
    }

    public synchronized boolean isRunning() {
        return run;
    }


}

