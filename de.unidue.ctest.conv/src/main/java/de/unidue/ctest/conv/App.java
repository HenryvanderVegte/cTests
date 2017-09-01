package de.unidue.ctest.conv;

import java.io.IOException;
import java.util.List;

import de.unidue.ctest.read.Reader;
import de.unidue.ctest.util.CTest;
import de.unidue.ctest.write.Writer;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        Reader reader = new Reader();
        List<CTest> cTests = reader.getAllCTests();
        Writer writer = new Writer();
        writer.writeCTests(cTests);

    }
}
