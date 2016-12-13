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

    public void addSong(String songTitle, String songArtist, String songCategory, Long songDuration, String songPath)
    {
        fileParser.sendSongInfo(songTitle, songArtist, songCategory, songDuration, songPath);
    }

    public List<Song> getAllSongs() throws IOException {
        return fileParser.getSongs();
    }

    public void addPlaylist(String playlistName) {
        fileParser.sendPlaylistName(playlistName);
    }
    public void editPlaylistName(String playlistName)
    {
        
    }

    public List<Playlist> getAllPlaylists(){
        return fileParser.getAllPlaylists();
    }

    public void removePlaylist(int id) {
        fileParser.removePlaylist(id);
    }
    
    public void removeSongLibrary(int id) {
        fileParser.removeSong(id);
    }

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
