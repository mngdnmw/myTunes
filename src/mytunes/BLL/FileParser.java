/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.FileManager;

/**
 *
 * @author jeppe
 */
public class FileParser {

    FileManager fileManager = new FileManager();
    

    public List<Song> getSongs() {
        return fileManager.getAllSongs();
    }

    public void addSong(StringProperty songName, StringProperty songPath) {
        fileManager.addSong(songName, songPath);
    }

    public void sendPlaylistName(String playlistName) {
        fileManager.savePlaylist(playlistName);
    }
    
    public List<Playlist> getAllPlaylists(){
        try {
            return fileManager.getAll();
        } catch (IOException ex) {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
