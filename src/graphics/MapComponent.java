/**
 * This class is for creating the map
 * And for creating enemies that are on the map
 * I'm not sure how towers will interact with this
 *
 * @author Raymond
 */

package graphics;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import java.lang.Math;

import entities.Path;
import entities.Tile;
import entities.Tower;
import entities.MapData;

import static entities.Tile.TileType.*;

public class MapComponent extends JComponent
{
    private static BufferedImage baseImage, tileImage;

    private int x, y, s, mapRows, mapCols;
    private MapData data;
    private ArrayList<EnemyComponent> enemyList;
    private ArrayList<Tower> towerList;
    private Tower previousTower;
    private TileComponent[][] componentMat;
    private int enemyTicker = 1;
    private int enemiesSpawned = 0;
    private int numEnemies = 0;
    private int enemyHealth = 5;
    private int spawnFrequency = 10;
    private boolean roundOver = true;

    public static BufferedImage getBaseImage() {return baseImage;}
    public static BufferedImage getTileImage() {return tileImage;}

    //Create a cell using rows, columns and MapData
    public MapComponent(MapData d)
    {
        data = d;
        mapRows = d.getNumRows();
        mapCols = d.getNumCols();
        enemyList = new ArrayList<>();
        towerList = new ArrayList<>();
        numEnemies = d.getNumEnemies();
        spawnFrequency = d.getSpawnInterval();

        try
        {
            baseImage = ImageIO.read(new File("cpu.png"));
            tileImage = ImageIO.read(new File("background2.png"));
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Image not found");
        }

        buildMatrix();
    }

    public boolean isRoundOver() {return roundOver;}

    private void buildMatrix()
    {
        componentMat = new TileComponent[mapRows][mapCols];

        for (int i = 0; i < mapRows; i++)
        {
            for (int j = 0; j < mapCols; j++)
            {
                componentMat[i][j] = new TileComponent(data.getTile(i, j), i, j);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        draw(g2);

        if (!roundOver)
        {
            moveEnemies();
            drawEnemies(g2);
            attack(g2);
        }
    }

    private void draw(Graphics2D g2)
    {
        g2.drawImage(tileImage, 0, 0, null);

        drawPath(g2);

        //This will draw the non-path tiles
        for (int i = 0; i < mapRows; i++)
        {
            for (int j = 0; j < mapCols; j++)
            {
                componentMat[i][j].draw(g2);
            }
        }

        if(previousTower != null && previousTower.isSelected())
        {
            int range = previousTower.getRange();
            g2.setColor(Color.RED);
            int x = (previousTower.getX() * 50) - (range * 50);
            int y = (previousTower.getY() * 50) - (range * 50);
            g2.drawRect(x + 1, y + 1, 50 * (range * 2 + 1)-2, 50 * (range * 2 + 1)-2);
        }
    }

    private void drawPath(Graphics2D g2)
    {
        Path path = data.getHead();

        while(path != null)
        {
            componentMat[path.getY()][path.getX()].drawPath(g2);
            path = path.getMyNext();
        }
    }

    private void moveEnemies()
    {
        for(int i = 0; i < enemyList.size();)
        {
            enemyList.get(i).move();

            //If enemy dies as a result of move, remove from list
            //This will only work when it dies in the baseImage
            //Will need another statement checking if its in the last tileImage
            if (!enemyList.get(i).isAlive())
            {
                enemyList.remove(i);
                data.incrementMoney(8);
                data.decrementHealth();
            }
            else
            {
                i++;
            }
        }
    }

    //This method creates a new wave of enemies depending on current round
    public void createWave()
    {
        //Boss wave
        if(data.getRound() % 10 == 0)
        {
            createBossWave();
        }

        //Normal enemy wave
        else
        {
            enemiesSpawned = 0;
            enemyTicker = 1;
            //Should be a better function tbh
            numEnemies = 8 + ((data.getRound() - 1) * 5 / 2);
            enemyHealth = 25 + (int) (Math.pow(data.getRound() - 1, 1.7) + (data.getRound() - 1) * 3);
            roundOver = false;
        }
    }

    //Bosses should give much more loot than a regular enemy
    //They should also deal more damage to the base
    private void createBossWave()
    {
        enemiesSpawned = 0;
        enemyTicker = 1;
        numEnemies = 1;
        enemyHealth = 550 + (int) (Math.pow(data.getRound(), 2.2) + (data.getRound()) * 4);
        roundOver = false;
    }

    //Draws all the enemies on the map
    private void drawEnemies(Graphics2D g2)
    {
        //If enemyTicker = -1, all enemies have spawned
        //If enemyTicker = 0, play hasn't been pressed
        //enemiesSpawned is used so we can remove enemies from the list
        //without making this method think it must spawn more
        if (!roundOver)
        {
            if (numEnemies != 0 && enemyTicker > 0 && (enemyTicker == spawnFrequency || enemiesSpawned == 0))
            {
                enemyList.add(new EnemyComponent(data.getHead(), enemyHealth));
                enemiesSpawned++;
                enemyTicker = 1;

                if (enemiesSpawned == numEnemies)
                {
                    enemyTicker = -1;
                }
            }
        }

        for(int i = 0; i < enemyList.size(); i++)
        {
            enemyList.get(i).draw(g2);
        }

        if (enemyTicker > -1)
        {
            enemyTicker++;
        }
    }

    private void attack(Graphics2D g2)
    {
        for (int i = 0; i < towerList.size(); i++)
        {
            if (!enemyList.isEmpty())
            {
                int loot = towerList.get(i).attack(enemyList);
                if (loot != 0)
                {
                    data.incrementMoney(loot);
                }
            }
        }

        //If there are no more enemies and all have spawned, the round is over
        if(enemyList.isEmpty() && enemiesSpawned == numEnemies)
        {
            roundOver = true;
            //Get money for finishing a round?
            if(data.getRound() % 10 == 0)
            {
                data.incrementMoney(130 + (data.getRound()) * 2);
            }
            else
            {
                data.incrementMoney(50 + (data.getRound() - 1) * 2);
            }
            repaint();
        }
    }

    public String selectTower(String s, int x, int y)
    {
        Tile tileSelected = data.getTile(y, x);
        String info = "";
        int sell, upgrade, attack, range, speed, level;

        if(previousTower != null)
        {
            previousTower.setIsSelected(false);
        }

        if(tileSelected.getTileType() == TOWER)
        {
            Tower t = ((Tower) tileSelected);
            t.setIsSelected(true);
            previousTower = t;
            sell = t.getSellValue();
            upgrade = t.getUpgradeCost();
            attack = t.getAttack();
            range = t.getRange();
            speed = t.getSpeed();
            level = t.getUpgradeLevel();
            
            if(t.getUpgradeLevel() == 4)
            {
                upgrade = t.getUpgradeCost();
                info = String.format("Sell Value: $%d   Upgrade Cost: N/A   Attack: %d   Range: %d   Speed: %d   Level: %d"
                        , sell, attack, range, speed, level);
            }
            
            else
            {
                info = String.format("Sell Value: $%d   Upgrade Cost: $%d   Attack: %d   Range: %d   Speed: %d   Level: %d"
                        , sell, upgrade, attack, range, speed, level);
            }
        }

        else
        {
            info = "Attributes appear here";
        }

        repaint();

        return info;
    }

    //Use this method to buy or sell tiles
    public void swapTile(String s, int y, int x)
    {
        Tile tileSelected = data.getTile(y, x);

        if (s.equals("Sell")) //Sell a tower, return 1/4 of the cost to the player
        {
            if(tileSelected.getTileType() == TOWER)
            {
                data.incrementMoney(((Tower)tileSelected).getSellValue());
                ((Tower) tileSelected).setIsSelected(false);
                repaint();
                towerList.remove(tileSelected);
                tileSelected = new Tile(y, x);
                data.setTile(tileSelected, y, x);
                componentMat[y][x] = new TileComponent(tileSelected, y, x);
                if (roundOver)
                {
                    repaint();
                }
            }
        }

        //Upgrades cannot go past level 4
        else if(s.equals("Upgrade"))
        {
            //Check if selected tile is a tower and if you have enough money
            if(tileSelected.getTileType() == TOWER)
            {
                //Upgrade the tower
                Tower t = (Tower) tileSelected;
                if(t.getUpgradeCost() <= data.getMoney() && t.getUpgradeLevel() < 4)
                {
                    data.decrementMoney(t.getUpgradeCost());
                    t.upgrade();
                }

                if(roundOver)
                {
                    componentMat[y][x].draw((Graphics2D)getGraphics());
                }
            }
        }

        //THIS IS THE PART THAT CHECKS IF YOU CAN COMBINE TOWERS
        else if(tileSelected.getTileType() == TOWER)
        {
            Tower t = (Tower) tileSelected;
            Tower towerSelected = new Tower(s, y, x);
            //LOL this got to be pretty big
            //This asks 
            //Do you have enough money?
            //Is the tower fully upgraded?
            //Can the tower actually combine?
            //Is the tower the same type as the one to combine?
            if(1500 <= data.getMoney() 
               && t.getUpgradeLevel() == 4 
               && t.canCombine() 
               && t.getTowerType() != towerSelected.getTowerType())
            {
                t.combineTower(towerSelected);
                data.decrementMoney(1500);
                repaint();
            }

            if(roundOver)
            {
                componentMat[y][x].draw((Graphics2D)getGraphics());
            }
        }

        else    //Buy a tower, decrement money by the cost
        {
            Tower towerSelected = new Tower(s, y, x);
            if (tileSelected.getTileType() == TILE && towerSelected.getCost() <= data.getMoney())
            {
                towerSelected.setIsSelected(true);
                previousTower = towerSelected;
                repaint();
                data.decrementMoney(towerSelected.getCost());
                towerList.add(towerSelected);
                data.setTile(towerSelected, y, x);
                componentMat[y][x] = new TileComponent(towerSelected, y, x);
                if(roundOver)
                {
                    componentMat[y][x].draw((Graphics2D)getGraphics());
                }
            }
        }
    }
}
