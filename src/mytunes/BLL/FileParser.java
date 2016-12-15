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

    FileManager fileManager = FileManager.getInstance();

    public List<Song> getSongs() throws IOException
    {
        return fileManager.getAllSongs();
    }

    /**
     * Passes information for adding a song on.
     *
     * @param songTitle
     * @param songArtist
     * @param songCategory
     * @param songDuration
     * @param songPath
     */
    public void sendSongInfo(String songTitle, String songArtist, String songCategory, Long songDuration, String songPath)
    {
        fileManager.saveSong(songTitle, songArtist, songCategory, songDuration, songPath);
    }

    /**
     * Passes information for adding a playlist on.
     *
     * @param playlistName
     */
    public void sendPlaylistName(String playlistName)
    {
        fileManager.savePlaylist(playlistName);
    }

    /**
     * Gets all playlists from the filemanager and passes them on.
     *
     * @return
     */
    public List<Playlist> getAllPlaylists()
    {
        try
        {
            return fileManager.getAllPlaylists();
        } catch (IOException ex)
        {
            Logger.getLogger(FileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Passes information for removing a playlist on.
     *
     * @param id
     */
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

    /**
     * Passes information for removing a song on.
     *
     * @param id
     */
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

    /**
     * Gets song relations and passes them on.
     *
     * @param playlistID
     * @return
     * @throws IOException
     */
    public List<Integer> getSongRelations(int playlistID) throws IOException
    {
        return fileManager.getSongPlaylistRelations(playlistID);
    }

    /**
     * Passes information for adding songrelations on.
     *
     * @param playlistID
     * @param songID
     */
    public void saveSongRelations(int playlistID, int songID)
    {
        fileManager.saveSongRelations(playlistID, songID);
    }
}
