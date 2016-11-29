/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Fjord82
 */
public class PlaylistViewController implements Initializable
{

    @FXML
    private TextField textFiledNamePlaylist;

    /**
     * Initializes the controller class.
     */
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
    private void clickCloseNewPlaylist(ActionEvent event)
    {
    }
    
}
