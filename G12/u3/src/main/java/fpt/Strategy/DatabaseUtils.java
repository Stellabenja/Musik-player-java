package fpt.Strategy;

import fpt.interfaces.SerializableStrategy;
import fpt.interfaces.Song;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by corin on 17.06.2017.
 */
public class DatabaseUtils implements SerializableStrategy {

    private Connection con;
    private String tableName;
    private List<fpt.model.Song> songTable;

    public DatabaseUtils() {
        createDatabase();
    }

    public void createDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection tempCon = DriverManager.getConnection("jdbc:sqlite:music.db")) {
                System.out.println("Opened database successfully");

                try (Statement stmt = tempCon.createStatement()) {
                    String createLibrary = "CREATE TABLE LIBRARY" +
                            "(ID LONG PRIMARY KEY  NOT NULL," +
                            "TITEL            TEXT ," +
                            "INTERPRET        TEXT ," +
                            "ALBUM            TEXT ," +
                            "PATH             TEXT )";
                    stmt.executeUpdate(createLibrary);
                    System.out.println("created Library Table");
                } catch (SQLException eLTest) {
                    System.out.println("Library Table already exists");
                }

                try (Statement stmt = tempCon.createStatement()) {
                    String createPlaylist = "CREATE TABLE PLAYLIST" +
                            "(ID LONG PRIMARY KEY  NOT NULL," +
                            "TITEL            TEXT ," +
                            "INTERPRET        TEXT ," +
                            "ALBUM            TEXT ," +
                            "PATH             TEXT )";
                    stmt.executeUpdate(createPlaylist);
                    System.out.println("created Playlist Table");
                } catch (SQLException eTest) {
                    System.out.println("Playlist Table already exist");
                }
            }
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertSong(Song s) {
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO  " +tableName+ "  VALUES (?,?,?,?,?)")) {
            pstmt.setLong(1, s.getId());
            pstmt.setString(2, s.getTitle());
            pstmt.setString(3, s.getInterpret());
            pstmt.setString(4, s.getAlbum());
            pstmt.setString(5, s.getPath());
            pstmt.executeUpdate();
            System.out.println("Song " + s.getTitle() + " inserted");
        } catch (SQLException | NullPointerException e) {
            System.out.println("Song already exist");
            //e.printStackTrace();

        }
        //System.out.println("Song insertSong inserted");
    }
    public fpt.model.Song findSongByID(long id){

        fpt.model.Song s = new fpt.model.Song();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT *  FROM  " + tableName + "  WHERE ID =" + id);
            ResultSet rs;
            rs = pstmt.executeQuery();
            s.setId(rs.getInt("ID"));
            s.setTitle(rs.getString("TITEL"));
            s.setInterpret(rs.getString("INTERPRET"));
            s.setAlbum(rs.getString("ALBUM"));
            s.setPath(rs.getString("PATH"));

            System.out.println("Song :" + rs.getString("TITEL"));
            System.out.println("id :" + rs.getLong("ID"));
            rs.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return s;
    }

    public ArrayList<fpt.model.Song> getAllSongsFromTable(){
        ArrayList<fpt.model.Song> sList = new ArrayList<>();
        try {
            PreparedStatement pstmtL = con.prepareStatement("SELECT * FROM  " + tableName) ;
            ResultSet res = pstmtL.executeQuery();
            while(res.next()){
                sList.add(new fpt.model.Song(res.getInt("ID"),res.getString("TITEL"),res.getString("INTERPRET"),res.getString("ALBUM"),res.getString("PATH")));

            }res.close();
        }catch (SQLException sl){
            //sl.printStackTrace();
        }

        return sList;


    }

    public void deleteSongWithID(long id){
        try {
            PreparedStatement pstmtL = con.prepareStatement("DELETE  FROM PLAYLIST WHERE ID = ? ");
            pstmtL.setLong(1,id);
            pstmtL.executeUpdate();
            System.out.println("Song deleted");
        }catch (SQLException|NullPointerException e){
            e.printStackTrace();
        }

    }

    @Override
    public void openWriteableSongs() throws IOException {

        connect();
        tableName = "LIBRARY";
    }

    @Override
    public void openReadableSongs() throws IOException {
        connect();
        tableName = "LIBRARY";
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        connect();
        tableName = "PLAYLIST";
        Song s =getAllSongsFromTable().remove(0);
        deleteSongWithID(s.getId());
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        connect();
        tableName = "PLAYLIST";
    }

    @Override
    public void writeSong(fpt.interfaces.Song s) throws IOException {

        if (s != null) {
            insertSong(s);
        } else {
            throw new IOException("Song to write doesn't exist");
        }
        //System.out.println("Song write2  inserted");
    }

    @Override
    public fpt.interfaces.Song readSong() throws IOException, ClassNotFoundException {
        if (songTable == null) {
            songTable = getAllSongsFromTable();
        }
        if (songTable.isEmpty()) {
            songTable = null;
            throw new IOException("End of table");

        }
        System.out.println(songTable);
        return songTable.remove(0);
    }

    @Override
    public void closeReadable() throws IOException {
        try {
            con.close();
        } catch (SQLException | NullPointerException e) {}
    }

    @Override
    public void closeWriteable() throws IOException {
        try {
            con.close();
        } catch (SQLException | NullPointerException e) {}
    }

    private void connect() throws IOException {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:sqlite:music.db");
            }
        } catch (SQLException e) {
            throw new IOException("Couldn't open Database Connection");
        }
    }
    public void returnSing(){
        try {
            connect();
            System.out.println(getAllSongsFromTable());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
