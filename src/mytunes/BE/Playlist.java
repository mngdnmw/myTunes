package mytunes.BE;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Playlist {

    //private final int id;
    private String name;
    private String playlistDuration;
    private List<Integer> songsRelations;
    private static int nextId = 0;
    private final IntegerProperty id;
    

    public Playlist(String name) {
        //this.id = id;
        this.name = name;
        songsRelations = new ArrayList<>();
        id = new SimpleIntegerProperty(nextId++);
        
    }
    
    public int getID()
    {
        return id.get();
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
