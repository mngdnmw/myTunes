/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public void addSong(String songName, String songPath, String songArtist)
    {
        Song s = new Song(songName, songPath, songArtist);
        songList.add(s);
        
       /* try
            (BufferedWriter bw = new BufferedWriter(new FileWriter("Songlist.txt")))
        {
            bw.write(songList.);
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    } 
}
