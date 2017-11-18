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

import entities.Path;

public class EnemyComponent extends JComponent
{   
    private int x = 0;
    private int y = 0;
    private int moveTicker = 0;
    private int myHealth = 0;
    private Path.Direction lastDir;
    private Path.Direction currentDir;
    private boolean dirChanged;
    private boolean isAlive = true;
    private Path currentLocation;
    
    public EnemyComponent(Path head, int h)
    {
        myHealth = h;
        currentLocation = head;
        currentDir = currentLocation.getMyDirection();
        lastDir = currentDir;
        setCoords();
    }

    @Override
    public int getX() {return x / 50;}
    @Override
    public int getY() {return y / 50;}
    public boolean isAlive() {return isAlive;}

    public void draw(Graphics2D g2)
    {
        //As of right now, an enemy will be a circle in the center of the path
        Ellipse2D.Double e = new Ellipse2D.Double(x + 10, y + 10, 30, 30);
        g2.setColor(Color.BLACK);
        g2.fill(e);
        g2.setColor(Color.CYAN);

            if(myHealth < 10)
            {
                g2.drawString(String.format("%d", myHealth), x + 22, y + 30);
            }

            else if(myHealth > 9999)
            {
                g2.drawString(String.format("%d", 9999), x + 12, y + 30);
            }

            else if(myHealth < 9999 && myHealth > 99)
            {
                g2.drawString(String.format("%d", myHealth), x + 12, y + 30);
            }

            else if(myHealth < 100)
            {
                g2.drawString(String.format("%d", myHealth), x + 18, y + 30);
            }

            //Health is less than 999
            else
            {
                g2.drawString(String.format("%d", myHealth), x + 14, y + 30);
            }
    }

    private void setCoords()
    {
        x = currentLocation.getX() * 50;
        y = currentLocation.getY() * 50;

        dirChanged = (currentDir != lastDir);

        switch (!dirChanged ? currentDir : lastDir)
        {
            case UP:
            {
                y += 25;
                break;
            }
            case RIGHT:
            {
                x -= 25;
                break;
            }
            case DOWN:
            {
                y -= 25;
                break;
            }
            default:
            {
                x += 25;
                break;
            }
        }
    }

    public void move()
    {
        if (isAlive)
        {
            Path nextLocation = currentLocation.getMyNext();

            //This will change the location of the enemy based on current location
            if (moveTicker < 9)
            {
                //This uses the current location and check if its null
                if(moveTicker == 4 && nextLocation == null)
                {
                    isAlive = false;
                }
                else
                {
                    if (dirChanged && moveTicker > 4)
                    {
                        dirChanged = false;
                    }

                    switch (!dirChanged ? currentDir : lastDir)
                    {
                        case UP:
                        {
                            y -= 5;
                            break;
                        }
                        case RIGHT:
                        {
                            x += 5;
                            break;
                        }
                        case DOWN:
                        {
                            y += 5;
                            break;
                        }
                        default:
                        {
                            x -= 5;
                            break;
                        }
                    }

                    moveTicker++;
                }
            }
            else
            {
                currentLocation = nextLocation;
                lastDir = currentDir;
                if (currentLocation.getMyDirection() != null)
                {
                    currentDir = currentLocation.getMyDirection();
                }
                setCoords();

                moveTicker = 0;
            }
        }
    }

    public void decrementHealth(int x)
    {
        if (x < myHealth)
        {
            myHealth -= x;
        }
        else
        {
            myHealth = 0;
            isAlive = false;
        }
    }
}