package fpt.main;

import fpt.controller.ControllerClient;
import fpt.interfaces.MusikPlayer;
import fpt.model.IDgenerator;
import fpt.model.Model;
import fpt.sockets.TCPClient;
import fpt.sockets.UDPClient;
import fpt.view.ViewClient;
import fpt.view.ViewServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by STELLA on 10/05/2017.
 */
public class MainClient extends Application {
    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        IDgenerator.init(model);
        ViewClient viewClient = new ViewClient();

        /*try {
            UDPClient udpClient = new UDPClient();
        }catch (Exception ex){
            ex.printStackTrace();
        }*/

        try {
            TCPClient tcpClient = new TCPClient("remoteClient", "music",model,viewClient);
            //viewClient.link(tcpClient);
        }catch (Exception e){
            e.printStackTrace();
        }



        Scene scene = new Scene(viewClient, 1000, 630);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Playlist");
        primaryStage.setResizable(true);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
