/**
 * This class stores all graphical information for a given tile.
 */

package graphics;

import entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TileComponent extends JComponent
{
    private final int TILE_LENGTH = 50;

    private Tile myTile;
    private int myY, myX;

    public TileComponent(Tile tile, int y, int x)
    {
        myTile = tile;
        myY = y * TILE_LENGTH;
        myX = x * TILE_LENGTH;
    }

    public void draw(Graphics2D g2)
    {
        switch (myTile.getTileType())
        {
            case TILE:
            {
                g2.setColor(Color.BLACK);
                g2.drawRect(myX, myY, TILE_LENGTH, TILE_LENGTH);
                break;
            }
            //If we decide to add more towers
            //we check the tower type and draw the correct tower.
            case TOWER:
            {
                Tower myTower = (Tower) myTile;
                g2.setColor(myTower.getColor());
                g2.fillRect(myX+7, myY+7, TILE_LENGTH -15, TILE_LENGTH -15);
                //Surround the tower with a border.
                g2.setColor(Color.BLACK);
                g2.drawRect(myX+7, myY+7, TILE_LENGTH -15, TILE_LENGTH -15);
                
                if(myTower.getSecondColor() != null)
                {
                    //Draw a tower inside the other tower around it (for combining towers).
                    g2.setColor(myTower.getSecondColor());
                    g2.fillRect(myX+14, myY+14, TILE_LENGTH -28, TILE_LENGTH -28);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(myX+14, myY+14, TILE_LENGTH -28, TILE_LENGTH -28);
                }

                g2.drawRect(myX, myY, TILE_LENGTH, TILE_LENGTH);
                g2.drawString(String.format("%d", myTower.getLevel()), myX + 22, myY + 30);
                break;
            }
        }
    }

    //Logic to draw the enemy path.
    public void drawPath(Graphics2D g2)
    {
        switch (myTile.getTileType())
        {
            case PATH:
            {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(myX, myY, TILE_LENGTH, TILE_LENGTH);
                break;
            }
            case BASE:
            {
                g2.drawImage(MapComponent.getBaseImage(), myX, myY, null);
                Ellipse2D.Double e = new Ellipse2D.Double(myX + 10, myY + 10, 30, 30);
                g2.setColor(Color.BLACK);
                g2.fill(e);
                g2.setColor(Color.CYAN);
                g2.drawString("CPU", myX + 13, myY + 30);
                break;
            }
        }
    }
}
