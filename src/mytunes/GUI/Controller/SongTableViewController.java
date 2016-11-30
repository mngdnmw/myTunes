/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.GUI.Model.SongManager;

/**
 * FXML Controller class
 *
 * @author Fjord82
 */
public class SongTableViewController extends SongManager implements Initializable
{

    @FXML
    private ComboBox<?> comboCategory;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextField textFieldArtist;
    @FXML
    private TextField textFieldTime;
    @FXML
    private TextField textFieldFilePath;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        
        
        
    }

    @FXML
    private void clickMoreInfo(ActionEvent event)
    {
    }

    @FXML
    private void clickChooseFromFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter(".mp4", "*.mp4"),
                new FileChooser.ExtensionFilter(".mp3", "*.mp3"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        fileChooser.setTitle("Vælg sang...");
        fileChooser.showOpenDialog(textFieldFilePath.getScene().getWindow());
        File file = fileChooser.showOpenDialog(textFieldFilePath.getScene().getWindow());
        textFieldFilePath.setText(file.getAbsolutePath());
    }

    @FXML
    private void clickSaveAddSong(ActionEvent event)
    {
        super.addSong(textFieldArtist.getText(), textFieldFilePath.getText());
        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clickCloseAddSong(ActionEvent event)
    {
        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }

}
