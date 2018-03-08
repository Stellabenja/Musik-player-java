package fpt.sockets;


import javafx.application.Platform;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by corin on 05.07.2017.
 */
public class UDPClient  {
    public boolean run;
    public static String zeit = new String();
    DatagramSocket dSocket;

    public UDPClient() {

        InetAddress ia = null;
        try{
        ia = InetAddress.getByName("localhost");
        } catch(UnknownHostException e2) {
        e2.printStackTrace();
        }

    // Socket für den Klienten anlegen
        try{
            dSocket = new DatagramSocket(5000);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        String command = "{" + "\"cmd\"" + ":" + "\"time\"" + "}";

                        byte buffer[] = null;
                        buffer = command.getBytes();

                        // Paket mit der Anfrage vorbereiten
                        DatagramPacket packet = new DatagramPacket(buffer,
                                buffer.length, InetAddress.getByName("localhost"), 3141);
                        // Paket versenden
                        dSocket.send(packet);

                        byte answer[] = new byte[1024];
                        // Paket für die Antwort vorbereiten
                        packet = new DatagramPacket(answer, answer.length);
                        // Auf die Antwort warten
                        dSocket.receive(packet);

                        // System.out.println(new String(packet.getData(), 0, packet
                        //.getLength()));
                        String absplZeit = new String(packet.getData(), 0, packet
                                .getLength());
                        zeit.equals(absplZeit);
                /*Platform.runLater(new Runnable() {

                    public void run() {


                    }
                });*/

                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }
            },1000,1000);

    } catch(
    SocketException e1)

    {
        e1.printStackTrace();
    }finally {
            dSocket.close();
        }

}
    public static String getZeit(){
        return zeit;
    }
}
