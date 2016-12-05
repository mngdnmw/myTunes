package mytunes.DAL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Playlist;

import mytunes.BE.Song;

/**
 *
 * @author jeppe
 */
public class FileManager {

    //private String playlist;
    //Final variables for playlistlist.txt
    private static final int PLAYLIST_NAME_SIZE = 50;
    private static final int PLAYLIST_ID_SIZE = 50;
    private static final int RECORD_SIZE_PLAYLIST = PLAYLIST_NAME_SIZE + PLAYLIST_ID_SIZE;

    //Final variables for songlist.txt
    private static final int SONG_NAME_SIZE = 50;
    private static final int SONG_PATH_SIZE = 50;
    private static final int SONG_ARTIST_SIZE = 50;
    private static final int RECORD_SIZE_SONGLIST = SONG_NAME_SIZE + SONG_PATH_SIZE + SONG_ARTIST_SIZE;

    //Final variables for songRelations.txt
    private static final int RECORD_SIZE_RELATIONS = PLAYLIST_ID_SIZE + SONG_NAME_SIZE;

    private List<Song> songList = new ArrayList();
    private List<Playlist> playlistList = new ArrayList();

    public FileManager() {
    }

//    public FileManager(String playlistName) {
//
//        this.playlistName = playlistName;
//    }
//    public List<Playlist> getAllPlaylists() {
//        if (playlistList.isEmpty()) {
//            return null;
//        } else {
//            return playlistList;
//        }
//    }
    public void savePlaylist(String playlistName) {
//
//        try (BufferedWriter bw
//                = new BufferedWriter(new FileWriter(playlistName + ".txt"))) {
//            bw.write(playlistName);
//        } catch (IOException ex) {
//            Logger.getLogger(PlaylistViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Playlist playlist = new Playlist(playlistName);
        try {
            //        playlistList.add(playlist);
            addPlaylist(playlist);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveSong(String songName, String songPath, String songArtist) {
        Song song = new Song(songName, songPath, songArtist);
        try {
            addSong(song);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPlaylist(Playlist p) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File("playlists.txt"), "rw")) {
            raf.seek(raf.length());  // place the file pointer at the end of the file.
            raf.writeInt(p.getId());
            raf.writeBytes(String.format("\n" + "%-" + PLAYLIST_NAME_SIZE + "s", p.getName()).substring(0, PLAYLIST_NAME_SIZE));
        }
    }

    public void addSong(Song s) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File("songlist.txt"), "rw")) {
            raf.seek(raf.length());  // place the file pointer at the end of the file.
            //raf.writeInt(p.getId());
            //writing song name, path and artist
            raf.writeBytes(String.format("\n" + "%-" + SONG_NAME_SIZE + "s", s.getSongName()).substring(0, SONG_NAME_SIZE));
            raf.writeBytes(String.format("%-" + SONG_PATH_SIZE + "s", s.getSongPath()).substring(0, SONG_PATH_SIZE));
            raf.writeBytes(String.format("%-" + SONG_ARTIST_SIZE + "s", s.getSongArtist()).substring(0, SONG_ARTIST_SIZE));

        }
    }

    public List<Playlist> getAll() throws IOException {
        try (RandomAccessFile rafp = new RandomAccessFile(new File("playlists.txt"), "rw")) {
            List<Playlist> playlists = new ArrayList<>();

            while (rafp.getFilePointer() < rafp.length()) {
                playlists.add(getOnePlaylist(rafp));
            }
            return playlists;
        }
    }

    public List<Song> getAllSongs() throws IOException {
//        if (songList.isEmpty()) {
//            return null;
//        } else {
//            return songList;
//        }

        try(RandomAccessFile rafs = new RandomAccessFile(new File("songlist.txt"), "rw")){
            List<Song> songs = new ArrayList<>();
            while (rafs.getFilePointer() < rafs.length()) {
                songs.add(getOneSong(rafs));
            }
            return songs;
        } 
        
    }

    private Playlist getOnePlaylist(final RandomAccessFile raf) throws IOException {
        byte[] bytes = new byte[PLAYLIST_NAME_SIZE];
        int id = raf.readInt();
        raf.read(bytes);
        String playlistName = new String(bytes).trim();
        return new Playlist(playlistName);
    }
    
    private Song getOneSong(final RandomAccessFile rafs) throws IOException {
        byte[] name = new byte[SONG_NAME_SIZE];
        byte[] path = new byte[SONG_NAME_SIZE];
        byte[] artist = new byte[SONG_NAME_SIZE];
        rafs.read(name);
        rafs.read(path);
        rafs.read(artist);
        String songName = new String(name).trim();
        String songPath = new String(path).trim();
        String songArtist = new String(artist).trim();
        return new Song(songName, songPath, songArtist);
    }

    public Playlist getByPlaylist(String playlistName) throws IOException {
        try (RandomAccessFile rafp = new RandomAccessFile(new File("playlists.txt"), "rw")) {
            for (int pos = 0; pos < rafp.length(); pos += RECORD_SIZE_PLAYLIST) {
                rafp.seek(pos);
                //int id = raf.readInt();
                String playlist = rafp.readLine();

                if (playlist.equals(playlistName)) {
                    rafp.seek(pos);
                    return getOnePlaylist(rafp);
                }
            }
            return null;
        }
    }

    public Song getBySong(String songName) throws IOException {
        try (RandomAccessFile rafs = new RandomAccessFile(new File("songlist.txt"), "rw")) {
            for (int pos = 0; pos < rafs.length(); pos += RECORD_SIZE_SONGLIST) {
                rafs.seek(pos);
                String song = rafs.readLine();

                if (songName.equals(song)) {
                    rafs.seek(pos);
                    return getOneSong(rafs);
                }
            }
            return null;
        }
    }

    public void deleteByPlaylist(String playlistName) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File("playlists.txt"), "rw")) {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE_PLAYLIST) {
                raf.seek(pos);
                String playlist = raf.readLine();
                if (playlist.equals(playlistName)) {
                    raf.seek(pos);
                    raf.write(new byte[RECORD_SIZE_PLAYLIST]); // write as many blank bytes as one record
                }
            }
        }
    }
    
    public void deleteBySong(String songName) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File("songlist.txt"), "rw")) {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE_SONGLIST) {
                raf.seek(pos);
                String song = raf.readLine();
                if (songName.equals(song)) {
                    raf.seek(pos);
                    raf.write(new byte[RECORD_SIZE_SONGLIST]); // write as many blank bytes as one record
                }
            }
        }
    }

}
