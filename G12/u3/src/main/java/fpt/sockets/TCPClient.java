package fpt.sockets;

import fpt.controller.ControllerClient;
import fpt.interfaces.MusikPlayer;
import fpt.model.Model;
import fpt.model.SongList;
import fpt.view.ViewClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Created by corin on 08.07.2017.
 */
public class TCPClient {
    private static String dienstname;
    private String password;
    private Model model;
    private ViewClient viewClient;
    private static final int PORT = 1099;
    public static String dienstnameServer;

    public TCPClient(String dienstname ,String password,Model model,ViewClient viewClient) throws Exception{
        this.dienstname = dienstname;
        this.password = password;
        this.model = model;
        this.viewClient = viewClient;
        ObjectInputStream in = null;
        ObjectOutputStream out =null;


        try{Socket serverCon = new Socket("localhost",5021);

            Remote remoteClient = new ControllerClient(model,viewClient);
            Registry registry = LocateRegistry.getRegistry(PORT);
            registry.rebind(dienstname,remoteClient);
            System.out.println( "Remote " + dienstname +" bound to registry port " + PORT);

            in = new ObjectInputStream(serverCon.getInputStream());
            out = new ObjectOutputStream(serverCon.getOutputStream());
            out.writeObject(dienstname);
            out.writeObject(password);
            dienstnameServer = (String) in.readObject();
            System.out.println(dienstnameServer);

            MusikPlayer remoteServer = (MusikPlayer) Naming.lookup("//localhost/" + dienstnameServer);
            System.out.println("connection to Remote " + dienstnameServer +" accepted");



        }catch (IOException ie){
                ie.printStackTrace();
        }
        finally {
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }
    public static String returnDienstameS(){
        System.out.println(dienstnameServer);
        return dienstnameServer;

    }
    public static String getDienstname(){
        return dienstname;
    }

}
