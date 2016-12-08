/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import java.util.List;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Song;

/**
 *
 * @author Fjord82
 */
public class SongModel
{

    private final ObservableList<Song> songList;

    public SongModel()
    {
        this.songList = FXCollections.observableArrayList();
    }

    public ObservableList<Song> getSongList()
    {
        return songList;
    }

    public void setSongs(List<Song> songs)
    {
        songList.clear();
        songList.addAll(songs);
    }

}
