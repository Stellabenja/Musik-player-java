package fpt.sockets;

import fpt.controller.ControllerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by corin on 12.07.2017.
 */
class UDPClientThread extends Thread {

    private DatagramPacket packet;
    private DatagramSocket socket;

    public UDPClientThread(DatagramPacket packet, DatagramSocket socket)
            throws SocketException {
        this.packet = packet;
        this.socket = socket;
    }

    public void run() {
        // Daten auslesen
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        int len = packet.getLength();
        byte[] data = packet.getData();

        System.out.printf("Anfrage von %s vom Port %d mit der L�nge %d:%n%s%n",
                address, port, len, new String(data, 0, len));

        // Nutzdaten in ein Stringobjekt �bergeben
        String time = new String(packet.getData());

        if (time.equals("\"{\" +\"\\\"cmd\\\"\" + \":\"+ \"\\\"time\\\"\" + \"}\"")) {
            byte[] myTime  = ControllerServer.modifyTemps(null).getBytes();

            // Paket mit neuen Daten (Datum) als Antwort vorbereiten
            packet = new DatagramPacket(myTime, myTime.length, address, port);

            try {
                // Paket versenden
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            byte[] myTime = new byte[1024];
            myTime = new String("Command unknown").getBytes();
            try {
                sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            // Paket mit Information, dass das Schl�sselwort ung�ltig ist als
            // Antwort vorbereiten
            packet = new DatagramPacket(myTime, myTime.length, address, port);
            try {
                // Paket versenden
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
