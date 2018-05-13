package CreateLevel;


import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MapParserTest {
    @Test
    public void testGetValueFromFileHeader() {
        try {
            File tempFile = File.createTempFile("temp", "txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write("width: 30 height: 40 level: 2");
            bw.close();

            MapParser mapParser = new MapParser();
            int width = mapParser.getValueFromFileHeader(tempFile, "width");
            int height = mapParser.getValueFromFileHeader(tempFile, "height");
            int level = mapParser.getValueFromFileHeader(tempFile, "level");

            assertEquals(30, width);
            assertEquals(40, height);
            assertEquals(2, level);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
