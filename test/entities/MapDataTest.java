/**
 * JUnit: 11 tests for MapData.
 * Each tests that an exception is thrown given a specific problem with the input file.
 * The map is a 2x2
 *
 * |------|
 * | SR D |
 * | O  E |
 * |------|
 *
 */

package entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MapDataTest
{
    @Test
    public void constructorTest1() throws IOException //Example: Start isn't given a direction.
    {
        Executable closure = () -> getTestData("S D\nO E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest2() throws IOException //Example: Path not connected.
    {
        Executable closure = () -> getTestData("SR R\nO E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest3() throws IOException //Example: 2 starts.
    {
        Executable closure = () -> getTestData("SR D\nSR E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest4() throws IOException //Example: 2 ends.
    {
        Executable closure = () -> getTestData("SR D\nE E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest5() throws IOException //Example: No start.
    {
        Executable closure = () -> getTestData("O D\nO E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest6() throws IOException //Example: No end.
    {
        Executable closure = () -> getTestData("SR D\nO D");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest7() throws IOException //Example: Start has invalid direction.
    {
        Executable closure = () -> getTestData("SA D\nO E");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest8() throws IOException //Example: 2 char token that isn't start.
    {
        Executable closure = () -> getTestData("SR D\nAA D");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest9() throws IOException //Example: More than 2 char token.
    {
        Executable closure = () -> getTestData("SR D\nAAA D");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest10() throws IOException //Example: Unequal rows.
    {
        Executable closure = () -> getTestData("SR D\nO D O");
        assertThrows(IllegalArgumentException.class, closure);
    }

    @Test
    public void constructorTest11() throws IOException //Example: Empty file.
    {
        Executable closure = () -> getTestData("");
        assertThrows(IllegalArgumentException.class, closure);
    }

    private MapData getTestData(String s) throws IOException
    {
        File f = new File("testData.txt");
        FileWriter wr = new FileWriter(f);
        wr.write(s);
        wr.flush();
        wr.close();
        return new MapData("testData.txt");
    }
}