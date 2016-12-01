/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;


/**
 *
 * @author jeppe
 */
public class Song
{
    private final String songName;
    private final String songPath;
    private final String songArtist;
    
    public Song(String songName, String songPath, String songArtist)
    {
        this.songName = songName;
        this.songPath = songPath;
        this.songArtist = songArtist;
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
