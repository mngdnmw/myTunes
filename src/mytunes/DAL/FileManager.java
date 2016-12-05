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
public class FileManager
{

    //private String playlist;
    private static final int PLAYLIST_SIZE = 50;
    private static final int SONG_SIZE = 50;
    private static final int RECORD_SIZE = PLAYLIST_SIZE + SONG_SIZE;

    private List<Song> songList = new ArrayList();
    private List<Playlist> playlistList = new ArrayList();
    private final String filename = "playlist.txt";


//    public FileManager(String playlistName) {
//
//        this.playlistName = playlistName;
//    }
    public List<Song> getAllSongs()
    {
        if (songList.isEmpty())
        {
            return null;
        } else
        {
            return songList;
        }
    }
    

//    public List<Playlist> getAllPlaylists() {
//        if (playlistList.isEmpty()) {
//            return null;
//        } else {
//            return playlistList;
//        }
//    }
    public void savePlaylist(String playlistName)
    {
//
//        try (BufferedWriter bw
//                = new BufferedWriter(new FileWriter(playlistName + ".txt"))) {
//            bw.write(playlistName);
//        } catch (IOException ex) {
//            Logger.getLogger(PlaylistViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Playlist playlist = new Playlist(playlistName);
        try
        {
            //        playlistList.add(playlist);
            addPlaylist(playlist);
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPlaylist(Playlist p) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(filename), "rw"))
        {
            raf.seek(raf.length());  // place the file pointer at the end of the file.
            raf.writeInt(p.getID());
            raf.writeBytes(String.format("%-" + PLAYLIST_SIZE + "s", p.getName()).substring(0, PLAYLIST_SIZE));
        }
    }

    public List<Playlist> getAll() throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(filename), "rw"))
        {
            List<Playlist> playlists = new ArrayList<>();

            while (raf.getFilePointer() < raf.length())
            {
                playlists.add(getOnePlaylist(raf));
            }
            return playlists;
        }
    }

    private Playlist getOnePlaylist(final RandomAccessFile raf) throws IOException
    {
        byte[] bytes = new byte[PLAYLIST_SIZE];
        int id = raf.readInt();
        raf.read(bytes);
        String playlistName = new String(bytes).trim();
        return new Playlist(playlistName);
    }

    public Playlist getByPlaylist(int playlistID) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(filename), "rw"))
        {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE)
            {
                raf.seek(pos);
                int id = raf.readInt();
                //String playlist = raf.readLine();

                if (id == playlistID)
                {
                    raf.seek(pos);
                    return getOnePlaylist(raf);
                }
            }
            return null;
        }
    }

    public void deleteByPlaylist(int id) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(filename), "rw"))
        {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE)
            {
                raf.seek(pos);
                //String playlist = raf.readLine();
                //if (playlist.equals(playlistName))
                    int currentId = raf.readInt();
                if (currentId == id)    
                {
                    raf.seek(pos);
                    raf.write(new byte[RECORD_SIZE]); // write as many blank bytes as one record
                }
            }
        }
    }

    public void addSong(String songName, String songPath, String songArtist)
    {
        Song s = new Song(songName, songPath, songArtist);
        songList.add(s);
    }
}
