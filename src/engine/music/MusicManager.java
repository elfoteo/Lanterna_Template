package engine.music;

import engine.utils.Constants;
import engine.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The MusicManager class manages the music for the game.
 */
public class MusicManager {
    private static MusicPlayer musicPlayer; // The music player instance
    private static List<File> musicFilePaths; // List of music file paths

    /**
     * Registers soundtracks by scanning the sounds directory and initializing the music player.
     * It looks for sound files in the directory specified by Constants.soundsDir.
     *
     * @return returns if the registration has failed or not
     */
    public static boolean register() {
        File soundsDir = new File(Constants.soundsDir); // Get the sounds directory
        if (!soundsDir.exists()) {
            // Attempt to create the directory
            boolean created = soundsDir.mkdirs(); // This will create all necessary directories

            // Check if directory creation was successful
            if (!created) {
                Utils.Debug("Failed to create sounds directory.");
            }
        }
        File[] soundFiles = soundsDir.listFiles(); // List the files in the directory

        // If there are files in the directory, initialize the list of music file paths
        if (soundFiles != null) {
            musicFilePaths = new ArrayList<>();
            for (File soundFile : soundFiles) {
                if (soundFile.isFile()) {
                    musicFilePaths.add(soundFile); // Add each file to the list of music file paths
                }
            }
        }

        // Initialize the MusicPlayer with the first soundtrack, if available
        if (musicFilePaths == null || musicFilePaths.isEmpty()) {
            Utils.Debug("No soundtracks are present in the sounds directory, please add them.");
            return false;
        }

        musicPlayer = new MusicPlayer(musicFilePaths.get(0)); // Initialize the music player
        return true;
    }

    /**
     * Retrieves the list of soundtracks.
     * @return A list of File objects representing the soundtracks.
     */
    public static List<File> getSoundtracks(){
        return musicFilePaths; // Return the list of music file paths
    }

    /**
     * Retrieves the music player.
     * @return The MusicPlayer instance.
     */
    public static MusicPlayer getMusicPlayer() {
        return musicPlayer; // Return the music player instance
    }
}
