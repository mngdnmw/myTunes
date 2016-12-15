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
import java.util.Collections;
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

    ObservableList<Song> songPlaylist;

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

    }

    /**
     * Sets the player to play the first song in the library, to avoid a null
     * pointer exception.
     */
    private void setStartingSong()
    {

        if (tblViewLibrary.getItems().isEmpty())
        {

            lastPlayedSong = null;

        } else
        {

            songPlaying = 0;
            lastPlayedSong = tblViewLibrary.getItems().get(songPlaying);

            media = new Media(new File(lastPlayedSong.getSongPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            labelCurrentlyPlaying.setText(lastPlayedSong.getSongArtist() + " - " + lastPlayedSong.getSongTitle());
        }

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

    /**
     * Adds selected song to the selected playlist.
     *
     * @param event
     */
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

    /**
     * Opens the window to create a new playlist.
     *
     * @param event
     */
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
        if (!tblViewPlaylists.getSelectionModel().isEmpty())
        {
            Playlist selectedPlaylist = tblViewPlaylists.getSelectionModel().getSelectedItem();

            showPlaylistWindow("Edit Playlist", selectedPlaylist);
        }
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

    /**
     * Moves a song up in the playlist.
     *
     * @param event
     */
    @FXML
    private void clickToggleUpPlaylist(ActionEvent event)
    {
        Song songToMoveUp = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        if (songToMoveUp != null)
        {
            tblSongsOnPlaylist.getSelectionModel().clearAndSelect(handleToggle(songToMoveUp, 1) - 1);
        }
    }

    /**
     * Handles moving songs up or down in a playlist.
     *
     * @param songToMove
     * @param direction
     * @return
     */
    private int handleToggle(Song songToMove, int direction)
    {
        int indexId = songPlaylist.indexOf(songToMove);
        if (indexId != 0)
        {
            if (direction == 1)
            {
                Collections.swap(songPlaylist, indexId - 1, indexId);
            } else
            {
                Collections.swap(songPlaylist, indexId + 1, indexId);
            }
        } else if (direction == 1)
        {
            indexId = +1;
        } else
        {
            indexId = -1;
        }
        return indexId;
    }

    /**
     * Moves a song down in a playlist.
     *
     * @param event
     */
    @FXML
    private void clickToggleDownPlaylist(ActionEvent event)
    {
        Song songToMoveDown = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        if (songToMoveDown != null)
        {
            tblSongsOnPlaylist.getSelectionModel().clearAndSelect(handleToggle(songToMoveDown, -1) + 1);
        }
    }

    /**
     * Opens a new dialogue window to add a song to the library, and pauses
     * execution until it has closed again.
     *
     * @param event
     * @throws IOException
     */
    private void showSongTableWindow(String title, Song song) throws IOException
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader
                    .setLocation(MyTunes.class
                            .getResource("GUI/View/SongTableView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            SongTableViewController controller = loader.getController();
            controller.setSong(song);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
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

    /**
     * Opens the window to add a new song to the library.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickNewSongLibrary(ActionEvent event) throws IOException
    {
        showSongTableWindow("New Song", null);

    }

    /**
     * Opens the window to edit a song in the library.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickEditSongLibrary(ActionEvent event) throws IOException
    {
        if (!tblViewLibrary.getSelectionModel().isEmpty())
        {
            Song selectedSong = tblViewLibrary.getSelectionModel().getSelectedItem();

            showSongTableWindow("Edit Song", selectedSong);
            loadSongsIntoLibrary();

        }
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
            alert.setTitle("No Selection");
            alert.setHeaderText("No Song Selected");
            alert.setContentText("Please select a song inside the music library.");

            alert.showAndWait();
        }
    }

    /**
     * Sends the relations between songs and playlists down through the layers
     * of the program to be saved in a file.
     */
    public void saveSongRelations()
    {
        songManager.saveSongRelations(lastSelectedPlaylist.getPlaylistId(), lastSelectedSong.getSongId());
        compareSongRelations();
    }

    /**
     * Compares the relations between songs and playlists and adds songs into
     * the playlists they should be in.
     *
     * @return
     */
    public List<Song> compareSongRelations()
    {
        List<Song> songList = new ArrayList();
        List<Integer> songIds = new ArrayList();
        try
        {
            songIds = songManager.getSongRelations(lastSelectedPlaylist.getPlaylistId());

            for (Integer songId : songIds)
            {
                for (Song song : songManager.getAllSongs())
                {
                    if (songId == song.getSongId())
                    {
                        songList.add(song);
                    }

                }
            }
            return songList;
        } catch (IOException ex)
        {
            Logger.getLogger(MainMyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        //volumeControl();
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

    /**
     * Sends a request through the layers of the program to receive all songs in
     * playlists, arranges them into an observable list and loads said list into
     * the view.
     */
    private void loadSongsIntoPlaylist()
    {

        if (lastSelectedPlaylist != null)
        {
            if (lastSelectedPlaylist.getSongList() != null)
            {
                songPlaylist = FXCollections.observableArrayList(compareSongRelations());
                tblSongsOnPlaylist.setItems(songPlaylist);
            }
        } else
        {

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

    /**
     * Changes to the next song in the list.
     *
     * @param actionevent
     */
    @FXML
    private void clickNextButton(ActionEvent actionevent)
    {
        volumeControl();
        mediaPlayer.seek(mediaPlayer.getTotalDuration());
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();

            Song music = null;

            if (songPlaying < tblViewLibrary.getItems().size() - 1)
            {
                songPlaying++;
                tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);

                music = tblViewLibrary.getSelectionModel().getSelectedItem();

                selectedSong = tblViewLibrary.getItems().get(songPlaying).getSongPath();
                media = new Media(new File(selectedSong).toURI().toString());

            } else
            {
                songPlaying = 0;
                tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                music = tblViewLibrary.getSelectionModel().getSelectedItem();
            }
            if (music != null)
            {
                String path = music.getSongPath();
                labelCurrentlyPlaying.setText(music.getSongTitle()
                        + " - "
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
        //volumeControl();
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    /**
     * Removes a song from a playlist.
     *
     * @param event
     */
    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event)
    {
        Song selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        if (selectedSong != null)
        {
            lastSelectedPlaylist.removeSongFromPlaylist(selectedSong);
            tblSongsOnPlaylist.getItems().remove(selectedSong);
        }
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
        if (!tblViewLibrary.getSelectionModel().isEmpty())
        {

            if (mediaPlayer != null)
            {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.stop();
                playButton.setText("▷");
            }
            }
            selectedSong = lastSelectedSong.getSongPath();
            media = new Media(new File(selectedSong).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
    }

    /**
     * Handles searching for keywords. Updates with each keystroke.
     *
     * @param event
     * @throws IOException
     */
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

    /**
     * Gets keyword and returns filtered observable list.
     *
     * @param query
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
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

    /**
     * Controls volume.
     */
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

    /**
     * Sets the selected playlist.
     *
     * @param event
     */
    @FXML
    private void setPlaylist(MouseEvent event)
    {
        lastSelectedPlaylist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        loadSongsIntoPlaylist();
    }

    /**
     * Selects a song in the playlist.
     *
     * @param event
     */
    @FXML
    private void selectSongInPlaylist(MouseEvent event)
    {
        if (tblSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
        {
            lastSelectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        }

        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.stop();
            playButton.setText("▷");

        }

        selectedSong = tblSongsOnPlaylist.getSelectionModel().getSelectedItem().getSongPath();
        media = new Media(new File(selectedSong).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    /**
     * Plays the previous song.
     *
     * @param event
     */
    public void clickPreviousSong(ActionEvent event)
    {
        {
            volumeControl();
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
            if (mediaPlayer != null)
            {
                mediaPlayer.stop();
                Song music = null;

                if (songPlaying > 0)
                {
                    songPlaying--;
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();

                    selectedSong = tblViewLibrary.getItems().get(songPlaying).getSongPath();
                    media = new Media(new File(selectedSong).toURI().toString());
                } else
                {
                    songPlaying = tblViewLibrary.getItems().size() - 1;
                    tblViewLibrary.getSelectionModel().clearAndSelect(songPlaying);
                    music = tblViewLibrary.getSelectionModel().getSelectedItem();
                }

                if (music != null)
                {
                    String path = music.getSongPath();
                    labelCurrentlyPlaying.setText(music.getSongTitle()
                            + " - "
                            + music.getSongArtist());
                    Media media = new Media(new File(path).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                }
            }
        }
    }

    /**
     * Event at end of a song.
     */
    private class endOfSongEvent implements Runnable
    {

        public void run()
        {
            clickNextButton(null);
            volumeControl();
        }
    }
}
