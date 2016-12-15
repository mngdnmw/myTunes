package mytunes.BLL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;

public class SongManager {

    private static SongManager instance;
    FileParser fileParser = new FileParser();

    public static SongManager getInstance() {
        if (instance == null) {
            instance = new SongManager();
        }
        return instance;
    }

    /**
     * Sends the information for adding a song down through the layers.
     *
     * @param songTitle
     * @param songArtist
     * @param songCategory
     * @param songDuration
     * @param songPath
     */
    public void addSong(String songTitle, String songArtist, String songCategory, Long songDuration, String songPath) {
        fileParser.sendSongInfo(songTitle, songArtist, songCategory, songDuration, songPath);
    }

    public void editSong(int songId, String songTitle, String songArtist, String songCategory, Long songDuration, String songPath) throws IOException {
        fileParser.sendEditSongInfo(songId, songTitle, songArtist, songCategory, songDuration, songPath);
    }

    /**
     * Gets all songs from the fileparser and sends them on.
     *
     * @return
     * @throws IOException
     */
    public List<Song> getAllSongs() throws IOException {
        return fileParser.getSongs();
    }

    /**
     * Sends the information for adding a playlist down through the layers.
     *
     * @param playlistName
     */
    public void addPlaylist(String playlistName) {
        fileParser.sendPlaylistName(playlistName);
    }

    /**
     * Sends the information for the song relations file down through the
     * layers.
     *
     * @param playlistID
     * @param songID
     */
    public void saveSongRelations(int playlistID, int songID) {
        fileParser.saveSongRelations(playlistID, songID);
    }

    /**
     * Gets the song relations info and sends it up.
     *
     * @param playlistID
     * @return
     * @throws IOException
     */
    public List<Integer> getSongRelations(int playlistID) throws IOException {
        return fileParser.getSongRelations(playlistID);
    }

    /**
     * Returns all playlists from the fileparser.
     *
     * @return
     */
    public List<Playlist> getAllPlaylists() {
        return fileParser.getAllPlaylists();
    }

    /**
     * Sends the id for a playlist that needs to be removed down through the
     * layers.
     *
     * @param id
     */
    public void removePlaylist(int id) {
        fileParser.removePlaylist(id);
    }

    /**
     * Sends the id for a playlist that needs to be edited down through the
     * layers.
     *
     * @param id
     * @param playlistName
     */
    public void editPlaylist(int id, String playlistName) {
        fileParser.editPlaylist(id, playlistName);
    }

    /**
     * Sends the id for a song that needs to be removed down through the layers.
     *
     * @param id
     */
    public void removeSongLibrary(int id) {
        fileParser.removeSong(id);
    }

    /**
     * Sends the id for a song that needs to be removed down through the layers.
     *
     * @param id
     */
    public void removeSongFromRelations(int id) {
        fileParser.removeSongFromRelations(id);
    }

    /**
     * Returns a list of all songs that match the query.
     *
     * @param query
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public List<Song> search(String query) throws FileNotFoundException, IOException {
        List<Song> allSongs = fileParser.getSongs();
        List<Song> searchList = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getAllSongStringInfo().toLowerCase().contains(query.toLowerCase())) {
                searchList.add(song);
            }
        }

        return searchList;
    }

    /**
     * Gets the duration of a song in microseconds and calculates it into
     * something more readable.
     *
     * @param microSeconds
     * @return
     */
    public String calcDuration(Long microSeconds) {
        int mili = (int) (microSeconds / 1000);
        int sec = (mili / 1000) % 60;
        int min = (mili / 1000) / 60;
        String duration;
        if (sec < 10) {
            duration = min + ":" + "0" + sec;
        } else {
            duration = min + ":" + sec;
        }

        return duration;
    }

}
