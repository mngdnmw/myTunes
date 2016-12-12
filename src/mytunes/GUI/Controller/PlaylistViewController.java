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
import mytunes.BLL.SongManager;

public class PlaylistViewController implements Initializable
{
    private SongManager songManager = SongManager.getInstance();
    private Playlist currentPlaylist = null;

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
        if(currentPlaylist == null)
        {
            songManager.addPlaylist(textFileNamePlaylist.getText());
        }else
        {
            //Needs to make a method thats modifies existing playlist
            //mangler at fortælle songmanager at der redigeres i navnet 
            currentPlaylist.setName(textFileNamePlaylist.getText());
        }
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
    
    public TextField getTextFileNamePlaylist()
    {
        return textFileNamePlaylist;
    }

    public void setTextFileNamePlaylist(TextField textFileNamePlaylist)
    {
        this.textFileNamePlaylist = textFileNamePlaylist;
    }

    @FXML
    private void clickCancelNewPlaylist(ActionEvent event)
    {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    void setPlaylist(Playlist playlist)
    {
        currentPlaylist = playlist;
        if(currentPlaylist != null)
        {
            this.textFileNamePlaylist.setText(playlist.getName());
        }
    }
    
}
