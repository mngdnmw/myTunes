/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import mytunes.BE.Song;

/**
 *
 * @author jeppe
 */
public class FileManager
{
    private List<Song> songList = new ArrayList();
    
    public List<Song> getAllSongs()
    {
        if (songList.isEmpty())
            return null;
        else return songList;
    }
    
    public void addSong(StringProperty songName, StringProperty songPath)
    {
        Song s = new Song(songName, songPath);
    }
}
