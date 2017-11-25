/**
 *
 * @author Colton
 */

package entities;

import graphics.EnemyComponent;
import java.awt.Color;

import java.util.ArrayList;

/**
 * Tower class holds all the information about towers
 * Currently there are 5 tower types, each with different attributes
 * Able to combine 2 towers to make an even greater tower
 * This is my ranking system for the overall best combinations
 * Short description for each
 *
 * Basic tower combinations
 * #7 Basic and Melee   -Kills bosses fairly fast
 * #2 Basic and Splash  -Good for Killing groups of enemies
 * #9 Basic and Sniper  -A sniper tower with increased speed
 * #10 Basic and Speed  -Only gives slights increases to speed and attack
 *
 * Melee tower combinations
 * #3 Melee and Splash  -Kills enemies at close range quickly
 * #4 Melee and Speed   -Insanely fast at killing bosses at close range
 * #8 Melee and Sniper  -Good for killing bosses at long distance
 *
 * Speed tower combinations
 * #5 Speed and Sniper  -Long range tower that hits very fast
 * #6 Speed and Splash  -Hits groups of enemies quickly
 *
 * Sniper tower combinations
 * #1 Sniper and Splash -Best tower at killing lots of enemies 
 *
 * @author Raymond
 */

public class Tower extends Tile
{
    public enum TowerType
    {
        BASIC, MELEE, SNIPER, SPEED, SPLASH,
    }

    private TowerType myType;
    private Color myColor;
    private TowerType second; //The second tower's type
    private Color secondColor; //The inside color
    private Boolean canCombine = true; //Can only combine once
    private Boolean isSelected;
    private int cost;
    private int attackTicker = 1;
    private int range;
    private int attack;
    private int speed;
    private int sellValue;
    private int upgradeLevel = 1;
    private int myX, myY;

    public Tower(String type, int y, int x)
    {
        super(y, x);
        myX = x;
        myY = y;
        second = null;
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
                attack = 5;
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
                attack = 12;
                range = 1;
                speed = 6;
                break;
            }

            case "Speed":
            {
                myType = TowerType.SPEED;
                myColor = Color.WHITE;
                cost = 200;
                attack = 3;
                range = 1;
                speed = 2;
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
                cost = 200;
                attack = 7;
                range = 5;
                speed = 7;
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
                attack = 5;
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
    public Color getSecondColor(){return secondColor;}

    //For the Select method
    public int getAttack(){return attack;}
    public int getRange(){return range;}
    public int getSpeed(){return speed;}
    public int getSellValue(){return sellValue/4;}

    public int getX(){return myX;}
    public int getY(){return myY;}

    public Boolean isSelected(){return isSelected;}
    public void setIsSelected(Boolean b)
    {
        isSelected = b;
    }

    //Upgrading a tower increases its attack by 1 or 2 (splash is 1, rest are 2)
    public void upgrade()
    {
        switch(myType)
        {
            case BASIC:
            {
                Color[] colorArray = {Color.ORANGE, new Color(255, 142, 32), new Color(255, 77, 0)};
                myColor = colorArray[upgradeLevel - 1]; //new Color(255, 50, 0);
                attack += 5;
                sellValue += cost * upgradeLevel;
                break;
            }

            case MELEE:
            {
                Color[] colorArray = {new Color(255, 0, 80), new Color(204, 0, 102), new Color(153, 0, 153)};
                myColor = colorArray[upgradeLevel - 1];
                attack += 10;
                sellValue += cost * upgradeLevel;
                break;
            }

            case SNIPER:
            {
                Color[] colorArray = {Color.GREEN, new Color(0, 204, 0), new Color(0, 132, 0)};
                myColor = colorArray[upgradeLevel - 1];
                //new Color(0, 175, 0);
                attack += 5;
                sellValue += cost * upgradeLevel;
                break;
            }

            case SPEED:
            {
                Color[] colorArray = {Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};
                myColor = colorArray[upgradeLevel - 1];
                attack += 3;
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
                Color[] colorArray = {Color.ORANGE, new Color(255, 142, 32), new Color(255, 77, 0)};
                myColor = colorArray[upgradeLevel - 1];
                attack += 5;
                sellValue += cost * upgradeLevel;
                break;
            }
        }
        upgradeLevel++;
    }

    //This method combines an existing tower with another tower
    public void combineTower(Tower t)
    {
        canCombine = false;
        //Add the attack stats together
        attack += t.getAttack();

        //Check the ranges and pick the longer one
        if(t.getRange() > range)
        {
            range = t.getRange();
        }

        //Check the ranges and pick the shorter one
        if(t.getSpeed() < speed)
        {
            speed = t.getSpeed();
        }

        //To get accurate sell and upgrade values
        cost = t.getCost();
        sellValue += 2000;
        //Set upgrade level back to 1
        upgradeLevel = 1;
        secondColor = myColor;
        myColor = t.getColor();
        //Set second to the current tower type
        second = myType;
        myType = t.getTowerType();
    }

    public Boolean canCombine()
    {
        return (canCombine && upgradeLevel == 4);
    }

    public int attack(ArrayList<EnemyComponent> enemyList)
    {
        int loot = 0;
        if (attackTicker == speed)
        {
            attackTicker = 0;
            for (int i = 0; i < enemyList.size(); i++)
            {
                EnemyComponent e = enemyList.get(i);
                //I think I solved the issue
                //Tested it and it looks fine
                if (e.getXPosition() + 45 >= (getX() * 50) - (range * 50)
                 && e.getXPosition() - 45 <= (getX() * 50) + (range * 50)
                 && e.getYPosition() + 45 >= (getY() * 50) - (range * 50)
                 && e.getYPosition() - 45 <= (getY() * 50) + (range * 50) )
                {
                    e.decrementHealth(attack);
                    if (!e.isAlive())
                    {
                        if(e.getType().equals("B"))
                        {
                            loot += 90;
                        }

                        else
                        {
                            loot += 8;
                        }

                        enemyList.remove(i);
                        i--;
                    }
                    //Splash towers attack everything in the list
                    if (myType != TowerType.SPLASH && second != TowerType.SPLASH)
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

