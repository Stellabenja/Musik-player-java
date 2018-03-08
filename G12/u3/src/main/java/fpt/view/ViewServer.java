package fpt.view;

import fpt.controller.ControllerServer;
import fpt.interfaces.MusikPlayer;
import fpt.interfaces.Song;
import fpt.model.SongList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;


public class ViewServer extends BorderPane{
    private Song selectedSong;


    VBox stack = new VBox();
    VBox stack2 = new VBox();
    VBox stack3 = new VBox();
    HBox hBox = new HBox();
    HBox hBox2 = new HBox();
    HBox hbox3 = new HBox();

    private final  ListView<Song> songListV = new ListView<>();
    private final ListView<Song> playListV = new ListView<>();

    private TextField titelS = new TextField();
    private TextField interpret = new TextField();
    private TextField album = new TextField();
    private TextField timeBox = new TextField();
    private Button addToPlayList;
    private Button removeFromPlaylist;
    private Button addAll;
    private Button load;
    private Button save;
    private Button stop;
    private Button play;
    private Button next;
    private Button commit;
    private Button pause;
    private ChoiceBox<String> choiceBox;
    private MusikPlayer remoteServer;

    public Button getAddToPlayButton(){

        return addToPlayList;
    }
    public Button getRemoveFromPLay(){

        return removeFromPlaylist;
    }
    public Button getPlay(){

        return play;
    }
    public Button getCommit(){
        return commit;
    }
    public TextField getAlbum(){
        return album;
    }
    public TextField getTitelS(){
        return titelS;
    }
    public TextField getInterpret(){
        return interpret;
    }

    public Button getAddall(){
        return addAll;
    }
    public Button getStop(){
        return stop;
    }
    public Button getPause(){
        return pause;
    }
    public Button getNext(){
        return next;
    }
    public Button getLoad(){ return  load;}
    public Button getSave(){return  save;}


    public ListView<Song> getSongList(){
        return songListV;
    }
    public ListView<Song> getPlayList(){
        return playListV;
    }


    public ViewServer() {
        Label label1 = new Label("Titel :");
        titelS.setPrefSize(200, 10);
        Label label2 = new Label("Interpret :");
        interpret.setPrefSize(200, 10);
        Label time = new Label("Time");
        Label label3 = new Label("Album :");
        album.setPrefSize(90, 10);
        songListV.setPrefSize(350, 550);
        playListV.setPrefSize(350, 550);

        songListV.setCellFactory(e -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getTitle() == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        playListV.setCellFactory(e -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getTitle() == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        load = new Button("Load");
        save = new Button("Save");
        pause = new Button("||");
        load.setPrefSize(60, 10);
        save.setPrefSize(60, 10);
        addAll = new Button("Add all");
        addAll.setPrefSize(100, 10);
        addToPlayList = new Button("Add to playlist");
        addToPlayList.setPrefSize(140, 10);
        removeFromPlaylist = new Button("Remove from playlist");
        removeFromPlaylist.setPrefSize(160, 10);
        stop = new Button();
        play = new Button();
        next = new Button(">>");
        commit = new Button("Commit");
        javafx.scene.shape.Rectangle r = new javafx.scene.shape.Rectangle(10,10);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                0.0, 5.0,
                10.0, 10.0,
                0.0, 15.0 });

        play.setGraphic(polygon);
        stop.setGraphic(r);
        stop.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        play.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        load.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        save.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        pause.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        addAll.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

        addToPlayList.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        removeFromPlaylist.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        next.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        commit.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

        stop.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        play.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        load.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        pause.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        save.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        addAll.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        addToPlayList.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        removeFromPlaylist.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        next.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));
        commit.setTextFill(javafx.scene.paint.Paint.valueOf("#000000"));

        choiceBox = new ChoiceBox(FXCollections.observableArrayList());

        hBox.setSpacing(40);
        hBox.getChildren().addAll(choiceBox, load, save,time,timeBox);
        hBox2.getChildren().addAll(stack, stack2, stack3);
        stack.getChildren().addAll(songListV);
        stack2.getChildren().addAll(playListV);
        stack3.getChildren().addAll(label1, titelS, label2, interpret, label3, album, hbox3, addToPlayList,removeFromPlaylist);
        stack3.setSpacing(10);
        hbox3.getChildren().addAll(stop, play, pause, next, commit);
        hbox3.setSpacing(5);
        hBox.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        stack.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox2.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        songListV.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

        setTop(hBox);
        setBottom(addAll);
        setCenter(hBox2);
        this.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        songListV.setOnMouseClicked(event -> {
            Song s = songListV.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedSong = s;
                titelS.setText(s.getTitle());
                interpret.setText(s.getInterpret());
                album.setText(s.getAlbum());
            }
        });
        playListV.setOnMouseClicked(event -> {
            Song s = playListV.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedSong = s;
                titelS.setText(s.getTitle());
                interpret.setText(s.getInterpret());
                album.setText(s.getAlbum());
            }
        });
        

        commit.setOnAction(event -> {
                    if(selectedSong==null){
                        return;
                    }
                    selectedSong.setAlbum(album.getText());
                    selectedSong.setInterpret(interpret.getText());
                    selectedSong.setTitle(titelS.getText());
                }
        );

    }
    public void choiceBox() {
        try {
            this.remoteServer = (MusikPlayer) Naming.lookup("//localhost/musicplayer");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            choiceBox = new ChoiceBox(FXCollections.observableArrayList(remoteServer.returnStrategies()));
            choiceBox.setPrefWidth(350);
            //choiceBox.getSelectionModel().getSelectedItem();
            choiceBox.getSelectionModel().selectedItemProperty().addListener(e -> {
                try {
                    remoteServer.setStrategy(choiceBox.getSelectionModel().getSelectedIndex());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void fillPlayList(SongList items) {
        playListV.setItems(items);
    }

    public void fillSongList(SongList items) {
        songListV.setItems(items);
    }

    public void fillTimeBox(String timeSong){
        timeBox.setText(timeSong);
    }
}
