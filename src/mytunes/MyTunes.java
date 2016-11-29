/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kristoffers
 */
public class MyTunes extends Application
{
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("MyTunes - Happy  Thoughts");
        primaryStage.centerOnScreen();
        Parent root = FXMLLoader.load(getClass().getResource("GUI/View/MainMyTunesView.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
