package mytunes.BE;

import java.util.List;

public class Playlist
{

    private int playlistID;
    private String playlistName;
    private String playlistDuration;
    private List<Integer> songsRelations;

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

    public List<Integer> getSongsRelations()
    {
        return songsRelations;
    }

    public void setSongsRelations(List<Integer> songsRelations)
    {
        this.songsRelations = songsRelations;
    }
}
