package mytunes.GUI.Controller;

import static com.sun.deploy.config.JREInfo.clear;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import mytunes.BE.Song;
import mytunes.BLL.SongManager;

import org.tritonus.share.sampled.file.TAudioFileFormat;

public class SongTableViewController implements Initializable
{

    private SongManager songManager = SongManager.getInstance();
    private Song currentSong = null;
    private Long durationOfSong;

    @FXML
    private ComboBox<String> comboCategory;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextField textFieldArtist;
    @FXML
    private TextField textFieldTime;
    @FXML
    private TextField textFieldFilePath;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        fillComboBox();
    }

    /**
     * Selects an audiofile from a filechooser window.
     *
     * @param event
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    @FXML
    private void clickChooseFromFile(ActionEvent event) throws UnsupportedAudioFileException, IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".mp3", "*.mp3"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        fileChooser.setTitle("Vælg sang...");
        File file = fileChooser.showOpenDialog(textFieldFilePath.getScene().getWindow()).getAbsoluteFile();
        textFieldFilePath.setText(file.getAbsolutePath());
        getArtistFromSong(file);
        getTitleFromSong(file);
        getDurationFromSong(file);

    }

    /**
     * Gets the artist from the file tag.
     *
     * @param song
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    private void getArtistFromSong(File song) throws UnsupportedAudioFileException, IOException
    {

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(song);
        if (fileFormat instanceof TAudioFileFormat)
        {
            Map<String, Object> properties = ((TAudioFileFormat) fileFormat).properties();

            String key = "author";

            String artist = (String) properties.get(key);
            textFieldArtist.setText(artist);
        }
    }

    /**
     * Gets the title from the file tag.
     *
     * @param song
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    private void getTitleFromSong(File song) throws UnsupportedAudioFileException, IOException
    {

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(song);
        if (fileFormat instanceof TAudioFileFormat)
        {
            Map<String, Object> properties = ((TAudioFileFormat) fileFormat).properties();

            String key = "title";

            String title = (String) properties.get(key);
            textFieldTitle.setText(title);
        }
    }

    /**
     * Gets the duration from the file tag.
     *
     * @param song
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    private void getDurationFromSong(File song) throws UnsupportedAudioFileException, IOException
    {

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(song);
        if (fileFormat instanceof TAudioFileFormat)
        {
            Map<String, Object> properties = ((TAudioFileFormat) fileFormat).properties();

            String key = "duration";

            Long microseconds = (Long) properties.get(key);

            textFieldTime.setText(songManager.calcDuration(microseconds));
            durationOfSong = microseconds;
        } else
        {
            throw new UnsupportedAudioFileException();
        }
    }

    /**
     * Closes the window for adding a new song.
     *
     * @param event
     */
    @FXML
    private void clickCloseAddSong(ActionEvent event)
    {
        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }

    /**
     * Fills the combo box with categories.
     */
    private void fillComboBox()
    {
        ObservableList<String> comboItems
                = FXCollections.observableArrayList("", "Blues", "Classical",
                        "Country", "Folk", "Electronic", "Jazz", "Hip-hop",
                        "Reggae", "Funk&Soul", "Rock", "Pop", "Soundtrack", "Religious",
                        "Traditional");
        comboCategory.setItems(comboItems);
        comboCategory.getSelectionModel().selectFirst();
    }

    //Getters for song info 
    public TextField getTextFieldTitle()
    {
        return textFieldTitle;
    }

    public TextField getTextFieldArtist()
    {
        return textFieldArtist;
    }

    public TextField getTextFieldTime()
    {
        return textFieldTime;
    }

    public TextField getTextFieldFilePath()
    {
        return textFieldFilePath;
    }

    public ComboBox<String> getComboCategory()
    {
        return comboCategory;
    }

    //Setters
    public void setTextFieldTitle(TextField textFieldTitle)
    {
        this.textFieldTitle = textFieldTitle;
    }

    public void setTextFieldArtist(TextField textFieldArtist)
    {
        this.textFieldArtist = textFieldArtist;
    }

    public void setTextFieldTime(TextField textFieldTime)
    {
        this.textFieldTime = textFieldTime;
    }

    public void setTextFieldFilePath(TextField textFieldFilePath)
    {
        this.textFieldFilePath = textFieldFilePath;
    }

    /**
     * Saves the song with the entered parameters.
     *
     * @param event
     */
    @FXML
    private void clickSaveAddSong(ActionEvent event)
    {
        if (currentSong == null)
        {
            songManager.addSong(
                    textFieldTitle.getText(),
                    textFieldArtist.getText(),
                    comboCategory.getValue(),
                    durationOfSong,
                    textFieldFilePath.getText());
        } else
        {
            durationOfSong = currentSong.getSongDuration();
            songManager.removeSongLibrary(currentSong.getSongId());
            songManager.addSong(
                    textFieldTitle.getText(),
                    textFieldArtist.getText(),
                    comboCategory.getValue(),
                    durationOfSong,
                    textFieldFilePath.getText());
        }

        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the textfields to the info from a selected song.
     *
     * @param song
     */
    public void setSong(Song song)
    {
        currentSong = song;
        if (currentSong != null)
        {
            this.textFieldTitle.setText(song.getSongTitle());
            this.textFieldArtist.setText(song.getSongArtist());
            this.textFieldTime.setText(song.getReadDuration());
            this.textFieldFilePath.setText(song.getSongPath());
        }
    }
}
