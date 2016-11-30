/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import javafx.beans.property.StringProperty;
import mytunes.BLL.FileParser;

/**
 *
 * @author jeppe
 */
public abstract class SongManager
{
    
    FileParser fileParser = new FileParser();
    
    public void addSong(String songName, String songPath)
    {
        fileParser.addSong(songName, songPath);
    }
    
}
