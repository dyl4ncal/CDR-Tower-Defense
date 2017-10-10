package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is for taking an input file as an argument
 * to define the map and also to get any saved game data.
 * 
 * @author Dylan
 */
public class DataInput
{
    private static FileReader in1;
    private static BufferedReader reader1;
    private static FileReader in2;
    private static BufferedReader reader2;

    public static void getDataFile(String[] args) throws FileNotFoundException, IOException
    {
        switch (args.length)
        {
            case 0:
            {
                System.out.println("Please specify the map file to read (.txt).");
                
                System.exit(0);
            }
                /*If only one argument is supplied, it implies
                no saved game exists.*/
            case 1:
                {
                    File mapFile = new File(args[0]);
                    in1 = new FileReader(mapFile);
                    reader1 = new BufferedReader(in1);
                    break;
                }
                /*If two arguments are supplied, it implies
                the first is a map data file and the second
                is a save game file.*/
            default:
                {
                    File mapFile = new File(args[0]);
                    File saveFile = new File(args[1]);
                    in1 = new FileReader(mapFile);
                    in2 = new FileReader(saveFile);
                    reader1 = new BufferedReader(in1);
                    reader2 = new BufferedReader(in2);
                    break;
                }
        }
    }
    
    //Method to read the map data file.
    public static void readData() throws IOException
    {
        String data = reader1.readLine();
    }
}
