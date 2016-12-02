package mytunes.GUI.Model;

import java.util.List;
import javafx.beans.property.StringProperty;
import mytunes.BE.Playlist;
import mytunes.BLL.FileParser;

/**
 *
 * @author jeppe
 */
public abstract class SongManager
{
    
    FileParser fileParser = new FileParser();
    
    public void addSong(StringProperty songName, StringProperty songPath)
    {
        fileParser.addSong(songName, songPath);
    }
    
    public void addPlaylist(String playlistName){
        fileParser.sendPlaylistName(playlistName);
    }
    
    public List<Playlist> getAllPlaylists(){
        return fileParser.getAllPlaylists();
    }
}
