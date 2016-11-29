/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import mytunes.BE.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import mytunes.MyTunes;

/**
 * FXML Controller class
 *
 * @author Fjord82
 */
public class MainMyTunesController implements Initializable
{

    @FXML
    private TableView<Playlist> tblViewPlaylists;
    @FXML
    private TextArea tblSongsOnPlaylist;
    @FXML
    private TableView<?> tblViewLibrary;
    @FXML
    private TextField textFieldFilterSearch;
    private Window primaryStage;

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
        try{
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MyTunes.class.getResource("GUI/View/PlaylistView.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("New Playlist");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        dialogStage.showAndWait();
//
//        return controller.isOkClicked();
    } catch (IOException e) {
        e.printStackTrace();
    }
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
