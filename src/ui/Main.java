/**
 * This class is simply used for running the game.
 *
 * @author Dylan & Raymond & Colton
 */

package ui;

import io.*;
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
        MapData mapData = new MapData(DataInput.getIn1(), DataInput.getIn2());

        MainMenu mainMenu = new MainMenu(mapData);
    }
}



