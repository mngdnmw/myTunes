package mytunes.GUI.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import mytunes.BE.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
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
    private TableView<Song> tblSongsOnPlaylist;
    @FXML
    private TableView<Song> tblViewLibrary;
    @FXML
    private TextField textFieldFilterSearch;

    private Window primaryStage;

    private boolean atEndOfMedia = false;

    private SongManager songManager = SongManager.getInstance();

    private SongModel songModel = new SongModel();

    private String selectedSong;
    private int songPlaying;

    private Song lastSelectedSong;
    private Playlist lastSelectedPlaylist;

    private Song lastPlayedSong;

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
    @FXML
    private Label labelCurrentlyPlaying;
    
    @FXML
    private Label lblCurrentSong;
    @FXML
    private Button lblPreviousSong;

    @FXML
    private TableColumn<Song, String> columnSongsInPlaylist;


    //Initializes the controller class.
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        populateLists();
        setStartingSong();
        //volumeControl();
        //compareSongRelations();
        

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
            songPlaying=0;
            selectedSong = tblViewLibrary.getItems().get(songPlaying).getSongPath();
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

        loadSongsIntoLibrary();

        //Songs in playlist viewer
        columnSongsInPlaylist.setCellValueFactory(new PropertyValueFactory("songTitle"));

        loadSongsIntoPlaylist();

    }

    @FXML
    private void clickAddSongPlaylist(ActionEvent event)
    {
        if (lastSelectedPlaylist != null && lastSelectedSong != null)
        {
            saveSongRelations();
        }
        lastSelectedPlaylist.addSongToPlaylist(lastSelectedSong);

        loadSongsIntoPlaylist();
    }

    /**
     * Opens a dialogue window to create a new playlist, and pauses execution
     * until it closes.
     */
    private void showPlaylistWindow(String title, Playlist playlist)
    {
        try
        {
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

        } catch (IOException e)
        {
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

        showPlaylistWindow("Edit Playlist", selectedPlaylist);
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
     * Gets the selected playlist from the playlist view and deletes it. Pops up
     * with an alert message if no playlist is selected.
     *
     * @param event
     */
    @FXML
    private void clickDeletePlaylist(ActionEvent event)
    {
        int selectedIndex = tblViewPlaylists.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0)
        {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete selected playlist?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                songManager.removePlaylist(tblViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId());
                tblViewPlaylists.getItems().remove(selectedIndex);
            }

        } else if (selectedIndex <= 0)

        {
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

        try
        {
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

        } catch (IOException ex)
        {

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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this song?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {

                songManager.removeSongLibrary(tblViewLibrary.getSelectionModel().getSelectedItem().getSongId());
                tblViewLibrary.getItems().remove(selectedIndex);

            }
        } else if (selectedIndex <= 0)

        {

            Alert alert = new Alert(AlertType.WARNING);
            //alert.initOwner(mainApp ""MyTunes".getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Song Selected");
            alert.setContentText("Please select a song inside the music library.");

            alert.showAndWait();
        }
    }

    public void saveSongRelations()
    {
        songManager.saveSongRelations(lastSelectedPlaylist.getPlaylistId(), lastSelectedSong.getSongId());
        //compareSongRelations();
    }
    
//        public void compareSongRelations()
//    {
//        try
//        {
//            List<int[]> relations = new ArrayList();
//            relations = songManager.getSongRelations();
//            System.out.println("Started for finding songs in playlists");
//            
//            for (int[] relation : relations)
//            {
//                System.out.println("1");
//                for (Playlist list : songManager.getAllPlaylists())
//                {
//                    System.out.println("2");
//                    if (list.getPlaylistId() == relation[0])
//                    {
//                        System.out.println("3");
//                        for (Song song : songManager.getAllSongs())
//                        {
//                            System.out.println("4");
//                            if (song.getSongId() == relation[1])
//                            {
//                                System.out.println(song.getSongTitle() + " matches " + list.getName());
//                                list.addSongToPlaylist(song);
//                                System.out.println("Did the thing");
//                            }
//                            
//                            
//                        }
//                    }
//                }
//            }
//        } catch (IOException ex)
//        {
//            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, "Compare Song relations.", ex);
//        }
//    }

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
        labelCurrentlyPlaying.setText("");

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
        if (tblViewLibrary.getItems().isEmpty())
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No selection");
            alert.setHeaderText("No song selected");
            alert.setContentText("Please select a song in the library.");

            alert.showAndWait();
        } else

        {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)

            {
                mediaPlayer.pause();
                playButton.setText("▷");
            } else
            {
                mediaPlayer.play();
                playButton.setText("||");
            }

            if (lastSelectedSong != null)
            {
                labelCurrentlyPlaying.setText(lastSelectedSong.getSongArtist() + " - " + lastSelectedSong.getSongTitle());

            } else
            {
                labelCurrentlyPlaying.setText(lastPlayedSong.getSongArtist() + " - " + lastPlayedSong.getSongTitle());
                tblViewLibrary.getSelectionModel().clearAndSelect(0);
            }
        }
    }

    /**
     * Sends a request through the layers of the program to receive all songs,
     * arranges them into an observable list and loads said list into the view.
     */
    private void loadSongsIntoLibrary()
    {

        try
        {
            ObservableList<Song> songLibrary = FXCollections.observableArrayList(songManager.getAllSongs());
            tblViewLibrary.setItems(songLibrary);
        } catch (IOException ex)
        {
            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadSongsIntoPlaylist()
    {
        //compareSongRelations();
        if (lastSelectedPlaylist != null)
        {
            if (lastSelectedPlaylist.getSongList() != null)
            {
                ObservableList<Song> songPlaylist = FXCollections.observableArrayList(lastSelectedPlaylist.getSongList());
                tblSongsOnPlaylist.setItems(songPlaylist);
            }
        }
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

    @FXML
    private void clickNextButton(ActionEvent actionevent)
    {
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            System.out.println("does nothing");
            Song music = null;
           /* if (playingFrom == SONGS_ON_PLAYLIST) {
                if (songPlaying < tblSongsOnPlaylist.getItems().size()) {
                    songPlaying++;
                    tblSongsOnPlaylist.getSelectionModel().clearAndSelect(songPlaying);
                    System.out.println("reaches here?");
                    music = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();

                } else {
                    songPlaying = 0;
                    System.out.println("Does this when reaching the end");
                    tblSongsOnPlaylist.getSelectionModel().clearAndSelect(songPlaying);
                    music = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
                }
            }
            if (playingFrom == ALL_SONGS) {*/
                if (songPlaying < tblViewLibrary.getItems().size() - 1) {
                    songPlaying++;
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    System.out.println("reaches here?");
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();

                    selectedSong = tblViewLibrary.getItems().get(songPlaying).getSongPath();
                    media = new Media(new File(selectedSong).toURI().toString());
                } else {
                    songPlaying = 0;
                    System.out.println("Does this when reaching the end");
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();
                }
            //}
            if (music != null) {
                String path = music.getSongPath();
                lblCurrentSong.setText("Song: "
                        + music.getSongTitle()
                        + " Artist: "
                        + music.getSongArtist());
                Media media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
               mediaPlayer.play();
               }
        }
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
        lastSelectedSong = tblViewLibrary.getSelectionModel().getSelectedItem();
        if (lastSelectedSong != null)
        {
             if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.stop();
            playButton.setText("▷");
        }
            System.out.println(lastSelectedSong.getSongTitle());
            selectedSong = lastSelectedSong.getSongPath();
            media = new Media(new File(selectedSong).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            System.out.println(selectedSong);
        }


//        selectedSong = tblViewLibrary.getSelectionModel().getSelectedItem().getSongPath();
//        media = new Media(new File(selectedSong).toURI().toString());
//        
//        mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setOnEndOfMedia(new endOfSongEvent());
//        System.out.println(selectedSong);

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

    @FXML
    private void setPlaylist(MouseEvent event)
    {
        lastSelectedPlaylist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        System.out.println(lastSelectedPlaylist.getName());
        loadSongsIntoPlaylist();
    }

    @FXML
    private void selectSongInPlaylist(MouseEvent event)
    {
        lastSelectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        System.out.println(lastSelectedSong.getSongTitle());

        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.stop();
            playButton.setText("▷");

        }

        selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem().getSongPath();
        media = new Media(new File(selectedSong).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        System.out.println(selectedSong);
    }
    
    public void ClickPreviousSong(ActionEvent event)
    {
        {
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            System.out.println("does nothing");
            Song music = null;
           
                if (songPlaying > 0) {
                    songPlaying--;
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    System.out.println("reaches here?");
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();

                    selectedSong = tblViewLibrary.getItems().get(songPlaying).getSongPath();
                    media = new Media(new File(selectedSong).toURI().toString());
                } else {
                    songPlaying = tblViewLibrary.getItems().size() -1;
                    System.out.println("Does this when reaching the end");
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();
                }
            //}
            if (music != null) {
                String path = music.getSongPath();
                lblCurrentSong.setText("Song: "
                        + music.getSongTitle()
                        + " Artist: "
                        + music.getSongArtist());
                Media media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
               mediaPlayer.play();
               }
        }
    }
    }

    
    private class endOfSongEvent implements Runnable
            {
                public void run(){
                clickNextButton(null);
                }
            }
}
