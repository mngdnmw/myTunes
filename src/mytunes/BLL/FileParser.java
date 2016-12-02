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

    public List<Song> getSongs()
    {
        return fileManager.getAllSongs();
    }

    public void addSong(String songName, String songPath, String songArtist)
    {
        fileManager.addSong(songName, songPath, songArtist);
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
}
