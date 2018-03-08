package fpt.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by corin on 12.07.2017.
 */
public class TCPClientThread extends Thread{
    private int name;
    private Socket socket;
    private String serverPassword;
    private byte[] dienstname;
    private ArrayList<String> nameList = new ArrayList<>();


    public TCPClientThread(int name,Socket socket,String serverPassword,byte[] dienstname) {
        this.name = name;
        this.socket = socket;
        this.serverPassword = serverPassword;
        this.dienstname = dienstname;
    }

    public void run() {
        String msg = "Server: Verbindung " + name;
        System.out.println(msg + " hergestellt");
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            try {
                sleep((long) (Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String clientName = in.toString() ;
            String password = in.toString();
            if(clientName!=null && password.equals(serverPassword)){
                synchronized (clientName) {
                    nameList.add(clientName);
                    out.write(dienstname);
                }
            }else{
                String error = "wrong password or name";
                out.write(error.getBytes());
            }
                out.flush();

            System.out.println("Verbindung " + name + " wird beendet");

            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
