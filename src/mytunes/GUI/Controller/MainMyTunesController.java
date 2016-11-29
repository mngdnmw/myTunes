/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Fjord82
 */
public class MainMyTunesController implements Initializable
{

    @FXML
    private TableView<?> tblViewPlaylists;
    @FXML
    private TextArea tblSongsOnPlaylist;
    @FXML
    private TableView<?> tblViewLibrary;
    @FXML
    private TextField textFieldFilterSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void clickAddSongPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickNewPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickEditPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickDeletePlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickToggleUpPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clcikToggleDownPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event)
    {
    }

    @FXML
    private void clickNewSongLibrary(ActionEvent event)
    {
    }

    @FXML
    private void clickEditSongLibrary(ActionEvent event)
    {
    }

    @FXML
    private void clickRemoveSongLibrary(ActionEvent event)
    {
    }

    @FXML
    private void clickCloseProgram(ActionEvent event)
    {
    }

    @FXML
    private void clickSearch(ActionEvent event)
    {
    }
    
}
