package fpt.interfaces;

import fpt.model.Model;
import fpt.view.ViewClient;
import fpt.view.ViewServer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by corin on 14.07.2017.
 */
public interface RemoteClient extends Remote {
    fpt.model.SongList songList() throws RemoteException;
    void playNext() throws RemoteException;
    void play() throws RemoteException;
    void link(Model model, ViewClient view) throws RemoteException;
    void stopButton(String duration) throws RemoteException;
    void setStrategy(int a) throws RemoteException;
    void load() throws IOException,RemoteException;
    void save() throws IOException,RemoteException;
    void fillSongs(long id,String titel,String interpret,String album) throws RemoteException;
    void addToPlay(long id) throws RemoteException;
    void commitSong(long id,String titel,String interpret,String album) throws RemoteException;
    void pause(String duration) throws RemoteException;
}
