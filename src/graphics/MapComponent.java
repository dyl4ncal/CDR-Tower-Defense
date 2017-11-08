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

import entities.Path;
import entities.Tile;
import entities.Tower;
import io.MapData;

import static entities.Tile.TileType.TILE;
import static entities.Tile.TileType.TOWER;

public class MapComponent extends JComponent
{
    private static BufferedImage base, tile;

    private int x, y, s, mapRows, mapCols;
    private MapData data;
    private ArrayList<EnemyComponent> enemyComponentList;
    private TileComponent[][] componentMat;
    private int enemyTicker = 1;
    private int enemiesSpawned = 0;
    private int numEnemies = 0;
    private int enemyHealth = 5;
    private int spawnFrequency = 10;
    private boolean setupComplete = false;
    private boolean roundOver = false;

    //Create a cell using rows, columns and MapData
    public MapComponent(MapData d)
    {
        data = d;
        mapRows = d.getNumRows();
        mapCols = d.getNumCols();
        enemyComponentList = new ArrayList<>();
        numEnemies = d.getNumEnemies();
        spawnFrequency = d.getSpawnFrequency();

        try
        {
            base = ImageIO.read(new File("cpu.png"));
            tile = ImageIO.read(new File("background2.png"));
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Image not found");
        }

        buildMatrix();
    }

    public void setSetupComplete() {setupComplete = true;}
    public boolean isRoundOver()
    {
        if(!roundOver)
        {
            return false;
        }

        else
        {
            roundOver = false;
            return true;
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        draw(g2);

        if (setupComplete)
        {
            updateEnemies();
            drawEnemies(g2);
        }
    }


    //Not quite right yet
    public void clockTick()
    {
        Graphics2D g2 = (Graphics2D) getGraphics();
        drawPath(g2);
        updateEnemies();
        drawEnemies(g2);
    }

    public void setTile(Tower t, int y, int x)
    {
        if (data.getTile(y, x).getTileType() == TILE && t.getCost() <= data.getMoney())
        {
            data.setTile(t, y, x);
            componentMat[y][x] = new TileComponent(t, y * 50, x * 50);
            componentMat[y][x].draw((Graphics2D) getGraphics());
            data.decrementMoney(t.getCost());
        }
    }

    //This method will sell a tower and replace it with a tile
    //Will return 1/4 of the cost back to the player
    public void sellTower(int y, int x)
    {
        if(data.getTile(y, x).getTileType() == TOWER)
        {
            //When you sell a tower, make sure to get the tower's cost
            Tower tower = (Tower) data.getTile(y, x);
            data.incrementMoney(tower.getCost() / 4);
            Tile t = new Tile(y, x);
            data.setTile(t, y, x);
            componentMat[y][x] = new TileComponent(t, y * 50, x * 50);
            componentMat[y][x].draw((Graphics2D) getGraphics());
        }
    }

    //May want to use this method to upgrade the tower
    //public void upgradeTower(){}

    public static BufferedImage getBase() {return base;}
    public static BufferedImage getTile() {return tile;}

    private void buildMatrix()
    {
        componentMat = new TileComponent[mapRows][mapCols];

        for (int i = 0; i < mapRows; i++)
        {
            for (int j = 0; j < mapCols; j++)
            {
                componentMat[i][j] = new TileComponent(data.getTile(i, j), i * 50, j * 50);
            }
        }
    }

    private void draw(Graphics2D g2)
    {
        g2.drawImage(tile, 0, 0, null);

        drawPath(g2);

        //This will draw the non-path tiles
        for (int i = 0; i < mapRows; i++)
        {
            for (int j = 0; j < mapCols; j++)
            {
                componentMat[i][j].draw(g2);
            }
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

    private void updateEnemies()
    {
        for(int i = 0; i < enemyComponentList.size();)
        {
            enemyComponentList.get(i).update();

            //If enemy dies as a result of move, remove from list
            //This will only work when it dies in the base
            //Will need another statement checking if its in the last tile
            if (!enemyComponentList.get(i).isAlive())
            {
                enemyComponentList.remove(i);
                data.incrementMoney(12);
                data.decrementHealth();
            }

            else
            {
                i++;
            }
            
            //If there are no more enemies, the round is over
            if(enemyComponentList.isEmpty())
            {
                roundOver = true;
            }
        }
    }

    //This method creates a new wave of enemies depending on current round
    public void createWave(int round)
    {
        enemiesSpawned = 0;
        enemyTicker = 1;
        //Should be a better function tbh
        numEnemies = (round * 3) + 10;
        enemyHealth += round * 3;
        drawEnemies((Graphics2D) getGraphics());
    }

    //Draws all the enemies on the map
    private void drawEnemies(Graphics g)
    {
        //If enemyTicker = -1, all enemies have spawned
        //If enemyTicker = 0, play hasn't been pressed
        //enemiesSpawned is used so we can remove enemies from the list
        //without making this method think it must spawn more
        if (numEnemies != 0 && enemyTicker > 0 && (enemyTicker == spawnFrequency || enemiesSpawned == 0))
        {
            enemyComponentList.add(new EnemyComponent(data.getHead(), enemyHealth));
            enemiesSpawned++;
            enemyTicker = 1;

            if (enemiesSpawned == numEnemies)
            {
                enemyTicker = -1;
            }
        }

        for(int i = 0; i < enemyComponentList.size(); i++)
        {
            enemyComponentList.get(i).draw(g);
        }

        if (enemyTicker > -1)
        {
            enemyTicker++;
        }
    }
}


