/**
 * This is a base class for defining the x & y
 * coordinates of locations on the map.
 *
 * @author Dylan
 */

package entities;

public class Tile
{
    public enum TileType
    {
        TILE, PATH, TOWER, BASE
    }

    private int y;
    private int x;
    
    public Tile(int y, int x)
    {
        this.y = y;
        this.x = x;
    }

    public int getY()
    {
        return y;
    }
    public int getX()
    {
        return x;
    }
    public TileType getTileType()
    {
        return TileType.TILE;
    }
}

