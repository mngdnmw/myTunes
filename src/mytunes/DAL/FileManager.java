package mytunes.DAL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mytunes.BE.Playlist;
import mytunes.BE.Song;

public class FileManager
{

    //Final variables for songlist.txt
    private static final int SONG_ID_SIZE = Integer.BYTES;
    private static final int SONG_TITLE_SIZE = 50;
    private static final int SONG_ARTIST_SIZE = 50;
    private static final int SONG_CATEGORY_SIZE = 20;
    private static final int SONG_DURATION_SIZE = 8;
    private static final int SONG_PATH_SIZE = 400;
    private static final int RECORD_SIZE_SONGLIST
            = SONG_ID_SIZE
            + SONG_TITLE_SIZE
            + SONG_ARTIST_SIZE
            + SONG_CATEGORY_SIZE
            + SONG_DURATION_SIZE
            + SONG_PATH_SIZE;

    //Final variables for playlistlist.txt
    private static final int PLAYLIST_ID_SIZE = Integer.BYTES;
    private static final int PLAYLIST_NAME_SIZE = 100;
    private static final int RECORD_SIZE_PLAYLIST = PLAYLIST_ID_SIZE + PLAYLIST_NAME_SIZE;

    //Final variables for songRelations.txt
    private static final int RECORD_SIZE_RELATIONS = PLAYLIST_ID_SIZE + SONG_TITLE_SIZE;

    private final String rw = "rw";
    private final String songlistPath = "songlist.txt";
    private final String playlistPath = "playlists.txt";

    private static int songID = 0;
    private static int playlistID = 0;

    private static FileManager instance;

    public static FileManager getInstance()
    {
        if (instance == null)
        {
            instance = new FileManager();
        }

        return instance;

    }

    private FileManager()
    {
    }

    public void saveSong(String songTitle, String songArtist, String songCategory, Long songDuration, String songPath)
    {
        Song song = new Song(songTitle, songArtist, songCategory, songDuration, songPath);
        try
        {
            addSong(song);
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSong(Song s) throws IOException
    {
        try (RandomAccessFile rafs = new RandomAccessFile(new File(songlistPath), rw))
        {
            //place the pointer where there are blank spaces (still working on****)
//            Song song = getOneSong(rafs);
//                
//                if(song!=null){
//                    listOfSongs.add(song);
//                }

            //place the file pointer at the end of the file
            rafs.seek(rafs.length());

            //writing song info into songlist.txt
            rafs.writeInt(s.getId());
            //raf.writeBytes(String.format("%-" + SONG_ID_SIZE + "s", s.getId()).substring(0, SONG_ID_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_TITLE_SIZE + "s", s.getSongTitle()).substring(0, SONG_TITLE_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_ARTIST_SIZE + "s", s.getSongArtist()).substring(0, SONG_ARTIST_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_CATEGORY_SIZE + "s", s.getSongCategory()).substring(0, SONG_CATEGORY_SIZE));
            rafs.writeLong(s.getSongDuration());
            //raf.writeBytes(String.format("%-" + SONG_DURATION_SIZE + "s", s.getSongDuration()).substring(0, SONG_DURATION_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_PATH_SIZE + "s", s.getSongPath()).substring(0, SONG_PATH_SIZE));

        }
    }

    private Song getOneSong(final RandomAccessFile rafs) throws IOException
    {

        //creates empty byte arrays corresponding to the size of each song property 
        //byte[] id = new byte[SONG_ID_SIZE];
        byte[] title = new byte[SONG_TITLE_SIZE];
        byte[] artist = new byte[SONG_ARTIST_SIZE];
        byte[] category = new byte[SONG_CATEGORY_SIZE];
       // byte[] duration = new byte[SONG_DURATION_SIZE];
        byte[] path = new byte[SONG_PATH_SIZE];

        //reads into the byte arrays

        int songId = rafs.readInt();
        rafs.read(title);
        rafs.read(artist);
        rafs.read(category);
        //rafs.read(duration);
        long songDuration = rafs.readLong();
        rafs.read(path);

        //type casts the byte arrays into strings

        //int songId = id;
        String songTitle = new String(title).trim();
        String songArtist = new String(artist).trim();
        String songCategory = new String(category).trim();
       // Long songDuration = bytesToLong(duration);
        String songPath = new String(path).trim();
        
        if (songId ==-1){
            return null;
        }
        
        return new Song(songId, songTitle, songArtist, songCategory, songDuration, songPath);
        
    }

    private static long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }

    public List<Song> getAllSongs() throws IOException
    {

        try (RandomAccessFile rafs = new RandomAccessFile(new File(songlistPath), rw))
        {

            //makes an arraylist to store the songs 
            List<Song> listOfSongs = new ArrayList<>();

            while (rafs.getFilePointer() < rafs.length()){
            
                Song song = getOneSong(rafs);
                
                if(song!=null){
                    listOfSongs.add(song);
                }
            }
            return listOfSongs;
        }

    }

    public Song getBySong(String songTitle) throws IOException
    {

        try (RandomAccessFile rafs = new RandomAccessFile(new File(songlistPath), rw))
        {
            for (int pos = 0; pos < rafs.length(); pos += RECORD_SIZE_SONGLIST)
            {

                //starts reading at the beginning of every song record
                rafs.seek(pos);
                String song = rafs.readLine();

                if (songTitle.equals(song))
                {
                    rafs.seek(pos);
                    return getOneSong(rafs);
                }
            }
            return null;
        }
    }

    public void savePlaylist(String playlistName)
    {
        Playlist playlist = new Playlist(playlistName);
        try
        {
            addPlaylist(playlist);
        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPlaylist(Playlist p) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(playlistPath), rw))
        {
            raf.seek(raf.length());  // place the file pointer at the end of the file.
            raf.writeInt(p.getId());
            raf.writeBytes(String.format("%-" + PLAYLIST_NAME_SIZE + "s", p.getName()).substring(0, PLAYLIST_NAME_SIZE));
        }
    }

    public List<Playlist> getAll() throws IOException
    {
        try (RandomAccessFile rafp = new RandomAccessFile(new File(playlistPath), rw))
        {

            List<Playlist> playlists = new ArrayList<>();

            while (rafp.getFilePointer() < rafp.length())
            {
                playlists.add(getOnePlaylist(rafp));
            }
            return playlists;
        }
    }

    private Playlist getOnePlaylist(final RandomAccessFile raf) throws IOException
    {
        byte[] bytes = new byte[PLAYLIST_NAME_SIZE];

        int id = raf.readInt();
        raf.read(bytes);
        String playlistName = new String(bytes).trim();
        return new Playlist(playlistName);
    }

    public Playlist getByPlaylist(String playlistName) throws IOException
    {
        try (RandomAccessFile rafp = new RandomAccessFile(new File(playlistPath), rw))
        {
            for (int pos = 0; pos < rafp.length(); pos += RECORD_SIZE_PLAYLIST)
            {
                rafp.seek(pos);
                //int id = raf.readInt();
                String playlist = rafp.readLine();

                if (playlist.equals(playlistName))
                {
                    rafp.seek(pos);
                    return getOnePlaylist(rafp);
                }
            }
            return null;
        }
    }

    public void deleteByPlaylist(int id) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(playlistPath), rw))
        {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE_PLAYLIST)
            {
                raf.seek(pos);
                int currentId = raf.readInt();
                if (currentId == id)
                {
                    raf.seek(pos);
                    raf.write(new byte[RECORD_SIZE_PLAYLIST]); // write as many blank bytes as one record
                }
            }
        }
    }

    public void deleteBySong(int id) throws IOException
    {
        try (RandomAccessFile raf = new RandomAccessFile(new File(songlistPath), rw))
        {
            for (int pos = 0; pos < raf.length(); pos += RECORD_SIZE_SONGLIST)
            {
                raf.seek(pos);
                int currentId = raf.readInt();
                if (currentId == id)
                {
                    raf.seek(pos);
                    Integer nullId = -1 ;
                    raf.writeInt(nullId);
                    raf.write(new byte[RECORD_SIZE_SONGLIST-4]); // write as many blank bytes as one record
                }
            }
        }
    }

    public int readID(String type)
    {
        String mode = "";
        int recordSize = 0;
        int ID = 0;
        try
        {

            if (type.equals("Song"))
            {
                mode = songlistPath;
                recordSize = RECORD_SIZE_SONGLIST;
            } else if (type.equals("Playlist"))
            {
                mode = playlistPath;
                recordSize = RECORD_SIZE_PLAYLIST;
            }

            RandomAccessFile raf = new RandomAccessFile(new File(mode), rw);

            try
            {
                if (raf.length() >= recordSize)
                {
                    raf.seek(0);
                    try
                    {
                        ID = raf.readInt();
                    } catch (IOException ex)
                    {
                        Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "1", ex);
                    }
                } else
                {
                    ID = 0;
                }
            } catch (IOException ex)
            {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "2", ex);
            }

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "3", ex);
        }
        return ID;
    }

}
