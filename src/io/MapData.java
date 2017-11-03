/**
 *
 * @author Colton
 */

package io;

import entities.*;

import java.util.ArrayList;
import java.util.Scanner;

import static entities.Path.Direction.*;
import static entities.Tile.TileType.PATH;
import static entities.Tile.TileType.TILE;
import static entities.Tile.TileType.TOWER;

public class MapData
{
    private Tile[][] tileMat;
    private int numRows;
    private int numCols;
    private Path pathHead;
    private int numEnemies = 10;
    private int spawnFrequency = 10;
    private int health = 100;
    private int money = 600;
    private boolean healthChanged = false;
    private boolean moneyChanged = false;

    public MapData(Scanner in1, Scanner in2)
    {
        buildTileMat(buildStringMat(in1));

        if (in2 != null)
        {
            getGameData(in2);
        }
    }

    public Tile getTile(int y, int x) {return tileMat[y][x];}
    public void setTile(Tile newTile, int y, int x)
    {
        Tile oldTile = getTile(y, x);
        Tile.TileType oldType = oldTile.getTileType();
        Tile.TileType newType = newTile.getTileType();

        //Can place tower on empty square (buying) or empty square on tower (selling)
        if (newType != PATH && oldType != PATH &&
                ((newType == TOWER && oldType == TILE) ||
                        (newType == TILE && oldType == TOWER)))
        {
            tileMat[y][x] = newTile;
        }
    }

    public Path getHead() {return pathHead;}
    public int getNumRows() {return numRows;}
    public int getNumCols() {return numCols;}
    public int getNumEnemies() {return numEnemies;}
    public int getSpawnFrequency(){return spawnFrequency;}
    public int getHealth() {return health;}

    public void decrementHealth()
    {
        health--;
        healthChanged = true;
    }

    public boolean getHealthChanged()
    {
        if (!healthChanged)
        {
            return false;
        }
        else
        {
            healthChanged = false;
            return true;
        }
    }

    public int getMoney()
    {
        return money;
    }

    public void decrementMoney(int x)
    {
        money -= x;
        moneyChanged = true;
    }

    public void incrementMoney(int x)
    {
        money += x;
        moneyChanged = true;
    }

    //Copy paste of getHealthChanged
    //Has the same functionality
    public boolean getMoneyChanged()
    {
        if (!moneyChanged)
        {
            return false;
        }
        else
        {
            moneyChanged = false;
            return true;
        }
    }

    private void getGameData(Scanner in)
    {
        try
        {
            numEnemies = Integer.parseInt(in.nextLine().split("\\s+")[0]);
            spawnFrequency = Integer.parseInt(in.nextLine().split("\\s+")[0]);
            health = Integer.parseInt(in.nextLine().split("\\s+")[0]);
            money = Integer.parseInt(in.nextLine().split("\\s+")[0]);

            if (numEnemies < 1 || spawnFrequency < 1 || health < 1 || money < 1)
            {
                throw new IllegalArgumentException("All values must be greater than 0!");
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Improper format of game data!");
        }
    }

    private String[][] buildStringMat(Scanner in)
    {
        ArrayList<String[]> lines = new ArrayList<String[]>();
        String[][] stringMat;

        //Make arraylist
        while (in.hasNextLine())
        {
            lines.add(in.nextLine().split("\\s+"));
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
        stringMat = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++)
        {
            stringMat[i] = lines.get(i);
        }

        //Check for valid symbols and number of starts and ends
        int starts = 0;
        int ends = 0;
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                if(stringMat[i][j].length() == 1)
                {
                    if (stringMat[i][j].equals("E"))
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
                else if(stringMat[i][j].length() == 2)
                {
                    if(stringMat[i][j].charAt(0) == 'S' &&
                            (stringMat[i][j].charAt(1) == 'U' || stringMat[i][j].charAt(1) == 'R' ||
                                    stringMat[i][j].charAt(1) == 'D' || stringMat[i][j].charAt(1) == 'L'))
                    {
                        if(starts == 0)
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
                else if(stringMat[i][j].length() == 3)
                {
                    if(stringMat[i][j].charAt(0) != 'T')
                    {
                        throw new IllegalArgumentException("Invalid format at (" + i + ", " + j + ")!");
                    }
                }
                else if(stringMat[i][j].length() == 4)
                {
                    if(stringMat[i][j].charAt(0) != 'T' || !(stringMat[i][j].charAt(3) == 'U' ||
                            stringMat[i][j].charAt(3) == 'R' || stringMat[i][j].charAt(3) == 'D' ||
                            stringMat[i][j].charAt(3) == 'L'))
                    {
                        throw new IllegalArgumentException("Invalid format at (" + i + ", " + j + ")!");
                    }
                }
            }
        }

        return stringMat;
    }

    private void buildTileMat(String[][] stringMat)
    {
        //Conceptually an array has a[y][x]
        numRows = stringMat.length;
        numCols = stringMat[0].length;
        tileMat = new Tile[numRows][numCols];

        //Find start then build path
        boolean pathBuilt = false;
        for (int i = 0; i < numRows; i++)
        {
            if (!pathBuilt)
            {
                for (int j = 0; j < numCols; j++)
                {
                    if(stringMat[i][j].charAt(0) == 'S')
                    {
                        connectPath(stringMat, i, j);
                        pathBuilt = true;
                        break;
                    }
                }
            }
            else
            {
                break;
            }
        }
        if (pathHead == null)
        {
            throw new IllegalArgumentException("Illegal path!");
        }

        //Fill the rest
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {
                if (tileMat[i][j] == null)
                {
                    if (stringMat[i][j].charAt(0) == 'T')
                    {
                        tileMat[i][j] = new Tower(i, j, stringMat[i][j].substring(1, 3));
                    }
                    else
                    {
                        tileMat[i][j] = new Tile(i, j);
                    }
                }
            }
        }
    }

    private Path connectPath(String[][] textGrid, int y, int x) throws IllegalArgumentException
    {
        if (y >= textGrid.length || y < 0 || x >= textGrid[0].length || x < 0)
        {
            throw new IllegalArgumentException("Illegal path!");
        }
        else
        {
            Path path;
            char c = textGrid[y][x].charAt(0);
            if (c == 'E')
            {
                path = new Base(y, x);
            }
            else
            {
                boolean start = false;
                if (c == 'S')
                {
                    c = textGrid[y][x].charAt(1);
                    start = true;
                }

                switch (c)
                {
                    case 'U':
                    {
                        path = new Path(y, x, UP, connectPath(textGrid, y - 1, x));
                        break;
                    }
                    case 'R':
                    {
                        path = new Path(y, x, RIGHT, connectPath(textGrid, y, x + 1));
                        break;
                    }
                    case 'D':
                    {
                        path = new Path(y, x, DOWN, connectPath(textGrid, y + 1, x));
                        break;
                    }
                    case 'L':
                    {
                        path = new Path(y, x, LEFT, connectPath(textGrid, y, x - 1));
                        break;
                    }
                    default:
                    {
                        throw new IllegalArgumentException("Illegal path!");
                    }
                }

                if (start)
                {
                    pathHead = path;
                }
            }

            tileMat[y][x] = path;
            return path;
        }
    }
}
