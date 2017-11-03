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
        myY = y;
        myX = x;
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
                switch(myTower.getTowerType())
                {
                    case "Sniper":
                    {
                        g2.setColor(Color.GREEN);
                         //Make the towers slightly smaller than the tile size
                        g2.fillRect(myX+7, myY+7, mySize-15, mySize-15);
                        break;
                    }

                    case "Basic":
                    {
                        g2.setColor(Color.YELLOW);
                         //Make the towers slightly smaller than the tile size
                        g2.fillRect(myX+7, myY+7, mySize-15, mySize-15);
                        break;
                    }

                    case "Melee":
                    {
                        g2.setColor(Color.RED);
                         //Make the towers slightly smaller than the tile size
                        g2.fillRect(myX+7, myY+7, mySize-15, mySize-15);
                        break;
                    }

                    default:
                    {
                        break;
                    }
                }
                //Surround the tower with a border
                g2.setColor(Color.BLACK);
                g2.drawRect(myX+7, myY+7, mySize-15, mySize-15);
                g2.setColor(Color.BLACK);
                g2.drawRect(myX, myY, mySize, mySize);
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
                g2.setColor(Color.WHITE);
                g2.fillRect(myX, myY, mySize, mySize);
                break;
            }
            case BASE:
            {
                g2.drawImage(MapComponent.getBase(), myX, myY, null);
                g2.setColor(Color.BLACK);
                g2.drawRect(myX, myY, mySize, mySize);
                Ellipse2D.Double e = new Ellipse2D.Double(myX + 15, myY + 15, 20, 20);
                g2.setColor(Color.BLACK);
                g2.fill(e);
                break;
            }
        }
    }
}
