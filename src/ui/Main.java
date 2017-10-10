package ui;

import io.DataInput;
import java.io.IOException;

/**
 * This class is simply used for running the game.
 * 
 * @author Dylan
 */
public class Main
{
    public static void main(String[] args) throws IOException
    {
        runGame(args);
    }
    
    public static void runGame(String[] args) throws IOException
    {
        DataInput.getDataFile(args);
        new MainMenu();
    }
}
