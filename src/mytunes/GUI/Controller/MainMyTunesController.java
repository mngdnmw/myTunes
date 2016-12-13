package mytunes.GUI.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import mytunes.BE.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import mytunes.BE.Song;
import mytunes.BLL.SongManager;
import mytunes.GUI.Model.SongModel;
import mytunes.MyTunes;

public class MainMyTunesController implements Initializable
{

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

    private SongModel songModel = new SongModel();

    private String selectedSong;

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
    private TableColumn<Song, ?> tblViewLibraryColumnTime;

    private Duration duration;
    @FXML
    private Button playButton;
    @FXML
    private Button rldButton;

    //Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        populateLists();
        setStartingSong();
//        volumeControl();

    }

    /**
     * Sets the player to play the first song in the library, to avoid a null
     * pointer exception.
     */
    private void setStartingSong()
    {

        if (tblViewLibrary.getItems().isEmpty())
        {

            selectedSong = null;
        } else {
            selectedSong = tblViewLibrary.getItems().get(0).getSongPath();
            media = new Media(new File(selectedSong).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }


        System.out.println(selectedSong);
    }

    /**
     * Set cell value factories for the library and playlist tables, and loads
     * files from the Model>BLL>DAL into the view.
     */
    private void populateLists()
    {
        //Playlist viewer
        columnPlaylistName.setCellValueFactory(new PropertyValueFactory("name"));

        loadPlaylistsIntoViewer();

        //Song viewer
        tblViewLibraryColumnTitle.setCellValueFactory(new PropertyValueFactory("songTitle"));
        tblViewLibraryColumnArtist.setCellValueFactory(new PropertyValueFactory("songArtist"));
        tblViewLibraryColumnCategory.setCellValueFactory(new PropertyValueFactory("songCategory"));
        tblViewLibraryColumnTime.setCellValueFactory(new PropertyValueFactory("readDuration"));
        try
        {

            loadSongsIntoLibrary();
        } catch (IOException ex) {
            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clickAddSongPlaylist(ActionEvent event)
    {
    }

    
    private void showPlaylistWindow(String title, Playlist playlist)
    {
    /**
     * Opens a dialogue window to create a new playlist, and pauses execution
     * until it closes.
     */
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(MyTunes.class
                            .getResource("GUI/View/PlaylistView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            PlaylistViewController controller = loader.getController();
            controller.setPlaylist(playlist);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
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
    private void clickNewPlaylist(ActionEvent event)
    {

        showPlaylistWindow("New Playlist", null);
    }
    
    /**
     * Opens a dialogue window to create a new playlist, and pauses execution
     * until it closes.
     */

    @FXML
    private void clickEditPlaylist(ActionEvent event)
    {
        Playlist selectedPlaylist = tblViewPlaylists.getSelectionModel().getSelectedItem();

        showPlaylistWindow ("Edit Playlist", selectedPlaylist);
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
            //alert.initOwner(MyTunes.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Playlist selected");
            alert.setContentText("Please select a playlist to edit.");
            
            alert.showAndWait();
        }*/

   

    }

    /**
     * Gets the selected playlit from the playlist view and deletes it. Pops up
     * with an alert message if no playlist is selected.
     *
     * @param event
     */
    @FXML
    private void clickDeletePlaylist(ActionEvent event)
    {
        int selectedIndex = tblViewPlaylists.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            
            songManager.removePlaylist(tblViewPlaylists.getSelectionModel().getSelectedItem().getId());
            tblViewPlaylists.getItems().remove(selectedIndex);
            
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No playlist selected");
            alert.setContentText("Please select a playlist in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void clickToggleUpPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickToggleDownPlaylist(ActionEvent event)
    {
    }

    /**
     * Opens a new dialogue window to add a song to the library, and pauses
     * execution until it has closed again.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickNewSongLibrary(ActionEvent event) throws IOException
    {

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
    private void clickEditSongLibrary(ActionEvent event)
    {
    }

    /**
     * Removes a selected song from the song library. Pops up with an alert
     * message if no song has been selected.
     *
     * @param event
     */
    @FXML
    private void clickRemoveSongLibrary(ActionEvent event)
    {
        int selectedIndex = tblViewLibrary.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0)
        {   

            songManager.removeSongLibrary(tblViewLibrary.getSelectionModel().getSelectedItem().getId());
                        tblViewLibrary.getItems().remove(selectedIndex);
            
        } else
        {

            Alert alert = new Alert(AlertType.WARNING);
            //alert.initOwner(mainApp ""MyTunes".getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Song Selected");
            alert.setContentText("Please select a song inside the music library.");
        }
    }

    /**
     * Exits the program.
     *
     * @param event
     */
    @FXML
    private void clickCloseProgram(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    private void clickSearch(ActionEvent event)
    {
    }

    /**
     * Stops the currently playing song and resets it to the start again.
     *
     * @param event
     */
    @FXML
    private void clickStopButton(ActionEvent event)
    {
        mediaPlayer.stop();
        playButton.setText("▷");

    }

    /**
     * Plays if paused, and pauses if playing. Changes the symbol on the button
     * dynamically. Also handles the playback volume.
     *
     * @param event
     */
    @FXML
    private void clickPlayPauseButton(ActionEvent event)
    {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            playButton.setText("▷");
        } else {
            mediaPlayer.play();
            playButton.setText("||");
        }
    }

    /**
     * Sends a request through the layers of the program to receive all songs,
     * arranges them into an observable list and loads said list into the view.
     */
    private void loadSongsIntoLibrary() throws IOException
    {

        ObservableList<Song> songLibrary = FXCollections.observableArrayList(songManager.getAllSongs());
        tblViewLibrary.setItems(songLibrary);
    }

    /**
     * Sends a request through the layers of the program to receive all
     * playlists, arranges them into an observable list and loads said list into
     * the view.
     */
    private void loadPlaylistsIntoViewer()
    {
        ObservableList<Playlist> playlistLists = FXCollections.observableArrayList(songManager.getAllPlaylists());
        tblViewPlaylists.setItems(playlistLists);

    }

    private void clickNextButton(ActionEvent event)
    {
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
    }

    /**
     * Resets the currently playing song. Does not pause.
     *
     * @param event
     */
    @FXML
    private void clickReloadButton(ActionEvent event)
    {
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event)
    {
    }

    /**
     * Sets the currently playing song to whichever song is selected in library
     * view.
     *
     * @param event
     */
    @FXML
    private void setSong(MouseEvent event)
    {

        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.stop();
        }

        selectedSong = tblViewLibrary.getSelectionModel().getSelectedItem().getSongPath();
        media = new Media(new File(selectedSong).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        System.out.println(selectedSong);
    }

    @FXML
    private void search(KeyEvent event) throws IOException
    {
        if (textFieldFilterSearch.textProperty().get().isEmpty())
        {
            tblViewLibrary.setItems(songModel.getSongList());
        }
        String query = textFieldFilterSearch.getText().trim();
        tblViewLibrary.setItems(getSongList(query));

    }

    public ObservableList<Song> getSongList(String query) throws FileNotFoundException, IOException
    {
        List<Song> allSongs = songManager.getAllSongs();
        if (query.isEmpty())
        {
            return FXCollections.observableArrayList(allSongs);
        }
        ObservableList<Song> searchList = FXCollections.observableArrayList();

        for (Song song : allSongs)
        {
            if (song.getAllSongStringInfo().toLowerCase().contains(query.toLowerCase()))
            {
                searchList.add(song);
            }
        }
        return searchList;
    }

    private void volumeControl()
    {
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);

        volumeSlider.valueProperty().addListener(new InvalidationListener()
        {

            @Override
            public void invalidated(Observable observable)
            {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });

    }


}
