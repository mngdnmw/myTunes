package mytunes.GUI.Controller;

import mytunes.BE.Playlist;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mytunes.BLL.FileParser;
import mytunes.GUI.Model.SongManager;
import mytunes.MyTunes;

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

    private FileParser fileParser = new FileParser();
    private SongManager songManager = new SongManager();

    //Initializes the controller class.
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
        try
        {
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

            dialogStage.show();

        } catch (IOException e)
        {
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
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MyTunes.class.getResource("GUI/View/SongTableView.fxml"));
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
