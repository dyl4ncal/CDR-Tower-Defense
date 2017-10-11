/**
 * This class is simply used for running the game.
 *
 * @author Dylan
 */

package ui;

import entities.Map;
import io.DataInput;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        runGame(args);
    }
    
    public static void runGame(String[] args) throws IOException
    {
        DataInput.getDataFile(args);
        Map map = DataInput.readData();
        new MainMenu();
    }
}
