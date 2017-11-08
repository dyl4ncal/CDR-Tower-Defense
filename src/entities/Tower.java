/**
 *
 * @author Colton
 */

package entities;

public class Tower extends Tile
{
    private enum TowerType
    {
        BASIC, SNIPER, MELEE, SPLASH
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
        //The speed may have to change relative to the clock/ticker
        tower = type;
        switch (type)
        {
            //Hits from a fair distance
            //Low amount of damage
            //Low attack speed
            //Cost is low
            case "Basic":
            {
                myType = TowerType.BASIC;
                cost = 150;
                range = 3;
                attack = 2;
                speed = 3;
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
                speed = 5;
                break;
            }

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
                speed = 10;
                break;
            }

            case "Splash":
            {
                myType = TowerType.SPLASH;
                cost = 300;
                //Tile radius
                range = 1;
                attack = 3;
                speed = 7;
                break;
            }

            //Default is the basic tower type
            default:
            {
                myType = TowerType.BASIC;
                cost = 150;
                range = 2;
                attack = 2;
                speed = 3;
                break;
            }
        }
    }

    @Override
    public TileType getTileType() 
    {
        return TileType.TOWER;
    }

    //Upgrading a tower increases its attack by 1 or 2 (splash is 1, rest are 2)
    public void incrementAttack(int a)
    {

    }

    public int getCost() {
        return cost;
    }

    public int getRange()
    {
        return range;
    }

    public int getAttack()
    {
        return attack;
    }

    public int getSpeed()
    {
        return speed;
    }

    public String getTowerType()
    {
        return tower;
    }
}

