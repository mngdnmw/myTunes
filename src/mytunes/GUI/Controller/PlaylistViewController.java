package mytunes.GUI.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistViewController implements Initializable
{

    @FXML
    private TextField textFiledNamePlaylist;
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
        String playlistName = "";
        playlistName = textFiledNamePlaylist.getText();
        try(BufferedWriter bw =
                new BufferedWriter(new FileWriter("playlist.txt")))
        {
            bw.write(playlistName);
        } catch (IOException ex)
        {
            Logger.getLogger(PlaylistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public TextField getTextFiledNamePlaylist()
    {
        return textFiledNamePlaylist;
    }

    public void setTextFiledNamePlaylist(TextField textFiledNamePlaylist)
    {
        this.textFiledNamePlaylist = textFiledNamePlaylist;
    }

    @FXML
    private void clickCancelNewPlaylist(ActionEvent event)
    {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    
}
