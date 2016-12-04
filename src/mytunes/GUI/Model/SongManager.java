package mytunes.GUI.Model;

import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.FileParser;

/**
 *
 * @author jeppe
 */
public abstract class SongManager
{

    FileParser fileParser = new FileParser();

    public void addSong(String songName, String songPath, String songArtist)
    {
        fileParser.addSong(songName, songPath, songArtist);
    }

    public List<Song> getAllSongs()
    {
        return fileParser.getSongs();
    }

    public void addPlaylist(String playlistName)
    {
        fileParser.sendPlaylistName(playlistName);
    }

    public List<Playlist> getAllPlaylists()
    {
        return fileParser.getAllPlaylists();
    }
}
