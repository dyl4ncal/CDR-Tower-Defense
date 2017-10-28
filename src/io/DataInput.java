package io;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is for taking an input file as an argument
 * to define the map and also to get any saved game data.
 * 
 * @author Dylan & Colton
 */
public class DataInput
{
    private static Scanner in1 = null;
    private static Scanner in2 = null;

    public static void getDataFile(String[] args) throws IOException
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
                    in1 = new Scanner(mapFile);
                    break;
                }
                /*If two arguments are supplied, it implies
                the first is a map data file and the second
                is a save game file.*/
            default:
                {
                    File mapFile = new File(args[0]);
                    File saveFile = new File(args[1]);
                    in1 = new Scanner(mapFile);
                    in2 = new Scanner(saveFile);
                    break;
                }
        }
    }

    public static Scanner getIn1() {return in1;}
    public static Scanner getIn2() {return in2;}
}

