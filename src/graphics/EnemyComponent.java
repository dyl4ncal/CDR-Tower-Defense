/**
 * This class creates and moves enemies on the map.
 * This class really just gives information to the MapComponent class of where to draw an enemy
 * This class doesn't actually draw an enemy
 * @author Dylan and Raymond
 */

package graphics;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

import entities.Enemy;
import io.*;

public class EnemyComponent extends JComponent
{   
    private Enemy myEnemy;
    
    public EnemyComponent(MapData md)
    {
        myEnemy = new Enemy(md);
    }

    //I dont think I need this, but I'll keep it here just because -RAYMOND
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        super.paintComponent(g);
        drawEnemy(g);
    }

    public void drawEnemy(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        //As of right now, an enemy will be a circle in the center of the path
        Ellipse2D.Double e = new Ellipse2D.Double(myEnemy.getX() + 15, myEnemy.getY() +15, 20, 20);
        g2.setColor(Color.BLACK);
        g2.fill(e);
    }

    public void update() {myEnemy.move();}

    public boolean isAlive() {return myEnemy.isAlive();}
}