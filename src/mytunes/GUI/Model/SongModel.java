
package mytunes.GUI.Model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Song;



public class SongModel {

    private final ObservableList<Song> songList;

    public SongModel() {
        this.songList = FXCollections.observableArrayList();
    }

    public ObservableList<Song> getSongList() {
        return songList;
    }

    public void setSongs(List<Song> songs) {

        songList.clear();
        songList.addAll(songs);
    }

}
