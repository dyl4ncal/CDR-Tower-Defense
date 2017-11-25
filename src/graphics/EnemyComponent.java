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
import java.lang.Math;

import entities.Path;
import music.DeathSound;

public class EnemyComponent extends JComponent
{   
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    private DeathSound oof;
    private int x = 0;
    private int y = 0;
    private int moveTicker = 0;
    private int myHealth = 0;
    private Path.Direction lastDir;
    private Path.Direction currentDir;
    private boolean dirChanged;
    private boolean isAlive = true;
    private Path currentLocation;
    private String myType;
    private float hue = 0;
    private float saturation = 1;
    private float brightness = 1;
    
    public EnemyComponent(Path head, int h, String t)
    {
        myHealth = h;
        currentLocation = head;
        currentDir = currentLocation.getMyDirection();
        lastDir = currentDir;
        setCoords();
        myType = t;
        //Creates a 
        try
        {
            oof = new DeathSound();
        }
        catch(Exception e){}
    }

    @Override
    public int getX() {return x / 50;}
    @Override
    public int getY() {return y / 50;}
    public int getXPosition(){return x;}
    public int getYPosition(){return y;}
    
    public boolean isAlive() {return isAlive;}
    public String getType(){return myType;}

    public void draw(Graphics2D g2)
    {
        //As of right now, an enemy will be a circle in the center of the path
        Ellipse2D.Double e = new Ellipse2D.Double(x + 10, y + 10, WIDTH, HEIGHT);
        if(myType.equals("B"))
        {
            g2.setColor(new Color(Color.HSBtoRGB(hue, saturation, brightness)));
            g2.fill(e);
            g2.setColor(Color.BLACK);
            hue = (float) ((hue + Math.PI/100.0) % 1.0);
        }
        else
        {
            g2.setColor(Color.BLACK);
            g2.fill(e);
            g2.setColor(Color.CYAN);
        }

            if(myHealth < 10)
            {
                g2.drawString(String.format("%d", myHealth), x + 22, y + 30);
            }

            else if(myHealth > 9999)
            {
                g2.drawString(String.format("%d", 9999), x + 11, y + 30);
            }

            else if(myHealth > 999 && myHealth <= 9999)
            {
                g2.drawString(String.format("%d", myHealth), x + 11, y + 30);
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
            //Play sound file
            try
            {
                oof.play();
            }
            catch(Exception exp){}
        }
    }
}