/**
 * This class encapsulates graphical information for MapComponent (to draw enemies
 * and other data for manipulation by tower).
 */

package graphics;

import entities.Path;
import music.DeathSound;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EnemyComponent extends JComponent
{   
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private DeathSound oof;
    private Path currentLocation;
    private Path.Direction lastDir;
    private Path.Direction currentDir;
    private String myType;
    private boolean dirChanged;
    private boolean isAlive = true;
    private int x = 0;
    private int y = 0;
    private int moveTicker = 0;
    private int myHealth = 0;
    public static boolean sfxOn = true;
    private float hue = 0;
    private final float SATURATION = 1;
    private final float BRIGHTNESS = 1;
    
    public EnemyComponent(Path head, int h, String t)
    {
        myHealth = h;
        currentLocation = head;
        currentDir = currentLocation.getMyDirection();
        lastDir = currentDir;
        setCoords();
        myType = t;
        
        //Creates a sound on enemy death events. 
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
    public static void changeSFX()
    {
        if(sfxOn)
        {
            sfxOn = false;
        }
        else
        {
            sfxOn = true;
        }
    }

    //Logic for drawing enemies.
    public void draw(Graphics2D g2)
    {
        //An enemy is displayed as a circle in the center of the path.
        Ellipse2D.Double e = new Ellipse2D.Double(x + 10, y + 10, WIDTH, HEIGHT);
        if(myType.equals("B"))
        {
            g2.setColor(new Color(Color.HSBtoRGB(hue, SATURATION, BRIGHTNESS)));
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
            else if(myHealth < 100)
            {
                g2.drawString(String.format("%d", myHealth), x + 18, y + 30);
            }
            else if (myHealth < 1000)
            {
                g2.drawString(String.format("%d", myHealth), x + 14, y + 30);
            }
            else if(myHealth < 10000)
            {
                g2.drawString(String.format("%d", myHealth), x + 11, y + 30);
            }
            else
            {
                g2.drawString(String.format("%d", 9999), x + 11, y + 30);
            }
    }

    //Logic for enemy movement.
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

    //Logic to decrement an enemies health value.
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

            //Plays sound effects file.
            if(sfxOn)
            {
                try
                {
                    oof.play();
                }
                catch(Exception exp){}
            }
        }
    }

    //Sets an enemies coordinates.
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
}