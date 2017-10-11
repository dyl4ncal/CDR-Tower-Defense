/**
 *
 * @author Colton
 */

package entities;

public class Tower extends Tile
{
    private enum TowerType
    {
        BASIC;
    }

    private TowerType myType;

    public Tower(int x, int y, String type)
    {
        super(x, y);
        switch (type)
        {
            default:
            {
                myType = TowerType.BASIC;
                break;
            }
        }
    }
}
