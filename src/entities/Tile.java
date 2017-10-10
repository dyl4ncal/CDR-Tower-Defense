package entities;

/**
 * This is a base class for defining the x & y
 * coordinates of locations on the map.
 * 
 * @author Dylan
 */
public class Tile
{
    private int xCoordinate;
    private int yCoordinate;
    
    public Tile(){}
    
    public void setXCoordinate(int x)
    {
        this.xCoordinate = x;
    }
    
    public void setYCoordinate(int y)
    {
        this.yCoordinate = y;
    }
    
    public int getXCoordinate()
    {
        return this.xCoordinate;
    }
    
    public int getYCoordinate()
    {
        return this.yCoordinate;
    }
}
