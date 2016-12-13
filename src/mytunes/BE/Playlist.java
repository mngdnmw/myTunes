package mytunes.BE;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Playlist {

    //private int id = 0;
    //private final int id; need to come back and to change id to final 
    private String name;
    private String playlistDuration;
    private List<Integer> songsRelations;
    private static int nextId = 0;
    private final IntegerProperty playlistId;
    

    public Playlist(String playlistName) {

        this.name = playlistName;
    
        playlistId = new SimpleIntegerProperty(nextId++);
        
    }

    public Playlist(int playlistId, String playlistName)
    {
        this(playlistName);
        this.playlistId.set(playlistId);
    }
    
    public int getPlaylistId()
    {
        return playlistId.get();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaylistDuration() {
        return playlistDuration;
    }

    public void setPlaylistDuration(String playlistDuration) {
        this.playlistDuration = playlistDuration;
    }

    public List<Integer> getSongsRelations() {
        return songsRelations;
    }

    public void setSongsRelations(List<Integer> songsRelations) {
        this.songsRelations = songsRelations;
    }

//    public int getId() {
//        return id;
//    }

}
