/**
 * This class represents the Base path. It defines its coordinates.
 */

package entities;

public class Base extends Path
{
    public Base(int y, int x)
    {
        super(y, x, null, null);
    }

    @Override
    public TileType getTileType()
    {
        return TileType.BASE;
    }
}
