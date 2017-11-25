/**
 * This class represents the graphical component of the map and
 * controls the creation/deletion/modification of enemies and tiles
 */

package graphics;

import entities.MapData;
import entities.Path;
import entities.Tile;
import entities.Tower;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static entities.Tile.TileType.TILE;
import static entities.Tile.TileType.TOWER;

public class MapComponent extends JComponent
{
    private static BufferedImage baseImage, tileImage;

    private final int SPAWN_INTERVAL = 10;    //Number of clock ticks between enemy spawns
    private final int BOSS_ROUNDS = 10; //Number of rounds between bosses

    private MapData data;
    private TileComponent[][] componentMat;
    private ArrayList<EnemyComponent> enemyList;
    private ArrayList<Tower> towerList;
    private Rectangle selectedRange = null;
    private String enemyType;
    private int enemyTicker = 1;
    private int enemiesSpawned = 0;
    private int numEnemies = 0;
    private int enemyHealth = 5;

    public static BufferedImage getBaseImage() {return baseImage;}

    public MapComponent(MapData d)
    {
        data = d;
        enemyList = new ArrayList<>();
        towerList = new ArrayList<>();
        String background = "images/background.png";

        try
        {
            baseImage = ImageIO.read(new File("images/cpu.png"));
            if(data.getNumRows() > 11 || data.getNumCols() > 13)
            {
                background = "images/background2.png";
            }
            tileImage = ImageIO.read(new File(background));
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Image not found");
        }

        buildMatrix();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        draw(g2);

        if (!data.getRoundOver())
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
        for (int i = 0; i < data.getNumRows(); i++)
        {
            for (int j = 0; j < data.getNumCols(); j++)
            {
                componentMat[i][j].draw(g2);
            }
        }

        //Logic to draw the radius of the tower
        if(selectedRange != null)
        {
            drawRange(g2);
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

                //If the round is a boss round and he hit us
                if(data.getRound() % BOSS_ROUNDS == 0)
                {
                    data.decrementHealth(5);
                    data.incrementMoney(60);
                }
                //Else its a regular round and an enemy hit us
                else
                {
                    data.decrementHealth(1);
                    data.incrementMoney(4);
                }
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
        enemiesSpawned = 0;
        enemyTicker = 1;

        //Boss wave
        if(data.getRound() % BOSS_ROUNDS == 0)
        {
            numEnemies = 1;
            enemyHealth = 525 + (int) (Math.pow(data.getRound(), 2.45) + (data.getRound()) * 3);
            enemyType = "B";
        }

        //Normal enemy wave
        else
        {
            numEnemies = 8 + ((data.getRound() - 1) * 5 / 2);
            enemyHealth = 25 + (int) (Math.pow(data.getRound() - 1, 1.8) + (data.getRound() - 1) * 2);
            enemyType = "R";
        }

        data.setRoundOver(false);
    }

    //Draws all the enemies on the map
    private void drawEnemies(Graphics2D g2)
    {
        //If enemyTicker = -1, all enemies have spawned
        //If enemyTicker = 0, play hasn't been pressed
        //enemiesSpawned is used so we can remove enemies from the list
        //without making this method think it must spawn more
        if (!data.getRoundOver())
        {
            if (numEnemies != 0 && enemyTicker > 0 && (enemyTicker == SPAWN_INTERVAL || enemiesSpawned == 0))
            {
                enemyList.add(new EnemyComponent(data.getHead(), enemyHealth, enemyType));
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
            data.setRoundOver(true);
            data.incrementMoney(50 + (data.getRound() - 1) * 3);
            repaint();
        }
    }

    public String selectTower(int y, int x)
    {
        Tile tileSelected = data.getTile(y, x);
        String info;

        if(tileSelected.getTileType() == TOWER)
        {
            Tower t = ((Tower) tileSelected);
            String cost;

            createRange(t);
            
            if(t.getLevel() == 4)
            {
                cost = String.format("Combination Cost: %s", (t.canCombine(null) ? "$2000" : "N/A"));
            }
            else
            {
                cost = String.format("Upgrade Cost: $%d", t.getUpgradeCost());
            }
            info = String.format("Sell Value: $%d   %s   Attack: %d   Range: %d   Speed: %d   Level: %d",
                    t.getSellValue(), cost, t.getAttack(), t.getRange(), t.getSpeed(), t.getLevel());
        }
        else
        {
            info = "Attributes appear here";
            selectedRange = null;
            repaint();
        }

        return info;
    }

    //Use this method to buy, sell, upgrade, and combine tiles
    public void swapTile(String s, int y, int x)
    {
        Tile tileSelected = data.getTile(y, x);

        if (s.equals("Sell")) //Sell a tower, return 1/4 of the cost to the player
        {
            if(tileSelected.getTileType() == TOWER)
            {
                data.incrementMoney(((Tower)tileSelected).getSellValue());
                towerList.remove(tileSelected);
                selectedRange = null;
                tileSelected = new Tile(y, x);
                data.setTile(tileSelected, y, x);
                componentMat[y][x] = new TileComponent(tileSelected, y, x);
                repaint();
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
                if(t.getUpgradeCost() <= data.getMoney() && t.getLevel() < 4)
                {
                    data.decrementMoney(t.getUpgradeCost());
                    t.upgrade();
                    createRange(t);
                }
                if(data.getRoundOver())
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

            if(2000 <= data.getMoney() && t.canCombine(towerSelected.getTowerType()))
            {
                t.combineTower(towerSelected);
                data.decrementMoney(2000);
                createRange(t);
            }

            if(data.getRoundOver())
            {
                componentMat[y][x].draw((Graphics2D)getGraphics());
            }
        }

        else    //Buy a tower, decrement money by the cost
        {
            Tower towerSelected = new Tower(s, y, x);
            if (tileSelected.getTileType() == TILE && towerSelected.getCost() <= data.getMoney())
            {
                createRange(towerSelected);
                data.decrementMoney(towerSelected.getCost());
                towerList.add(towerSelected);
                data.setTile(towerSelected, y, x);
                componentMat[y][x] = new TileComponent(towerSelected, y, x);
                if(data.getRoundOver())
                {
                    componentMat[y][x].draw((Graphics2D)getGraphics());
                }
            }
        }
    }

    private void buildMatrix()
    {
        componentMat = new TileComponent[data.getNumRows()][data.getNumCols()];

        for (int i = 0; i < data.getNumRows(); i++)
        {
            for (int j = 0; j < data.getNumCols(); j++)
            {
                componentMat[i][j] = new TileComponent(data.getTile(i, j), i, j);
            }
        }
    }

    private void createRange(Tower t)
    {
        if (selectedRange != null)
        {
            selectedRange = null;
            repaint();
        }

        int range = t.getRange();
        int x = (t.getX() * 50) - (range * 50);
        int y = (t.getY() * 50) - (range * 50);
        selectedRange = new Rectangle(x + 1, y + 1, 50 * (range * 2 + 1)-2, 50 * (range * 2 + 1)-2);

        drawRange((Graphics2D)getGraphics());
    }

    private void drawRange(Graphics2D g2)
    {
        g2.setColor(Color.RED);
        g2.draw(selectedRange);
    }
}
