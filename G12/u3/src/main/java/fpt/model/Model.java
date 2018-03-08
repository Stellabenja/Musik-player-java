package fpt.model;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import fpt.Strategy.DatabaseUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import fpt.interfaces.Song;

/**
 *
 */

public class Model {
    private SongList allSongs = new SongList();
    private SongList playlist = new SongList();

    public void addSongsFromDir(String directory) {
        try {
            for (File f : new File(directory).listFiles()) {
                if (f.getAbsolutePath().endsWith(".mp3")) {
                    allSongs.addSong(new fpt.model.Song(IDgenerator.getNextID(),f.getName(),"","", f.toURI().toString()));

                }
            }
        } catch (IDOverFlowException e1) {
            e1.printStackTrace();
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }

    public Song findSongById(long id){
        return allSongs.findSongByID(id);
    }

    public SongList getAllSongs() {
        return allSongs;
    }

    public SongList getPlaylist() {
        return playlist;
    }




}
