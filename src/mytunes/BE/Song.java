package mytunes.BE;

import mytunes.BLL.SongManager;

public class Song
{
    private int songID;
    private String songTitle;
    private String songArtist;
    private String songCategory;

    private Long songDuration;
    private String songPath;
    private String readDuration;

    SongManager songManager = SongManager.getInstance();

    public Song(int songID, String songTitle, String songArtist, String songCategory, Long songDuration, String songPath)
    {

        this.songID = songID;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songCategory = songCategory;
        this.songDuration = songDuration;
        this.songPath = songPath;
        readDuration = readableDuration(this.songDuration);


    }

    public String getReadDuration()
    {
        return readDuration;
    }

    public int getSongId()
    {
        return songID;

    }

    public String getSongTitle()
    {
        return songTitle;
    }

    public void setSongTitle(String songTitle)
    {
        this.songTitle = songTitle;
    }

    public String getSongPath()
    {
        return songPath;
    }

    public void setSongPath(String songPath)
    {
        this.songPath = songPath;
    }

    public String getSongArtist()
    {
        return songArtist;
    }

    public void setSongArtist(String songArtist)
    {
        this.songArtist = songArtist;
    }

    public String getSongCategory()
    {
        return songCategory;
    }

    public void setSongCategory(String songCategory)
    {
        this.songCategory = songCategory;
    }

    public Long getSongDuration()
    {
        return songDuration;
    }

    public String readableDuration(Long songDuration)
    {
        String duration;
        if (songDuration != null)
        {
            int mili = (int) (songDuration / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;

            if (sec < 10)
            {
                duration = min + ":" + "0" + sec;
            } else
            {
                duration = min + ":" + sec;
            }

        } else
        {

            duration = "";
        }

        return duration;

    }

    public void setSongDuration(Long songDuration)
    {
        this.songDuration = songDuration;

    }

    public String getAllSongStringInfo()
    {
        return songArtist + " " + songTitle;
    }

}
