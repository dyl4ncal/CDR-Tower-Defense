/**
 * This is a base class for defining the x & y
 * coordinates of locations on the map.
 *
 * @author Dylan
 */

package entities;

public class Tile
{
    private int yCoordinate;
    private int xCoordinate;
    
    public Tile(int y, int x)
    {
        yCoordinate = y;
        xCoordinate = x;
    }

    public int getYCoordinate()
    {
        return yCoordinate;
    }
    public int getXCoordinate()
    {
        return xCoordinate;
    }
}
