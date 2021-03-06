package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private final int playlistID;
    private String playlistName;
    private final List<Song> songList = new ArrayList();

    public Playlist(int playlistID, String playlistName) {

        this.playlistID = playlistID;
        this.playlistName = playlistName;

    }

    public int getPlaylistId() {
        return playlistID;

    }

    public String getName() {
        return playlistName;
    }

    public void setName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void addSongToPlaylist(Song song) {
        songList.add(song);
    }

    /**
     * Removes a song from a playlist.
     *
     * @param song
     */
    public void removeSongFromPlaylist(Song song) {
        for (Song song1 : songList) {
            if (song1.equals(song)) {
                songList.remove(song);
            }
        }

    }

}
