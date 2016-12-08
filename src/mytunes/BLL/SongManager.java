package mytunes.BLL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.FileParser;

/**
 *
 * @author jeppe
 */
public class SongManager
{

    private static SongManager instance;

    public static SongManager getInstance()
    {
        if (instance == null)
        {
            instance = new SongManager();
        }

        return instance;

    }

    FileParser fileParser = new FileParser();

    public void addSong(String songTitle, String songArtist, String songCategory, String songPath)
    {
        fileParser.sendSongInfo(songTitle, songArtist, songCategory, songPath);
    }

    public List<Song> getAllSongs() throws IOException
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

    public void removePlaylist(int id)
    {
        fileParser.removePlaylist(id);
    }

    public List<Song> search(String query) throws FileNotFoundException, IOException
    {
        List<Song> allSongs = fileParser.getSongs();
        List<Song> searchList = new ArrayList<>();

        for (Song song : allSongs)
        {
            if (song.getAllSongStringInfo().toLowerCase().contains(query.toLowerCase()))
            {
                searchList.add(song);
            }
        }

        return searchList;
    }

}