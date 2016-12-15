package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.BLL.SongManager;

public class PlaylistViewController implements Initializable {

    private SongManager songManager = SongManager.getInstance();

    private Playlist currentPlaylist = null;

    @FXML
    private TextField textFileNamePlaylist;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Saves a playlist.
     *
     * @param event
     */
    @FXML
    private void clickSaveAddPlaylist(ActionEvent event) {
        if (currentPlaylist == null) {
            songManager.addPlaylist(textFileNamePlaylist.getText());
        } else {
            songManager.editPlaylist(currentPlaylist.getPlaylistId(), textFileNamePlaylist.getText());

        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the text field.
     *
     * @return
     */
    public TextField getTextFileNamePlaylist() {
        return textFileNamePlaylist;
    }

    /**
     * Sets the textfield.
     *
     * @param textFileNamePlaylist
     */
    public void setTextFileNamePlaylist(TextField textFileNamePlaylist) {
        this.textFileNamePlaylist = textFileNamePlaylist;
    }

    /**
     * Cancels adding a new playlist.
     *
     * @param event
     */
    @FXML
    private void clickCancelNewPlaylist(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the playlist to a given playlist.
     *
     * @param playlist
     */
    public void setPlaylist(Playlist playlist) {
        currentPlaylist = playlist;
        if (currentPlaylist != null) {
            this.textFileNamePlaylist.setText(playlist.getName());
        }
    }

}
