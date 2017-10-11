package entities;

public class Map
{
    private enum TileType
    {
        TILE, PATH, TOWER;
    }

    private Tile[][] myGrid;
    private int numRows;
    private int numCols;
    private Path pathHead;

    public Map(String[][] textGrid) throws IllegalArgumentException
    {
        //Conceptually an array has a[y][x]
        numRows = textGrid.length;
        numCols = textGrid[0].length;
        myGrid = new Tile[numRows][numCols];

        //Find start then build path
        boolean pathBuilt = false;
        for (int i = 0; i < numRows; i++)
        {
            if (!pathBuilt)
            {
                for (int j = 0; j < numCols; j++)
                {
                    if(textGrid[i][j].charAt(0) == 'S')
                    {
                        connectPath(textGrid, i, j);
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
                if (myGrid[i][j] == null)
                {
                    if (textGrid[i][j].charAt(0) == 'T')
                    {
                        myGrid[i][j] = new Tower(i, j, textGrid[i][j].substring(1, 3));
                    }
                    else
                    {
                        myGrid[i][j] = new Tile(i, j);
                    }
                }
            }
        }
    }

    public Tile getTile(int y, int x)
    {
        return myGrid[y][x];
    }

    public void setTile(Tile newTile, int y, int x)
    {
        TileType oldType;
        TileType newType;
        Tile oldTile = getTile(y, x);

        if (oldTile instanceof Path)
        {
            oldType = TileType.PATH;
        }
        else if (oldTile instanceof Tower)
        {
            oldType = TileType.TOWER;
        }
        else
        {
            oldType = TileType.TILE;
        }

        if (newTile instanceof Path)
        {
            newType = TileType.PATH;
        }
        else if (newTile instanceof Tower)
        {
            newType = TileType.TOWER;
        }
        else
        {
            newType = TileType.TILE;
        }

        //Can place tower on empty square (buying) or empty square on tower (selling)
        if (newType != TileType.PATH && oldType != TileType.PATH &&
                ((newType == TileType.TOWER && oldType == TileType.TILE) ||
                        (newType == TileType.TILE && oldType == TileType.TOWER)))
        {
            myGrid[y][x] = newTile;
        }
    }

    public Path getHead()
    {
        return pathHead;
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
            switch (textGrid[y][x].charAt(0))
            {
                case 'S':
                {
                    switch (textGrid[y][x].charAt(1))
                    {
                        case 'U':
                        {
                            path = new Path(y, x, connectPath(textGrid, y - 1, x));
                            break;
                        }
                        case 'R':
                        {
                            path = new Path(y, x, connectPath(textGrid, y, x + 1));
                            break;
                        }
                        case 'D':
                        {
                            path = new Path(y, x, connectPath(textGrid, y + 1, x));
                            break;
                        }
                        default:
                        {
                            path = new Path(y, x, connectPath(textGrid, y, x - 1));
                            break;
                        }
                    }
                    break;
                }
                case 'U':
                {
                    path = new Path(y, x, connectPath(textGrid, y - 1, x));
                    break;
                }
                case 'R':
                {
                    path = new Path(y, x, connectPath(textGrid, y, x + 1));
                    break;
                }
                case 'D':
                {
                    path = new Path(y, x, connectPath(textGrid, y + 1, x));
                    break;
                }
                case 'L':
                {
                    path = new Path(y, x, connectPath(textGrid, y, x - 1));
                    break;
                }
                case 'E':
                {
                    path = new Path(y, x, null);
                    break;
                }
                default:
                {
                    throw new IllegalArgumentException("Illegal path!");
                }
            }
            myGrid[y][x] = path;
            return path;
        }
    }
}
