package CreateLevel;


import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class MapParserTest {
    @Test
    public void testCorrectGetValueFromFileHeader() {
        try {
            File tempFile = File.createTempFile("temp", "txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write("width: 30 height: 40 level: 2");
            bw.close();

            InputStream inputStream = new FileInputStream(tempFile);

            MapParser mapParser = new MapParser();
            int width = mapParser.getValueFromInputStreamHeader(inputStream, "width");
            int height = mapParser.getValueFromInputStreamHeader(inputStream, "height");
            int level = mapParser.getValueFromInputStreamHeader(inputStream, "level");

            assertEquals(30, width);
            assertEquals(40, height);
            assertEquals(2, level);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrongGetValueFromFileHeader() {
        try {
            File tempFile = File.createTempFile("temp", "txt");

            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write("width30 heiht: 40 level 2");
            bw.close();

            InputStream inputStream = new FileInputStream(tempFile);

            MapParser mapParser = new MapParser();
            int width = mapParser.getValueFromInputStreamHeader(inputStream, "width");
            int height = mapParser.getValueFromInputStreamHeader(inputStream, "height");
            int level = mapParser.getValueFromInputStreamHeader(inputStream, "level");

            assertEquals(-1, width);
            assertEquals(-1, height);
            assertEquals(-1, level);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
