/**
 *
 * @author Colton
 */

package entities;

public class Tower extends Tile
{
    private enum TowerType
    {
        BASIC, SNIPER, MELEE
    }

    private TowerType myType;
    private String tower;
    private int cost;
    private int range;
    private int attack;
    private int speed;

    public Tower(int x, int y, String type)
    {
        super(x, y);
        //Set the attributes of the tower
        tower = type;
        switch (type)
        {
            //Highest range
            //Moderate damage
            //Slow attack speed 
            //Cost is high
            case "Sniper":
            {
                myType = TowerType.SNIPER;
                cost = 250;
                //Tile radius
                range = 5;
                attack = 4;
                speed = 7;
                break;
            }

            //Hits from a fair distance
            //Low amount of damage
            //Low attack speed
            //Cost is low
            case "Basic":
            {
                myType = TowerType.BASIC;
                cost = 150;
                range = 2;
                attack = 2;
                speed = 2;
                break;
            }

            //Shortest range
            //Highest attack
            //Moderate attack speed
            //Cost is moderate
            case "Melee":
            {
                myType = TowerType.MELEE;
                cost = 200;
                range = 1;
                attack = 5;
                speed = 4;
                break;
            }

            //Default is the basic tower type
            default:
            {
                myType = TowerType.BASIC;
                cost = 150;
                range = 2;
                attack = 2;
                speed = 2;
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

    public String getTowerType()
    {
        return tower;
    }
}

