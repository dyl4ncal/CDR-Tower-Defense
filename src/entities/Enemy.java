package entities;

import io.MapData;

public class Enemy
{
    private int x = 0;
    private int y = 0;
    private int moveTicker = 0;
    private Path.Direction lastDir;
    private Path.Direction currentDir;
    private boolean dirChanged;
    private boolean isAlive;
    private MapData mapData;
    private Path currentLocation;

    /*At the start, the enemy is given the head node/start tile.*/
    public Enemy(MapData md)
    {
        mapData = md;
        currentLocation = mapData.getHead();
        currentDir = currentLocation.getMyDirection();
        lastDir = currentDir;
        setCoords();
        isAlive = true;
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

    public int getX() {return x;}
    public int getY() {return y;}
    public boolean isAlive() {return isAlive;}

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
