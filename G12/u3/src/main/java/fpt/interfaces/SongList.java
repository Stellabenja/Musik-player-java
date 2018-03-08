package fpt.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SongList extends Remote, Serializable, Iterable<Song>{
    boolean addSong(Song s) throws RemoteException;
    boolean deleteSong(Song s) throws RemoteException;
    void setList(ArrayList<Song> s) throws RemoteException;
    ArrayList<Song> getList() throws RemoteException;
    void deleteAllSongs() throws RemoteException;
    int sizeOfList() throws RemoteException;
    Song findSongByPath(String name) throws RemoteException;
}
