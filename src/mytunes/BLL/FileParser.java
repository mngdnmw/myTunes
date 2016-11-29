/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.util.List;
import javafx.beans.property.StringProperty;
import mytunes.BE.Song;
import mytunes.DAL.FileManager;

/**
 *
 * @author jeppe
 */
public class FileParser
{
    FileManager fileManager = new FileManager();
    
    public List<Song> getSongs()
    {
        return fileManager.getAllSongs();
    }
    
    public void addSong(StringProperty songName, StringProperty songPath)
    {
        fileManager.addSong(songName, songPath);
    }
    
}
