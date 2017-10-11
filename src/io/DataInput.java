package io;

import entities.Map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is for taking an input file as an argument
 * to define the map and also to get any saved game data.
 * 
 * @author Dylan
 */
public class DataInput
{
    private static Scanner in1;
    private static Scanner in2;

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
    
    //Method to read the map data file.
    public static Map readData() throws IOException
    {
        Map map;
        ArrayList<String[]> lines = new ArrayList<String[]>();

        //Make arraylist
        while (in1.hasNextLine())
        {
            lines.add(in1.nextLine().split("\\s+"));
        }

        //Check all rows are same length
        int numRows = lines.size();
        int numCols = lines.get(0).length;
        for (int i = 0; i < numRows; i++)
        {
            if (lines.get(i).length != numCols)
            {
                throw new IllegalArgumentException("Row lengths do not match!");
            }
        }

        //Convert to 2d array
        String[][] textGrid = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++)
        {
            textGrid[i] = lines.get(i);
        }

        //Check for valid symbols and number of starts and ends
        int starts = 0;
        int ends = 0;
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                if (textGrid[i][j].length() == 1)
                {
                    if (textGrid[i][j].equals("E"))
                    {
                        if (ends == 0)
                        {
                            ends++;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Too many end tiles!");
                        }
                    }
                }
                else if (textGrid[i][j].length() == 2)
                {
                    if (textGrid[i][j].charAt(0) == 'S' &&
                            (textGrid[i][j].charAt(1) == 'U' || textGrid[i][j].charAt(1) == 'R' ||
                                    textGrid[i][j].charAt(1) == 'D' || textGrid[i][j].charAt(1) == 'L'))
                    {
                        if (starts == 0)
                        {
                            starts++;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Too many start tiles!");
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("Invalid format at (" + i + ", " + j + ")!");
                    }
                }
                else if (textGrid[i][j].length() == 3)
                {
                    if (textGrid[i][j].charAt(0) != 'T')
                    {
                        throw new IllegalArgumentException("Invalid format at (" + i + ", " + j + ")!");
                    }
                }
                else if (textGrid[i][j].length() == 4)
                {
                    if (textGrid[i][j].charAt(0) != 'T' || !(textGrid[i][j].charAt(3) == 'U' ||
                            textGrid[i][j].charAt(3) == 'R' || textGrid[i][j].charAt(3) == 'D' ||
                            textGrid[i][j].charAt(3) == 'L'))
                    {
                        throw new IllegalArgumentException("Invalid format at (" + i + ", " + j + ")!");
                    }
                }
            }
        }

        return new Map(textGrid);
    }
}
