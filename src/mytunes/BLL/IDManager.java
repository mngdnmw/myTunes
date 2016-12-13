/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import mytunes.DAL.FileManager;

/**
 *
 * @author jeppe
 */
public class IDManager
{

    private int nextSongID = -1;
    private int nextPlaylistID = -1;

    private FileManager fileManager = FileManager.getInstance();

    private static IDManager instance;

    public static IDManager getInstance()
    {
        if (instance == null)
        {
            instance = new IDManager();
        }

        return instance;

    }

    private IDManager()
    {
        nextPlaylistID = fileManager.readID("Playlist");
        nextSongID = fileManager.readID("Song");
    }

    public int getIDForSong()
    {
        nextSongID++;
        return nextSongID;
    }

    public int getIDForPlaylist()
    {
        nextPlaylistID++;
        return nextPlaylistID;
    }

}
