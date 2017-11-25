/**
 * This class represents the Base path, only has coords
 */

package entities;

public class Base extends Path
{
    public Base(int y, int x)
    {
        super(y, x, null, null);
    }

    public TileType getTileType(){return TileType.BASE;}
}
