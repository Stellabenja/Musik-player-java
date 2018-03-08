package fpt.Strategy;

import fpt.interfaces.SerializableStrategy;
import fpt.interfaces.Song;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * Created by benja on 16.06.2017.
 */
public class XMLStrategy implements SerializableStrategy {
    private XMLEncoder xmlencoder;
    private XMLDecoder xmldecoder;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    @Override
    public void openWriteableSongs() throws IOException {
        fileOutputStream= new FileOutputStream("library.xml");
        xmlencoder=new XMLEncoder(fileOutputStream);

    }


    @Override
    public void openReadableSongs() throws IOException {
       fileInputStream=new FileInputStream("library.xml");
       xmldecoder = new XMLDecoder(fileInputStream);

    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        fileOutputStream= new FileOutputStream("playlist.xml");
        xmlencoder=new XMLEncoder(fileOutputStream);


    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fileInputStream=new FileInputStream("playlist.xml");
        xmldecoder = new XMLDecoder(fileInputStream);


    }

    @Override
    public void writeSong(Song s) throws IOException {
        if(s!= null && xmlencoder!=null){
            xmlencoder.writeObject(s);
            xmlencoder.flush();//schreib gepufferte Daten
        }else { throw new IOException("XMLDecoder does not exist!!!!!!!!!!!");}

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        Song readSong = null;
        if (xmldecoder!= null){
                try{
                    readSong = (Song) xmldecoder.readObject();
                }catch (ArrayIndexOutOfBoundsException a){

                }

        }else{throw  new IOException("warning ! XMLDecoder DOES NOT EXIST");}
        return readSong;
    }

    @Override
    public void closeReadable() {
    if(xmldecoder != null){
        xmldecoder.close();
        xmldecoder=null;
    }
    }

    @Override
    public void closeWriteable() {
        if(xmlencoder!=null){
            xmlencoder.close();
            xmlencoder=null;
        }

    }
}
