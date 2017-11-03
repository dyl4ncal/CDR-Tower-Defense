/**
 *
 * @author Colton
 */

package entities;

public class Tower extends Tile
{
    private enum TowerType
    {
        BASIC
    }

    private TowerType myType;
    private int cost;

    public Tower(int x, int y, String type)
    {
        super(x, y);
        switch (type)
        {
            default:
            {
                myType = TowerType.BASIC;
                cost = 200;
                break;
            }
        }
    }

    @Override
    public TileType getTileType() 
    {
        return TileType.TOWER;
    }

    public int getCost() {
        return cost;
    }
}

