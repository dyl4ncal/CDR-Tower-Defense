/**
 *
 * @author Colton
 */

package entities;

public class Path extends Tile
{
    public enum Direction
    {
        UP, RIGHT, DOWN, LEFT;
    }

    private Path myNext;
    private Direction myDirection;

    public Path(int x, int y, Direction dir, Path next)
    {
        super(x, y);
        myDirection = dir;
        myNext = next;
    }

    @Override
    public TileType getTileType() {return TileType.PATH;}
    public Path getMyNext() {return myNext;}
    public Direction getMyDirection() {return myDirection;}
}

