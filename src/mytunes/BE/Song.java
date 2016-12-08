package mytunes.BE;

import mytunes.BLL.SongManager;

/**
 *
 * @author jeppe
 */
public class Song {

    ///also need to implement duartion
    private String songTitle;
    private String songArtist;
    private String songCategory;
    private Long songDuration;
    private String songPath;
    private String readableDuration;

    SongManager songManager = SongManager.getInstance();

    public Song(String songTitle, String songArtist, String songCategory, Long songDuration, String songPath) {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songCategory = songCategory;//need to work on this, may have to set an enum file?
        this.songDuration = songDuration; //need to work on this
        this.songPath = songPath;
        readableDuration = readableDuration(songDuration);
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

    public Long getSongDuration() {
        return songDuration;
    }

    public String readableDuration(Long songDuration) {
//        String time;
//        if (songDuration != null) {
//            time = songManager.calcDuration(songDuration);
//        } else {
//            time = "";
//        }
//        return time;
        String duration;
        if (songDuration != null) {
            int mili = (int) (songDuration / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;
            duration = min + "min" + " " + sec + "sec";
        } else {
            duration = "";
        }

        return duration;

    }

    public void setSongDuration(Long songDuration) {
        this.songDuration = songDuration;
    }

    public String getAllSongStringInfo() {
        return songArtist + " " + songTitle;
    }

}
