package mytunes.BE;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
//import mytunes.BLL.IDManager;

public class Playlist
{

    //private int id = 0;
    //private final int id; need to come back and to change id to final 
    private int playlistID;
    private String playlistName;
    private String playlistDuration;
    private List<Integer> songsRelations;
    private List<Song> songList;

//    private final IntegerProperty id;
    //IDManager idManager = IDManager.getInstance();

    public Playlist(int playlistID, String playlistName)
    {

        this.playlistID = playlistID;
        this.playlistName = playlistName;
        
        System.out.println(playlistName + " : " + playlistID);

    }

    public int getId()
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
