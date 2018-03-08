package fpt.Strategy;

import fpt.interfaces.SerializableStrategy;
import fpt.interfaces.Song;
import fpt.interfaces.SongList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benja on 16.06.2017.
 */
public class BinaryStrategy implements SerializableStrategy {
    private FileInputStream fileInputStream = null;
    private FileOutputStream fileOutputStream = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    @Override
    public void openWriteableSongs() throws IOException {
        fileOutputStream = new FileOutputStream("library.ser",false);
        outputStream = new ObjectOutputStream(fileOutputStream);

    }

    @Override
    public void openReadableSongs() throws IOException {
        fileInputStream = new FileInputStream("library.ser");
        inputStream = new ObjectInputStream(fileInputStream);
        try {
            Song song = (Song)inputStream.readObject();
            System.out.println(song.getId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        fileOutputStream = new FileOutputStream("playlist.ser",false);
        outputStream = new ObjectOutputStream(fileOutputStream);


    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fileInputStream = new FileInputStream("playlist.ser");
        inputStream = new ObjectInputStream(fileInputStream);

            try {
                Song song = (Song)inputStream.readObject();
                System.out.println(song.getId());
            } catch (ClassNotFoundException e) {
                System.out.println("error");
                e.printStackTrace();
            }
    }

    @Override
    public void writeSong(Song s) throws IOException {

            outputStream.writeObject(s);
            outputStream.flush();//schreib gepufferte Daten
            System.out.println(s.getTitle());

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        Song readSong =null;
        if(inputStream!=null){
            try {
               while(inputStream.read()>-1) {
                   readSong = (Song) inputStream.readObject();
                   System.out.println(readSong.getTitle());
               }
            } catch (EOFException e){
          return null;
            }
        }
        return readSong;
    }

    @Override
    public void closeReadable() throws IOException {
        try{
        fileInputStream.close();
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void closeWriteable() throws IOException {
        try {
        fileOutputStream.close();
            outputStream.close();
        }catch (IOException es){
            es.printStackTrace();
        }

    }
}
