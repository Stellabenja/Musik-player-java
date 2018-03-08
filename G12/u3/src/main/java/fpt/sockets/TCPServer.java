package fpt.sockets;

import fpt.controller.ControllerServer;
import fpt.interfaces.RemoteClient;
import fpt.model.Model;
import fpt.model.Song;
import fpt.model.SongList;
import fpt.view.ViewClient;
import fpt.view.ViewServer;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.rmi.server.*;

/**
 * Created by corin on 08.07.2017.
 */
public class TCPServer extends Thread {
    private Model model;
    private String serverPassword;
    private String dienstname = "musicplayer";
    private static ArrayList<String> nameList = new ArrayList<>();
    private static final String PATH = "C:\\Users\\corin\\Desktop\\Sommersmester 2017\\FPT\\Aufgabe\\Lieder";
    private ViewServer viewServer;
    private static final int PORT = 1099;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oout = null;


    public TCPServer(String serverPassword,Model model,ViewServer viewServer) throws RemoteException {
        this.viewServer = viewServer;
        this.model = model;
        this.serverPassword = serverPassword;
        model.addSongsFromDir(PATH);
        viewServer.fillSongList(model.getAllSongs());
        System.out.println(nameList);



    }

    public void run(){
        try(ServerSocket server = new ServerSocket(5021) ){

            Remote remote = new ControllerServer(model,viewServer);//Model is here 0, have to link
            try {
                Registry registry = LocateRegistry.getRegistry(PORT);

                registry.rebind(dienstname, remote);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println( "Remote " + dienstname +" bound to registry port " + PORT);
            SongList songList = model.getAllSongs();
                while(true){
                    try{Socket clientSocket = server.accept();
                        InputStream in = clientSocket.getInputStream();
                        OutputStream out = clientSocket.getOutputStream();
                        oout = new ObjectOutputStream(out);
                        ois = new ObjectInputStream(in);
                        String clientName = (String) ois.readObject();
                        String password = (String) ois.readObject();

                        if(clientName!=null && password.equals(serverPassword)){
                            synchronized (nameList) {

                                nameList.add(clientName);

                            }
                            oout.writeObject(dienstname);
                                try {
                                    System.out.println(clientSocket.getInetAddress().getHostAddress());
                                    RemoteClient remoteClient = (RemoteClient) Naming.lookup("//localhost/" + clientName);
                                    System.out.println("connection to Remote " + clientName + " accepted");
                                    System.out.println(songList);
                                    synchronized (remoteClient){
                                        for(fpt.interfaces.Song s: songList) {
                                        remoteClient.fillSongs(s.getId(), s.getTitle(), s.getInterpret(), s.getAlbum());
                                        }
                                        if(model.getPlaylist()!=null) {
                                            for (fpt.interfaces.Song song : model.getPlaylist()) {
                                                remoteClient.addToPlay(song.getId());
                                            }
                                        }
                                    }

                                } catch (NotBoundException e) {
                                    e.printStackTrace();
                                }


                        }else{
                            String error = "wrong password or name";
                            oout.writeObject(error.getBytes());
                        }
                        oout.flush();
                    }catch (IOException ie){
                            ie.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(oout!=null) {
                            oout.close();
                            System.out.print("closed");
                        }
                        if(ois!=null) {
                            ois.close();
                            System.out.print("closed");
                        }
                    }
                }
        }catch (IOException ie2){
            ie2.printStackTrace();
        }
    }
    public static ArrayList<String> getNameList(){
        System.out.println(nameList);
        return nameList;

    }


}
