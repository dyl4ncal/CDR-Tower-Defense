/**
 *
 * @author Colton
 */

package entities;

import graphics.EnemyComponent;
import java.awt.Color;

import java.util.ArrayList;

public class Tower extends Tile
{
    public enum TowerType
    {
        BASIC, SNIPER, MELEE, SPLASH
    }

    private TowerType myType;
    private Color myColor;
    private int cost;
    private int attackTicker = 1;
    private int range;
    private int attack;
    private int speed;
    private int sellValue;
    private int upgradeLevel = 1;

    public Tower(String type, int y, int x)
    {
        super(y, x);

        //Set the attributes of the tower
        //The speed may have to change relative to the clock/ticker
        switch (type)
        {
            //Hits from a fair distance
            //Low amount of damage
            //Low attack speed
            //Cost is low
            case "Basic":
            {
                myType = TowerType.BASIC;
                myColor = Color.YELLOW;
                cost = 150;
                attack = 4;
                range = 3;
                speed = 4;
                break;
            }

            //Short range
            //High attack
            //Moderate attack speed
            //Cost is moderate
            case "Melee":
            {
                myType = TowerType.MELEE;
                myColor = Color.RED;
                cost = 200;
                attack = 8;
                range = 1;
                speed = 5;
                break;
            }

            //Highest range
            //Moderate damage
            //Slow attack speed 
            //Cost is moderately high
            case "Sniper":
            {
                myType = TowerType.SNIPER;
                myColor = new Color(128, 255, 0);
                cost = 250;
                attack = 6;
                range = 5;
                speed = 6;
                break;
            }

            //Low range
            //Low damage
            //Slow attack speed
            //Hits all in its radius
            //Cost is high
            case "Splash":
            {
                myType = TowerType.SPLASH;
                myColor = Color.CYAN;
                cost = 300;
                attack = 3;
                range = 1;
                speed = 10;
                break;
            }

            //Default is the basic tower type
            default:
            {
                myType = TowerType.BASIC;
                myColor = Color.YELLOW;
                cost = 150;
                attack = 4;
                range = 3;
                speed = 4;
                break;
            }
        }
        sellValue = cost;
    }

    @Override
    public TileType getTileType() {return TileType.TOWER;}
    public TowerType getTowerType() {return myType;}
    public int getCost() {return cost;}
    public int getUpgradeLevel(){return upgradeLevel;}
    public int getUpgradeCost(){return cost * upgradeLevel;}
    public Color getColor(){return myColor;}

    //For the Select method
    public int getAttack(){return attack;}
    public int getRange(){return range;}
    public int getSpeed(){return speed;}
    public int getSellValue(){return sellValue/4;}

    //Upgrading a tower increases its attack by 1 or 2 (splash is 1, rest are 2)
    public void upgrade()
    {
        switch(myType)
        {
            case BASIC:
            {
                Color[] colorArray = {Color.ORANGE, new Color(255, 142, 32), new Color(255, 77, 0)};
                myColor = colorArray[upgradeLevel - 1]; //new Color(255, 50, 0);
                attack += 3;
                sellValue += cost * upgradeLevel;
                                  //Red, Green, Blue
                //name = new Color{255, 255, 255}
                break;
            }

            case MELEE:
            {
                Color[] colorArray = {new Color(255, 0, 80), new Color(204, 0, 102), new Color(153, 0, 153)};
                myColor = colorArray[upgradeLevel - 1];
                attack += 6;
                sellValue += cost * upgradeLevel;
                break;
            }

            case SNIPER:
            {
                Color[] colorArray = {Color.GREEN, new Color(0, 204, 0), new Color(0, 132, 0)};
                myColor = colorArray[upgradeLevel - 1];
                //new Color(0, 175, 0);
                attack += 4;
                sellValue += cost * upgradeLevel;
                break;
            }

            case SPLASH:
            {
                Color[] colorArray = {new Color(0, 163, 163), new Color(0, 102, 204), Color.BLUE};
                myColor = colorArray[upgradeLevel - 1];
                attack += 2;
                sellValue += cost * upgradeLevel;
                break;
            }

            default:
            {
                myColor = Color.ORANGE;
                attack += 1;
                sellValue += cost * upgradeLevel;
                break;
            }
        }
        upgradeLevel++;
    }

    public int attack(ArrayList<EnemyComponent> enemyList)
    {
        int loot = 0;
        if (attackTicker == speed)
        {
            attackTicker = 1;
            for (int i = 0; i < enemyList.size(); i++)
            {
                EnemyComponent e = enemyList.get(i);
                if (e.getX() >= getX() - range && e.getX() <= getX() + range &&
                        e.getY() >= getY() - range && e.getY() <= getY() + range)
                {
                    e.decrementHealth(attack);
                    if (!e.isAlive())
                    {
                        loot += 8;
                        enemyList.remove(i);
                        i--;
                    }
                    //Splash towers attack everything in the list
                    if (myType != TowerType.SPLASH)
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            attackTicker++;
        }

        return loot;
    }
}

