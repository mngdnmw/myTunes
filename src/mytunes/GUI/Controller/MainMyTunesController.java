package mytunes.GUI.Controller;

import java.io.File;
import mytunes.BE.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import mytunes.BE.Song;
import mytunes.GUI.Model.SongManager;
import mytunes.MyTunes;

public class MainMyTunesController implements Initializable {

    @FXML
    private TableView<Playlist> tblViewPlaylists;
    @FXML
    private TextArea tblSongsOnPlaylist;
    @FXML
    private TableView<Song> tblViewLibrary;
    @FXML
    private TextField textFieldFilterSearch;

    private Window primaryStage;

    private boolean atEndOfMedia = false;

    private SongManager songManager = SongManager.getInstance();

    @FXML
    Slider volumeSlider;

    // Create Media and MediaPlayer
    private MediaPlayer mediaPlayer;
    private Media media;
    //Playlist table
    @FXML
    private TableColumn<Playlist, String> columnPlaylistName;
    @FXML
    private TableColumn<Playlist, String> columnNumSongs;
    @FXML
    private TableColumn<Playlist, String> columnDuration;
    //Song library
    @FXML
    private TableColumn<Song, String> tblViewLibraryColumnTitle;
    @FXML
    private TableColumn<Song, String> tblViewLibraryColumnArtist;
    @FXML
    private TableColumn<Song, String> tblViewLibraryColumnCategory;
    @FXML
    private TableColumn<Song, String> tblViewLibraryColumnTime;
    @FXML
    private Slider timeSlider;
    private Duration duration;
    @FXML
    private Button playButton;
    @FXML
    private Button rldButton;

    //Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateLists();
        

    }
    private void populateLists(){
        //Playlist viewer
        columnPlaylistName.setCellValueFactory(new PropertyValueFactory("name"));
    //    tblViewLibraryColumnArtist.setCellValueFactory(new PropertyValueFactory("artist"));
    //   tblViewLibraryColumnTitle.setCellValueFactory(new PropertyValueFactory("title"));
        loadPlaylistsIntoViewer();

        //Song viewer
        tblViewLibraryColumnTitle.setCellValueFactory(new PropertyValueFactory("songTitle"));
        tblViewLibraryColumnArtist.setCellValueFactory(new PropertyValueFactory("songArtist"));
        tblViewLibraryColumnCategory.setCellValueFactory(new PropertyValueFactory("songCategory"));
        tblViewLibraryColumnTime.setCellValueFactory(new PropertyValueFactory("songDuration"));
        try {
            loadSongsIntoLibrary();
        } catch (IOException ex) {
            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clickAddSongPlaylist(ActionEvent event) {
    }

    @FXML

    private void clickNewPlaylist(ActionEvent event) {

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(MyTunes.class
                            .getResource("GUI/View/PlaylistView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Playlist");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
            loadPlaylistsIntoViewer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickEditPlaylist(ActionEvent event) {
        /*Playlist selectedPlaylist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        if(selectedPlaylist != null)
        {
            boolean okClicked = MyTunes.showPlaylistView(selectedPlaylist);
            if(okClicked) {
                showPlaylistView(selectedPlaylist);
            }
        } else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(MyTunes.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Playlist selected");
            alert.setContentText("Please select a playlist to edit.");
            
            alert.showAndWait();
        }*/

    }

    @FXML
    private void clickDeletePlaylist(ActionEvent event) {
        int selectedIndex = tblViewPlaylists.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            tblViewPlaylists.getItems().remove(selectedIndex);
            songManager.removePlaylist(tblViewPlaylists.getSelectionModel().getSelectedItem().getId());
            loadPlaylistsIntoViewer();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            //alert.initOwner(mainApp ""MyTunes".getPrimaryStage());
            alert.setTitle("No Slection");
            alert.setHeaderText("No Playlist Selected");
            alert.setContentText("Please select a playlist in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void clickToggleUpPlaylist(ActionEvent event) {
    }

    @FXML
    private void clickToggleDownPlaylist(ActionEvent event) {
    }

    @FXML
    private void clickNewSongLibrary(ActionEvent event) throws IOException {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(MyTunes.class
                            .getResource("GUI/View/SongTableView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Song");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

        } catch (IOException ex) {

            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, null, ex);

        }
        loadSongsIntoLibrary();
    }

    @FXML
    private void clickEditSongLibrary(ActionEvent event) {
    }

    @FXML
    private void clickRemoveSongLibrary(ActionEvent event) {
        int selectedIndex = tblViewLibrary.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tblViewLibrary.getItems().remove(selectedIndex);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            //alert.initOwner(mainApp ""MyTunes".getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Song Selected");
            alert.setContentText("Please select a song inside the music library.");
        }
    }

    @FXML
    private void clickCloseProgram(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void clickSearch(ActionEvent event) {
    }

    @FXML
    private void clickStopButton(ActionEvent event) {
        MediaPlayer.Status status = mediaPlayer.getStatus();

        if (status == MediaPlayer.Status.UNKNOWN || status == status.HALTED) {
            return;
        }

        if (status == MediaPlayer.Status.PAUSED
                || status == status.READY
                || status == status.STOPPED) {
            if (atEndOfMedia) {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                atEndOfMedia = false;
            }
//             << << << < HEAD
//            
//            mediaPlayer.play();
//             == == ==
//                    =  >>> >>> > origin / KingKristoffers
        } else {
            mediaPlayer.stop();
            playButton.setText("▷");
        }
    }

    @FXML
    private void clickPlayPauseButton(ActionEvent event) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            playButton.setText("▷");
        } else {
            mediaPlayer.play();
            playButton.setText("||");
        }
//String path = "src/mytunes/MusicLibrary/" + "RedArmyChoir.mp3";
        String path = tblViewLibrary.getSelectionModel().getSelectedItem().getSongPath();

        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

//    private void readSongsIntoLibrary()
//    {
//        if (songManager.getAllSongs() == null)
//        {
//        }
//        ObservableList<Song> songLibrary = FXCollections.observableArrayList(songManager.getAllSongs());
//
//        tblViewLibrary.setItems(songLibrary);
//    }
    private void loadSongsIntoLibrary() throws IOException {

        ObservableList<Song> songLibrary = FXCollections.observableArrayList(songManager.getAllSongs());
        tblViewLibrary.setItems(songLibrary);
    }

    private void loadPlaylistsIntoViewer() {
        ObservableList<Playlist> playlistLists = FXCollections.observableArrayList(songManager.getAllPlaylists());
        tblViewPlaylists.setItems(playlistLists);

    }

    private void clickNextButton(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
    }

    @FXML
    private void clickReloadButton(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event) {
    }

}
