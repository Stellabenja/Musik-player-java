package fpt.Strategy;

import fpt.interfaces.SerializableStrategy;
import fpt.interfaces.Song;

import java.io.IOException;

/**
 * Created by benja on 16.06.2017.
 */
public class OpenJPA implements SerializableStrategy {
    @Override
    public void openWriteableSongs() throws IOException {

    }

    @Override
    public void openReadableSongs() throws IOException {

    }

    @Override
    public void openWriteablePlaylist() throws IOException {

    }

    @Override
    public void openReadablePlaylist() throws IOException {

    }

    @Override
    public void writeSong(Song s) throws IOException {

    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public void closeReadable() throws IOException {

    }

    @Override
    public void closeWriteable() throws IOException {

    }
}
