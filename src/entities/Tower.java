/**
 * Tower class holds all the information about towers.
 * Currently there are 5 tower types, each with different attributes.
 * Able to combine 2 different tower types to make an even greater tower.
 * This is our ranking system for the overall best combinations.
 * 
 * Short description for each:
 *--------------------------------------------------------------------------------
 * Antivirus tower combinations
 * #7 Antivirus and Firewall   -Kills bosses fairly fast
 * #2 Antivirus and Surge  -Good for Killing groups of enemies
 * #9 Antivirus and Quarantine  -A Quarantine tower with increased speed
 * #10 Antivirus and Encryption  -Only gives slight increases to speed and attack
 *
 * Firewall tower combinations
 * #3 Firewall and Surge  -Kills enemies at close range quickly
 * #4 Firewall and Encryption   -Insanely fast at killing bosses at close range
 * #8 Firewall and Quarantine  -Good for killing bosses at long distance
 *
 * Encryption tower combinations
 * #5 Encryption and Quarantine  -Long range tower that hits very fast
 * #6 Encryption and Surge  -Hits groups of enemies quickly
 *
 * Quarantine tower combinations
 * #1 Quarantine and Surge -Best tower at killing lots of enemies
 */

package entities;

import graphics.EnemyComponent;
import java.awt.Color;

import java.util.ArrayList;

public class Tower extends Tile
{
    //The possible tower types in CDR Tower Defense.
    public enum TowerType
    {
        ANTIVIRUS, FIREWALL, QUARANTINE, ENCRYPTION, SURGE,
    }

    private TowerType myType;
    private Color myColor;
    private Color[] colorArray;
    private TowerType second = null; //The second tower's type (for combining towers).
    private Color secondColor; //The inside color (for combining towers).
    private Boolean canCombine = true; //Can only combine once.
    private int cost;
    private int attackTicker = 1;
    private int range;
    private int attack;
    private int speed;
    private int attackIncrease;
    private int sellValue;
    private int level = 1;
    private int myX, myY;

    public Tower(String type, int y, int x)
    {
        super(y, x);
        myX = x;
        myY = y;

        //Set the attributes of the tower.
        //The speed may have to change relative to the clock/ticker.
        switch (type)
        {
            //Hits from a fair distance, low amount of damage, low attack speed, cost is low.
            case "Antivirus":
            {
                myType = TowerType.ANTIVIRUS;
                cost = 150;
                attack = 5;
                range = 3;
                speed = 4;
                attackIncrease = 5;
                break;
            }
            //Short range, high attack, moderate attack speed, cost is moderate.
            case "Firewall":
            {
                myType = TowerType.FIREWALL;
                cost = 200;
                attack = 12;
                range = 1;
                speed = 6;
                attackIncrease = 10;
                break;
            }
            //Highest range, moderate damage, slow attack speed, cost is moderately high.
            case "Quarantine":
            {
                myType = TowerType.QUARANTINE;
                cost = 200;
                attack = 7;
                range = 5;
                speed = 7;
                attackIncrease = 5;
                break;
            }
            case "Encryption":
            {
                myType = TowerType.ENCRYPTION;
                cost = 200;
                attack = 3;
                range = 1;
                speed = 2;
                attackIncrease = 3;
                break;
            }
            //Low range, low damage, slow attack speed, hits all in its radius, cost is high.
            case "Surge":
            {
                myType = TowerType.SURGE;
                cost = 300;
                attack = 3;
                range = 1;
                speed = 10;
                attackIncrease = 2;
                break;
            }
        }
        setColors();
        sellValue = cost;
    }

    @Override
    public TileType getTileType() {return TileType.TOWER;}
    public TowerType getTowerType() {return myType;}
    public int getCost() {return cost;}
    public int getLevel(){return level;}
    public int getUpgradeCost(){return cost * level;}
    public Color getColor(){return myColor;}
    public Color getSecondColor(){return secondColor;}
    public Boolean canCombine(TowerType t) {return second == null && level == 4 && t != myType;}

    //For the Select method
    public int getAttack(){return attack;}
    public int getRange(){return range;}
    public int getSpeed(){return speed;}
    public int getSellValue(){return sellValue / 4;}

    @Override
    public int getX(){return myX;}
    @Override
    public int getY(){return myY;}

    //Upgrading a tower increases its attack by 1 or 2 (Surge is 1, rest are 2)
    public void upgrade()
    {
        myColor = colorArray[level - 1]; //new Color(255, 50, 0);
        attack += attackIncrease;
        sellValue += cost * level;
        level++;
    }

    //This method combines an existing tower with another tower.
    public void combineTower(Tower t)
    {
        canCombine = false;
        //Add the attack stats together.
        attack += t.getAttack();

        //Check the ranges and pick the longer one.
        if(t.getRange() > range)
        {
            range = t.getRange();
        }

        //Check the ranges and pick the shorter one.
        if(t.getSpeed() < speed)
        {
            speed = t.getSpeed();
        }

        //To get accurate sell and upgrade values.
        cost = t.getCost();
        sellValue += 2000;

        //Set upgrade level back to 1.
        level = 1;

        //Set second to the current tower type.
        second = myType;
        myType = t.getTowerType();

        //Change color set.
        secondColor = myColor;
        setColors();
    }

    //Logic for attacking enemies.
    public int attack(ArrayList<EnemyComponent> enemyList)
    {
        int loot = 0;
        if (attackTicker == speed)
        {
            attackTicker = 0;
            for (int i = 0; i < enemyList.size(); i++)
            {
                EnemyComponent e = enemyList.get(i);

                
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

                    //Surge towers attack everything in the list
                    if (myType != TowerType.SURGE && second != TowerType.SURGE)
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

    //Logic for tower colors.
    private void setColors()
    {
        switch (myType)
        {
            case ANTIVIRUS:
            {
                myColor = Color.YELLOW;
                colorArray = new Color[]{Color.ORANGE, new Color(255, 142, 32), new Color(255, 77, 0)};
                break;
            }
            case FIREWALL:
            {
                myColor = Color.RED;
                colorArray = new Color[]{new Color(255, 0, 80), new Color(204, 0, 102), new Color(153, 0, 153)};
                break;
            }
            case QUARANTINE:
            {
                myColor = new Color(128, 255, 0);
                colorArray = new Color[]{Color.GREEN, new Color(0, 204, 0), new Color(0, 132, 0)};
                break;
            }
            case ENCRYPTION:
            {
                myColor = Color.WHITE;
                colorArray = new Color[]{Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};
                break;
            }
            case SURGE:
            {
                myColor = Color.CYAN;
                colorArray = new Color[]{new Color(0, 163, 163), new Color(0, 102, 204), Color.BLUE};
                break;
            }
        }
    }
}

