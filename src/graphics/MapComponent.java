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
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.*;

import io.MapData;

public class MapComponent extends JComponent
{

    private int x, y, s, mapRows, mapCols;
    private MapData data;
    private ArrayList<EnemyComponent> enemyComponentList;
    private int enemyTicker = 1;
    private int enemiesSpawned = 0;
    private int numEnemies;
    private int spawnFrequency;
    private BufferedImage base, tile;
    private boolean setupComplete = false;

    //Create a cell using rows, columns and MapData
    public MapComponent(MapData d)
    {
        data = d;
        mapRows = d.getNumRows();
        mapCols = d.getNumCols();
        enemyComponentList = new ArrayList<>();
        numEnemies = d.getNumEnemies();
        spawnFrequency = d.getSpawnFrequency();
    }

    public void setSetupComplete() {setupComplete = true;}

    //I'm not sure at the moment if this makes it repaint everything
    //If it does, it's pretty costly
    //since it already has to go through the 2D array twice
    @Override
    public void paintComponent(Graphics g)
    {
        draw(g);

        //Don't paint enemies during setup
        if (setupComplete)
        {
            animateEnemy();
            drawEnemies(g);
        }
    }

    private void draw(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        x = 0;
        y = 0;
        s = 50;

        /*THIS IS WHERE THE BACKGROUND IS BEING CREATED
        *Sadly, it seems drawing image is very costly, at least for my
        *laptop
        **/
        try
        {
            base = ImageIO.read(new File("cpu.png"));
            /*These make the background look cool, but on my laptop
            *it makes it much slower than what it should be
            **/
            //tile = ImageIO.read(new File("background.jpg"));
            //tile = ImageIO.read(new File("background1.jpg"));
            tile = ImageIO.read(new File("background2.png"));
            g2.drawImage(tile, x, y, null);
        }
        catch(IOException e){}

        //This will draw the path cells
        for (int i = 0; i < mapRows; i++)
        {
            y = s * i;
            for (int j = 0; j < mapCols; j++)
            {
                x = s * j;
                //Assume input data array is correct
                switch(data.getTile(i,j).getTileType())
                {
                    //Draws the path enemies travel
                    case PATH:
                        g2.setColor(Color.WHITE);
                        g2.fillRect(x, y, s, s);
                        break;

                    default:
                        break;
                }
            }
        }

        //This will draw the map cells
        for (int i = 0; i < mapRows; i++)
        {
            y = s * i;
            for (int j = 0; j < mapCols; j++)
            {
                x = s * j;
                //Assume input data array is correct
                switch(data.getTile(i,j).getTileType())
                {
                    //Draws tiles that a tower can go on
                    case TILE:
                        //g2.drawImage(tile, x, y, null);
                        //g2.setColor(Color.GREEN);
                        //g2.fillRect(x, y, s, s);
                        //Outline it with black so it is clear where a tile is
                        g2.setColor(Color.BLACK);
                        g2.drawRect(x, y, s, s);
                        break;

                    //This draws the tile to represent the base
                    case BASE:
                        //Draws the cpu on the thing
                        g2.drawImage(base, x, y, null);
                        g2.setColor(Color.BLACK);
                        g2.drawRect(x, y, s, s);
                        /*g2.setColor(Color.MAGENTA);
                        g2.fillRect(x, y, s, s);
                        //Outline it with black so it is clear where a tile is
                        g2.setColor(Color.BLACK);
                        g2.drawRect(x, y, s, s);
                        */
                        //Draw a circle in it
                        Ellipse2D.Double e = new Ellipse2D.Double(x+15, y+15, 20, 20);
                        g2.setColor(Color.BLACK);
                        g2.fill(e);
                        break;

                    //This draws the tile to represent towers
                    case TOWER:
                        g2.setColor(Color.YELLOW);
                        g2.fillRect(x, y, s, s);
                        //Outline it with black so it is clear where a tile is
                        g2.setColor(Color.BLACK);
                        g2.drawRect(x, y, s, s);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void animateEnemy()
    {
        for(int i = 0; i < enemyComponentList.size();)
        {
            enemyComponentList.get(i).update();

            //If enemy dies as a result of move, remove from list
            if (!enemyComponentList.get(i).isAlive())
            {
                enemyComponentList.remove(i);
                data.decrementHealth();
            }
            else
            {
                i++;
            }
        }
    }

    //Draws all the enemies on the map
    private void drawEnemies(Graphics g)
    {
        //If enemyTicker = -1, all enemies have spawned
        //If enemyTicker = 0, play hasn't been pressed
        //enemiesSpawned is used so we can remove enemies from the list
        //without making this method think it must spawn more
        if (enemyTicker > 0 && (enemyTicker == spawnFrequency || enemiesSpawned == 0))
        {
            enemyComponentList.add(new EnemyComponent(data));
            enemiesSpawned++;
            enemyTicker = 1;

            if (enemiesSpawned == numEnemies)
            {
                enemyTicker = -1;
            }
        }

        for(int i = 0; i < enemyComponentList.size(); i++)
        {
            enemyComponentList.get(i).drawEnemy(g);
        }

        if (enemyTicker > -1)
        {
            enemyTicker++;
        }
    }
}


