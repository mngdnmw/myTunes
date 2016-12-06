package mytunes.GUI.Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mytunes.BE.Song;
import mytunes.GUI.Model.SongManager;

/**
 * FXML Controller class
 *
 * @author Fjord82
 */
public class SongTableViewController implements Initializable
{
    private SongManager songManager = SongManager.getInstance();

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


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO

    }

    @FXML
    private void clickMoreInfo(ActionEvent event)
    {
        //TODO
    }

    @FXML
    private void clickChooseFromFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter(".mp4", "*.mp4"),
                new FileChooser.ExtensionFilter(".mp3", "*.mp3"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        fileChooser.setTitle("Vælg sang...");
        File file = fileChooser.showOpenDialog(textFieldFilePath.getScene().getWindow()).getAbsoluteFile();
        textFieldFilePath.setText(file.getAbsolutePath());
        
        
    }
    
    public void saveSongsFromView(){
        List<Song> song = new ArrayList();
    }

    @FXML
    private void clickSaveAddSong(ActionEvent event)
    {
        songManager.addSong(textFieldTitle.getText(), textFieldFilePath.getText(), textFieldArtist.getText());
        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clickCloseAddSong(ActionEvent event)
    {
        Stage stage = (Stage) textFieldFilePath.getScene().getWindow();
        stage.close();
    }
    
    //Getters for song info 

    public TextField getTextFieldTitle() {
        return textFieldTitle;
    }

    public TextField getTextFieldArtist() {
        return textFieldArtist;
    }

    public TextField getTextFieldTime() {
        return textFieldTime;
    }

    public TextField getTextFieldFilePath() {
        return textFieldFilePath;
    }
    
    //Setters

    public void setComboCategory(ComboBox<?> comboCategory) {
        this.comboCategory = comboCategory;
    }

    public void setTextFieldTitle(TextField textFieldTitle) {
        this.textFieldTitle = textFieldTitle;
    }

    public void setTextFieldArtist(TextField textFieldArtist) {
        this.textFieldArtist = textFieldArtist;
    }

    public void setTextFieldTime(TextField textFieldTime) {
        this.textFieldTime = textFieldTime;
    }

    public void setTextFieldFilePath(TextField textFieldFilePath) {
        this.textFieldFilePath = textFieldFilePath;
    }
    
    
}
