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
    private String songName;
    private String songPath;
    private String songArtist;
    
    public Song(String songName, String songPath)
    {
        this.songName = songName;
        this.songPath = songPath;
    }
    
    public void setArtist(String artist)
    {
        this.songArtist = artist;
    }
}
