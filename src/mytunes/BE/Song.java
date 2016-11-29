/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.StringProperty;

/**
 *
 * @author jeppe
 */
public class Song
{
    private StringProperty songName;
    private StringProperty songPath;
    private StringProperty songArtist;
    
    public Song(StringProperty songName, StringProperty songPath)
    {
        this.songName = songName;
        this.songPath = songPath;
    }
    
    public void setArtist(StringProperty artist)
    {
        this.songArtist = artist;
    }
}
