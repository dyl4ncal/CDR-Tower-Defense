/**
 *
 * @author Colton
 */

package entities;

public class Path extends Tile
{
    private Path myNext;

    public Path(int x, int y, Path next)
    {
        super(x, y);
        myNext = next;
    }

    public Path getMyNext()
    {
        return myNext;
    }
}
