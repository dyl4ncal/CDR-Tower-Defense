package graphics;

import entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TileComponent extends JComponent
{
    private Tile myTile;
    private int myY, myX, mySize;

    public TileComponent(Tile tile, int y, int x)
    {
        myTile = tile;
        myY = y * 50;
        myX = x * 50;
        //Set the size of each tile to 50, just good practice to use a variable
        mySize = 50;
    }

    public void draw(Graphics2D g2)
    {
        switch (myTile.getTileType())
        {
            case TILE:
            {
                g2.setColor(Color.BLACK);
                g2.drawRect(myX, myY, mySize, mySize);
                break;
            }
            //If we decide to add more towers
            //we check the tower type and draw the correct tower
            case TOWER:
            {
                Tower myTower = (Tower) myTile;
                g2.setColor(myTower.getColor());
                g2.fillRect(myX+7, myY+7, mySize-15, mySize-15);
                //Surround the tower with a border
                g2.setColor(Color.BLACK);
                g2.drawRect(myX+7, myY+7, mySize-15, mySize-15);
                
                if(myTower.getSecondColor() != null)
                {
                    //Draw a tower inside the other tower around it
                    g2.setColor(myTower.getSecondColor());
                    g2.fillRect(myX+14, myY+14, mySize-28, mySize-28);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(myX+14, myY+14, mySize-28, mySize-28);
                }

                g2.drawRect(myX, myY, mySize, mySize);
                g2.drawString(String.format("%d", myTower.getUpgradeLevel()), myX + 22, myY + 30);
                break;
            }

            default:
            {
                break;
            }
        }
    }

    public void drawPath(Graphics2D g2)
    {
        switch (myTile.getTileType())
        {
            case PATH:
            {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(myX, myY, mySize, mySize);
                break;
            }
            case BASE:
            {
                g2.drawImage(MapComponent.getBaseImage(), myX, myY, null);
                /*g2.setColor(Color.BLACK);
                g2.drawRect(myX, myY, mySize, mySize);*/
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
