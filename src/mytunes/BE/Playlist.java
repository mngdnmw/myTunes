package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

public class Playlist
{

    private int playlistID;
    private String playlistName;
    private String playlistDuration;
    private List<Song> songList = new ArrayList();

    public Playlist(int playlistID, String playlistName)
    {

        this.playlistID = playlistID;
        this.playlistName = playlistName;

        System.out.println(playlistName + " : " + playlistID);

    }

    public int getPlaylistId()
    {
        return playlistID;

    }

    public String getName()
    {
        return playlistName;
    }

    public void setName(String playlistName)
    {
        this.playlistName = playlistName;
    }

    public String getPlaylistDuration()
    {
        return playlistDuration;
    }

    public void setPlaylistDuration(String playlistDuration)
    {
        this.playlistDuration = playlistDuration;
    }

    public List<Song> getSongList()
    {
        return songList;
    }

    public void addSongToPlaylist(Song song)
    {
        songList.add(song);
    }

    public void removeSongFromPlaylist(Song song)
    {
        for (Song song1 : songList)
        {
            if (song1.equals(song))
            {
                songList.remove(song);
            }
        }

    }

}
