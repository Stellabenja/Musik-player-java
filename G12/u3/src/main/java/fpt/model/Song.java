package fpt.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.Media;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.jdbc.Strategy;

import javax.persistence.*;
import java.io.*;

/**

 */

@Entity
@Table(name = "Library")
public class Song implements fpt.interfaces.Song,Externalizable,Serializable{

    private static final long serialVersionUID = 1L;


    @Persistent
    @Strategy("StringPropertyValueHandler")
    private SimpleStringProperty path = new SimpleStringProperty();
    @Persistent
    @Strategy("StringPropertyValueHandler")
    private SimpleStringProperty titel = new SimpleStringProperty();
    @Persistent
    @Strategy("StringPropertyValueHandler")
    private SimpleStringProperty album = new SimpleStringProperty();
    @Persistent
    @Strategy("StringPropertyValueHandler")
    private SimpleStringProperty interpret = new SimpleStringProperty();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "Song_SEQ")
    private long id;
    private Media media;





    public Song(){}


    public Song(long id, String titel,String interpret,String album, String path){
        this.id = id;
        this.titel.set(titel);
        this.interpret.set(interpret);
        this.album.set(album);
        this.path.set(path);
        this.media = new Media(path);
    }


    @Override
    public String getAlbum() {
        return album.get();

    }

    @Override
    public void setAlbum(String album) {
        this.album.set(album);
    }



    @Override
    public String getInterpret() {
        return interpret.get();
    }

    @Override
    public void setInterpret(String interpret) {
        this.interpret.set(interpret);

    }

    @Override
    public String getPath() {

        return path.get();
    }

    @Override
    public void setPath(String path) {
        this.path.set(path);

    }

    @Override
    public String getTitle() {
        return titel.getValue();
    }

    @Override
    public void setTitle(String title) {
        this.titel.set(title);
    }

    @Override
    public long getId() {
       
        return id;

    }


    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public ObservableValue<String> pathProperty() {
        return path;
    }

    @Override
    public ObservableValue<String> albumProperty() {
        return album;
    }

    @Override
    public ObservableValue<String> interpretProperty() {
        return interpret;
    }

    @Override
    public String toString() {
        return String.format("%02d", getId()) + " | " + getTitle();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(id);
        out.writeObject(titel);
        out.writeObject(interpret);
        out.writeObject(album);
        out.writeObject(path);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readInt();
        titel = (SimpleStringProperty) in.readObject();
        interpret = (SimpleStringProperty) in.readObject();
        album = (SimpleStringProperty) in.readObject();
        path = (SimpleStringProperty) in.readObject();

    }
}
