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
    private static final int idSize = Integer.BYTES;

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

        int nextId;
        try (RandomAccessFile rafs = new RandomAccessFile(new File(songlistPath), rw))
        {
            //place the file pointer at the end of the file
            if (rafs.length() == 0)
            {
                rafs.writeInt(0);
                rafs.seek(0);
            }
            nextId = rafs.readInt(); //header
            //rafs.seek(rafs.length() - rafs.length());
            rafs.seek(0);
            rafs.writeInt(nextId + 1); //+1 on our nextid in our header

            rafs.seek(getFirstAvailPointer("Song"));

            //writing song info into songlist.txt
            rafs.writeInt(nextId); // id for song
            rafs.writeBytes(String.format("%-" + SONG_TITLE_SIZE + "s", songTitle).substring(0, SONG_TITLE_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_ARTIST_SIZE + "s", songArtist).substring(0, SONG_ARTIST_SIZE));
            rafs.writeBytes(String.format("%-" + SONG_CATEGORY_SIZE + "s", songCategory).substring(0, SONG_CATEGORY_SIZE));
            rafs.writeLong(songDuration);
            rafs.writeBytes(String.format("%-" + SONG_PATH_SIZE + "s", songPath).substring(0, SONG_PATH_SIZE));

        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "Uh-oh spaghettio-os!", ex);
        }
    }

    private Song getOneSong(final RandomAccessFile rafs) throws IOException
    {

        //creates empty byte arrays corresponding to the size of each song property 
        byte[] title = new byte[SONG_TITLE_SIZE];
        byte[] artist = new byte[SONG_ARTIST_SIZE];
        byte[] category = new byte[SONG_CATEGORY_SIZE];
        byte[] path = new byte[SONG_PATH_SIZE];

        //reads into the byte arrays
        if (rafs.getFilePointer() == 0)
        {
            rafs.seek(idSize);
        }
        int songId = rafs.readInt();

        rafs.read(title);
        String songTitle = new String(title).trim();

        rafs.read(artist);
        String songArtist = new String(artist).trim();

        rafs.read(category);
        String songCategory = new String(category).trim();

        long songDuration = rafs.readLong();

        rafs.read(path);
        String songPath = new String(path).trim();

        if (songId == -1)
        {
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

            while (rafs.getFilePointer() < rafs.length())
            {

                Song song = getOneSong(rafs);

                if (song != null)
                {
                    listOfSongs.add(song);
                }
            }
            return listOfSongs;
        }

    }

    public void savePlaylist(String playlistName)
    {
        int nextId;

        try (RandomAccessFile rafp = new RandomAccessFile(new File(playlistPath), rw))
        {
            if (rafp.length() == 0)
            {
                rafp.writeInt(1);
                rafp.seek(0);
            }

            nextId = rafp.readInt(); //header
            rafp.seek(0);
            rafp.writeInt(nextId + 1); //+1 on our nextid in our header

            rafp.seek(getFirstAvailPointer("Playlist"));
            rafp.seek(rafp.length());  // place the file pointer at the end of the file.
            rafp.writeInt(nextId);
            rafp.writeBytes(String.format("%-" + PLAYLIST_NAME_SIZE + "s", playlistName).substring(0, PLAYLIST_NAME_SIZE));

        } catch (IOException ex)
        {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Playlist> getAllPlaylists() throws IOException
    {
        try (RandomAccessFile rafp = new RandomAccessFile(new File(playlistPath), rw))
        {

            List<Playlist> listOfPlaylists = new ArrayList<>();

            while (rafp.getFilePointer() < rafp.length())
            {
                Playlist playlist = getOnePlaylist(rafp);

                if (playlist != null)
                {
                    listOfPlaylists.add(playlist);
                }

            }
            return listOfPlaylists;
        }
    }

    private Playlist getOnePlaylist(final RandomAccessFile rafp) throws IOException
    {
        byte[] bytes = new byte[PLAYLIST_NAME_SIZE];
        int playlistId = rafp.readInt();

        rafp.read(bytes);
        String playlistName = new String(bytes).trim();
        if (playlistId == -1)
        {
            return null;
        } else
        {
            return new Playlist(playlistId, playlistName);
        }
    }

    public void deleteByPlaylist(int id) throws IOException
    {
        try (RandomAccessFile rafp = new RandomAccessFile(new File(playlistPath), rw))
        {
            for (int pos = idSize; pos < rafp.length(); pos += RECORD_SIZE_PLAYLIST)
            {
                rafp.seek(pos);
                int currentId = rafp.readInt();
                if (currentId == id)
                {
                    rafp.seek(pos);
                    Integer nullId = -1;
                    rafp.writeInt(nullId);
                    rafp.write(new byte[RECORD_SIZE_PLAYLIST - idSize]); // write as many blank bytes as one record, minus the id.
                }
            }
        }
    }

    public void deleteBySong(int id) throws IOException
    {
        try (RandomAccessFile rafs = new RandomAccessFile(new File(songlistPath), rw))
        {
            for (int pos = idSize; pos < rafs.length(); pos += RECORD_SIZE_SONGLIST)
            {
                rafs.seek(pos);
                int currentId = rafs.readInt();
                if (currentId == id)
                {
                    rafs.seek(pos);
                    Integer nullId = -1;
                    rafs.writeInt(nullId);
                    rafs.write(new byte[RECORD_SIZE_SONGLIST - idSize]); // write as many blank bytes as one record, minus the id.
                }
            }
        }
    }

    public long getFirstAvailPointer(String type) throws FileNotFoundException, IOException
    {
        String path = "";
        int recordSize = 0;

        if (type == "Song")
        {
            path = songlistPath;
            recordSize = RECORD_SIZE_SONGLIST;
        }

        if (type == "Playlist")
        {
            path = playlistPath;
            recordSize = RECORD_SIZE_PLAYLIST;
        }
        try (RandomAccessFile raf = new RandomAccessFile(new File(path), "r"))
        {
            //starts reading at the beginning of the file

            for (long i = idSize; i < raf.length(); i += recordSize)
            {
                raf.seek(i);
                int songId = raf.readInt();
                if (songId == -1)
                {
                    return i;
                }
            }
            return raf.length();
        }

    }

}
