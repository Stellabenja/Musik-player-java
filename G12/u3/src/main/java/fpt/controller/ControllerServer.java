package fpt.controller;

import fpt.Strategy.BinaryStrategy;
import fpt.Strategy.DatabaseUtils;
import fpt.Strategy.OpenJPA;
import fpt.Strategy.XMLStrategy;
import fpt.interfaces.MusikPlayer;
import fpt.interfaces.RemoteClient;
import fpt.interfaces.SerializableStrategy;
import fpt.interfaces.Song;
import fpt.model.Model;
import fpt.model.SongList;
import fpt.sockets.TCPServer;
import fpt.sockets.UDPClient;
import fpt.sockets.UDPServer;
import fpt.view.ViewClient;
import fpt.view.ViewServer;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import static java.lang.Thread.sleep;
import static javafx.scene.media.MediaPlayer.Status.*;


/**
 * Created by corin on 09.05.2017.
 */
public class ControllerServer extends UnicastRemoteObject implements MusikPlayer {

    private static final long serialVersionUID = 1L;

    private static final String PATH = "C:\\Users\\corin\\Desktop\\Sommersmester 2017\\FPT\\Aufgabe\\Lieder";
    public static final String[] strategies = {"Binary Strategy", "OpenJPA", "XML Strategy", "JDBCConnector"};

    private SerializableStrategy strategy;
    private ViewServer viewServer;
    private Model model;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Media currentSong;
    private int seekForwarTime = 5000;// milliseconde
    private Duration duration;
    private Slider timeSlider;
    private static String temps = "";
    Timer timer = new Timer();


    public ControllerServer(Model model,ViewServer viewServer) throws RemoteException {
        this.model = model;
        this.viewServer = viewServer;

    }
    public String returnZeit() throws RemoteException {
        // UI updaten
        Platform.runLater(new Runnable() {
                @Override
                public void run() {

                            // entsprechende UI Komponente updaten
                            Duration currentTime = mediaPlayer.getCurrentTime();
                            duration = mediaPlayer.getMedia().getDuration();
                            modifyTemps(formatTime(currentTime,duration));

                }
            });

            // Thread schlafen
            try {
                // fuer 1 Sekunde
                sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ex) {

            }
        return temps;

    }

    public void link(Model model, ViewServer viewServer) {
        this.model = model;
        this.viewServer = viewServer;


        viewServer.getAddToPlayButton().setOnAction(event -> {

        });

        viewServer.getAddall().setOnMouseClicked(event -> {

            try {
                model.getPlaylist().deleteAllSongs();
                SongList sl = model.getAllSongs();
                for (Song s : sl) {
                    model.getPlaylist().addSong(s);
                }
                viewServer.fillPlayList(null);
                viewServer.fillPlayList(model.getPlaylist());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (model.getPlaylist().isEmpty()) {
                return;
            } else if (mediaPlayer != null) {
                mediaPlayer.getMedia();
            }
        });

        viewServer.getRemoveFromPLay().setOnMousePressed(event -> {
            Song s = viewServer.getPlayList().getSelectionModel().getSelectedItem();
            if (s == null) {
                return;
            }
            try {
                Media media = new Media(s.getPath());
                if (mediaPlayer == null) {
                    model.getPlaylist().deleteSong(s);
                    viewServer.fillPlayList(null);
                    viewServer.fillPlayList(model.getPlaylist());
                    return;
                }

                if (mediaPlayer != null && mediaPlayer.getMedia().getSource().equals(media.getSource())) {

                    model.getPlaylist().deleteSong(s);
                    //stopButton();
                    viewServer.fillPlayList(null);
                    viewServer.fillPlayList(model.getPlaylist());
                } else {
                    model.getPlaylist().deleteSong(s);
                    viewServer.fillPlayList(null);
                    viewServer.fillPlayList(model.getPlaylist());

                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });

        viewServer.getPlay().setOnAction(e ->
        {




        });


        viewServer.getStop().setOnAction(e ->
        {

        });
        viewServer.getPause().setOnAction(e ->
        {

        });
        viewServer.getNext().setOnAction(event -> {

        });
        viewServer.getLoad().setOnAction(event -> {
            try {
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        viewServer.getSave().setOnAction(event -> {
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public String[] returnStrategies(){
        return strategies;
    }
    @Override
    public fpt.model.SongList songList() throws RemoteException {
        System.out.print(model.getAllSongs());
        model.addSongsFromDir(PATH);
        return model.getAllSongs();
    }

    public void addToPlay(long id) throws RemoteException{
        ArrayList<String> clientNames = TCPServer.getNameList();
        System.out.println(clientNames);

         Song s = model.getAllSongs().findSongByID(id);
        if (s == null) {
            return;
        }
        try {
            model.getPlaylist().addSong(s);
            viewServer.fillPlayList(model.getPlaylist());
            System.out.println(s);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for(String name : clientNames){
            synchronized (clientNames) {
                Registry registry = LocateRegistry.getRegistry(1099);
                RemoteClient client = null;
                System.out.println("hhh");
                try {
                    try {
                            client = (RemoteClient) Naming.lookup("//localhost/" + name);
                    } catch (MalformedURLException e) {
                            e.printStackTrace();
                    }
                        synchronized (client) {
                            client.addToPlay(s.getId());
                        }

                    } catch (NotBoundException e) {
                        e.printStackTrace();
                }


            }
        }


    }
    public void commit(long id,String titel,String interpret,String album) throws RemoteException{
        ArrayList<String> clientNames = TCPServer.getNameList();
        Song selectedSong = model.getAllSongs().findSongByID(id);
        if(selectedSong==null){
            return;
        }
        selectedSong.setAlbum(album);
        selectedSong.setInterpret(interpret);
        selectedSong.setTitle(titel);

        for(String name : clientNames){
            synchronized (clientNames) {
                Registry registry = LocateRegistry.getRegistry(1099);
                RemoteClient client = null;
                System.out.println("hhh");
                try {
                    try {
                        client = (RemoteClient) Naming.lookup("//localhost/" + name);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    synchronized (client) {
                        client.commitSong(selectedSong.getId(),selectedSong.getTitle(),selectedSong.getInterpret(),selectedSong.getAlbum());
                    }

                } catch (NotBoundException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    public void playNext(long id) throws RemoteException {
        if (model.getPlaylist().isEmpty()) {
            return;
        }
        int index = viewServer.getPlayList().getSelectionModel().getSelectedIndex();
        System.out.println(index);
        if (index == -1 || index == model.getPlaylist().size() - 1) {
            viewServer.getPlayList().getSelectionModel().clearAndSelect(0);
        } else {
            viewServer.getPlayList().getSelectionModel().clearAndSelect(index + 1);
        }
        play(id);


    }

    public void play(long id) throws RemoteException{


        ArrayList<String> clientNames = TCPServer.getNameList();

        if (model.getPlaylist().isEmpty()) {
            return;
        }
        Song s = model.getAllSongs().findSongByID(id);
        if (s == null) {
            s = model.getPlaylist().get(0);
            viewServer.getPlayList().getSelectionModel().clearAndSelect(0);
            s = viewServer.getPlayList().getSelectionModel().getSelectedItem();


        }
        Media media = new Media(s.getPath());
        if (mediaPlayer != null) {

            if (mediaPlayer.getMedia().getSource().equals(media.getSource())) {
                MediaPlayer.Status st = mediaPlayer.getStatus();
                if (st == PAUSED || st == READY || st == STOPPED || st == STALLED) {
                    System.out.println("bggg");
                    mediaPlayer.play();
                    System.out.println("bgg");

                    try {
                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                Duration currentTime = mediaPlayer.getCurrentTime();
                                duration = mediaPlayer.getMedia().getDuration();
                                temps = formatTime(currentTime,duration);

                                viewServer.fillTimeBox(temps);
                                System.out.println(temps);
                                if(mediaPlayer.getStatus() == PAUSED){
                                    try {
                                        timer.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },1000,1000);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    for(String name: clientNames){
                        synchronized (clientNames) {
                            Registry registry = LocateRegistry.getRegistry(1099);
                            RemoteClient client = null;
                            try {
                                client = (RemoteClient) Naming.lookup("//localhost/" + name);
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            synchronized (client) {
                                client.play();
                            }
                        }
                    }

                    System.out.println("mediaPlayer is " + mediaPlayer);

                }
                return;
            } else {
                mediaPlayer.stop();
            }
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        try{
            UDPServer udpServer = new UDPServer();
            udpServer.start();
            System.out.println("udpserver started");
        }catch (Exception ex){
            System.out.println("error");
            ex.printStackTrace();
        }

        for(String name: clientNames){
            synchronized (clientNames) {
                Registry registry = LocateRegistry.getRegistry(1099);
                RemoteClient client = null;
                try {
                    client = (RemoteClient) Naming.lookup("//localhost/" + name);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                synchronized (client) {
                    client.play();
                }
            }
        }

        try {

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    duration = mediaPlayer.getMedia().getDuration();
                    temps = formatTime(currentTime,duration);

                    viewServer.fillTimeBox(temps);
                    System.out.println(temps);

                    if(mediaPlayer.getStatus() == PAUSED||mediaPlayer.getStatus() ==STOPPED) {
                        try {
                            timer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },1000,1000);
        } catch (Exception e1) {
            e1.printStackTrace();
        }



        mediaPlayer.getMedia();
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                playNext(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });
    }

    public void pause() throws RemoteException{
        ArrayList<String> clientNames = TCPServer.getNameList();
        if (mediaPlayer != null) {
            mediaPlayer.pause();

        }
        for(String name: clientNames){
            synchronized (clientNames) {
                Registry registry = LocateRegistry.getRegistry(1099);
                RemoteClient client = null;
                try {
                    client = (RemoteClient) Naming.lookup("//localhost/" + name);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                synchronized (client) {
                    int intDuration = (int)Math.floor(mediaPlayer.getMedia().getDuration().toSeconds());
                    int intDurationMin = intDuration / 60;
                    int intDurationSec = intDuration % 60;

                    int currentTime = (int)Math.floor(mediaPlayer.getCurrentTime().toSeconds());
                    int intCurrentMin = currentTime / 60;
                    int intCurrentSec = currentTime % 60;
                    client.pause(intCurrentMin +":" + intCurrentSec +"/" + intDurationMin + ":" +intDurationSec);
                }
            }
        }
    }

    public static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.compareTo(Duration.ZERO)>0) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }


    public void stopButton() throws RemoteException {
        ArrayList<String> clientNames = TCPServer.getNameList();
        if (mediaPlayer != null) {
            if (!mediaPlayer.isMute())
                mediaPlayer.stop();
        }
        for(String name: clientNames){
            synchronized (clientNames) {
                Registry registry = LocateRegistry.getRegistry(1099);
                RemoteClient client = null;
                try {
                    client = (RemoteClient) Naming.lookup("//localhost/" + name);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                synchronized (client) {
                    int intDuration = (int)Math.floor(mediaPlayer.getMedia().getDuration().toSeconds());
                    int intDurationMin = intDuration / 60;
                    int intDurationSec = intDuration % 60;

                    int currentTime = (int)Math.floor(mediaPlayer.getCurrentTime().toSeconds());
                    int intCurrentMin = currentTime / 60;
                    int intCurrentSec = currentTime % 60;
                    client.stopButton(intCurrentMin +":" + intCurrentSec +"/" + intDurationMin + ":" +intDurationSec);
                }
            }
        }
    }

    public void setStrategy(int strategycase) throws RemoteException {
        switch (strategycase) {
            case 0:
                strategy = new BinaryStrategy();
                break;
            case 1:
                strategy = new OpenJPA();
                break;
            case 2:
                strategy = new XMLStrategy();
                break;
            case 3:
                strategy = new DatabaseUtils();
                break;
        }
        System.out.println("Strategy:" + strategycase);
        System.out.println(strategy + " 4");
    }

    public void load() throws IOException,RemoteException {


        model.getAllSongs().deleteAllSongs();
        viewServer.fillSongList(null);

        if (model.getPlaylist() != null) {
            model.getPlaylist().deleteAllSongs();
        }

        try {
            strategy.openReadableSongs();
            Song readSongL = null;
            readSongL = strategy.readSong();

            while (readSongL != null) {
                model.getAllSongs().addSong(readSongL);
                readSongL = strategy.readSong();
                viewServer.fillSongList(model.getAllSongs());
            }

            System.out.println("Library loaded!!!");
        } catch (IOException|ClassNotFoundException e) {
            System.out.println("Library loaded");
            e.printStackTrace();
            //no more song to be read
        } finally {
            strategy.closeReadable();
        }

        try {
            strategy.openReadablePlaylist();
            Song readSong = strategy.readSong();
            Song result = null;
            long id =0;
            while (readSong!=null) {
                id = readSong.getId();
                if(id == model.getAllSongs().findSongByID(id).getId()) {
                    result = model.getAllSongs().findSongByID(id);
                    model.getPlaylist().addSong(result);
                    readSong = strategy.readSong();
                    viewServer.fillPlayList(null);
                    viewServer.fillPlayList(model.getPlaylist());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Playlist loaded");
            e.printStackTrace();
            //different song´s id
        } finally {
            strategy.closeReadable();
        }
    }


    public void save() throws IOException,RemoteException {
        System.out.println(strategy + " 1");
        try {

            strategy.openWriteableSongs();
            for (Song s : model.getAllSongs()) {
                strategy.writeSong(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            strategy.closeWriteable();
        }
        try {
            strategy.openWriteablePlaylist();

            for (Song s : model.getPlaylist()) {
                strategy.writeSong(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            strategy.closeWriteable();
        }

    }

    public synchronized static String modifyTemps(String time) {
        if (time != null) {
            temps = time;
        }
        return temps;
    }

}







