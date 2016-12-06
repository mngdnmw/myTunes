package mytunes.BE;

/**
 *
 * @author jeppe
 */
public class Song
{
    ///also need to implement duartion
    private final String songName;
    private final String songPath;
    private final String songArtist;
    private final String category;
    private final int duration;

    public Song(String songName, String songPath, String songArtist)
    {
        this.songName = songName;
        this.songPath = songPath;
        this.songArtist = songArtist;
        category = null;//need to work on this, may have to set an enum file?
        duration = 0; //need to work on this
        
    }

    public String getSongName()
    {
        return songName;
    }

    public String getSongPath()
    {
        return songPath;
    }

    public String getSongArtist()
    {
        return songArtist;
    }
}
