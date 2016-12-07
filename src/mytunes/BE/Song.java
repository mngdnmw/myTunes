package mytunes.BE;

/**
 *
 * @author jeppe
 */
public class Song
{
    ///also need to implement duartion
    private String songTitle;
    private String songArtist;
    private String songCategory;
    private int songDuration;
     private String songPath;

    public Song(String songTitle, String songArtist, String songCategory,String songPath)
    {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songCategory = songCategory;//need to work on this, may have to set an enum file?
        songDuration = getSongDuration(); //need to work on this
        this.songPath = songPath;
    }
    

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songCategory) {
        this.songCategory = songCategory;
    }

    public int getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }
    
   
}
