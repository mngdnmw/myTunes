package mytunes.BLL;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.FileManager;

public class FileParser
{

    FileManager fileManager = new FileManager();

    public List<Song> getSongs() throws IOException
    {
        return fileManager.getAllSongs();
    }

    public void sendSongInfo(String songTitle, String songArtist, String songCategory, String songPath)
    {
        fileManager.saveSong(songTitle, songArtist, songCategory, songPath);
    }

    public void sendPlaylistName(String playlistName)
    {
        fileManager.savePlaylist(playlistName);
    }

    public List<Playlist> getAllPlaylists()
    {
        try
        {
            return fileManager.getAll();
        } catch (IOException ex)
        {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void removePlaylist(int id)
    {
        try
        {
            fileManager.deleteByPlaylist(id);
        } catch (IOException ex)
        {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void removeSong(int id)
    {
        try
        {
            fileManager.deleteBySong(id);
        } catch (IOException ex)
        {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
