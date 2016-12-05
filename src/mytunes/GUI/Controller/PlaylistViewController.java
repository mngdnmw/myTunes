package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.SongManager;

public class PlaylistViewController extends SongManager implements Initializable
{

    @FXML
    private TextField textFileNamePlaylist;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void clickSaveNewPlaylist(ActionEvent event)
    {
        super.addPlaylist(textFileNamePlaylist.getText());
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
    
    
    
    public void savePlaylistFromView(){
        List<Playlist> playlist = new ArrayList();
    }
    
    public TextField getTextFiledNamePlaylist()
    {
        return textFileNamePlaylist;
    }

    public void setTextFiledNamePlaylist(TextField textFileNamePlaylist)
    {
        this.textFileNamePlaylist = textFileNamePlaylist;
    }

    @FXML
    private void clickCancelNewPlaylist(ActionEvent event)
    {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    
}
