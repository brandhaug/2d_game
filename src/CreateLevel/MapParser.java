package CreateLevel;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapParser {

    private char[][] map;

    /**
     * Gets the array from a map file, by collecting the height and the width of the map, as well as its content.
     *
     * @param inputStream the map
     * @return map
     */
    public char[][] getArrayFromInputStream(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        int index = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (index == 0) {
                int mapHeight = getValueFromMapHeader(line, "height");
                int mapWidth = getValueFromMapHeader(line, "width");

                if (mapHeight == -1 || mapWidth == -1) {
                    return null;
                }
                map = new char[mapHeight][mapWidth];
            } else {
                line = line.replaceAll("\\s", "");
                char[] lineArr = line.toCharArray();
                map[index - 1] = lineArr;
            }

            index++;
        }
        return map;
    }

    /**
     * Gets value from the map given by the string passed in. Could be 'height' of 'width'.
     *
     * @param inputStream the map name
     * @param name        the value name
     * @return value
     */
    public int getValueFromInputStreamHeader(InputStream inputStream, String name) {
        Scanner scanner;
        int value = 0;

        scanner = new Scanner(inputStream);
        String line = scanner.nextLine();
        value = getValueFromMapHeader(line, name);

        return value;
    }

    /**
     * Gets the value from the header of the map, by using regex.
     * Returns -1 if the value could not be found.
     *
     * @param header the first line in the map file
     * @param name   the value name
     * @return value, -1 if not found.
     */
    private int getValueFromMapHeader(String header, String name) {
        String reg = "(?<=" + name + ": )[0-9]+";

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return -1;
    }

    /**
     * Validates map in filePath
     *
     * @param filePath
     * @return false if invalid, true if valid
     */
    public boolean validateInputStreamMap(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));

            Scanner scanner = new Scanner(inputStream);
            int index = 0;
            int mapHeight = 0;
            int mapWidth = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (index == 0) {
                    mapHeight = getValueFromMapHeader(line, "height");
                    mapWidth = getValueFromMapHeader(line, "width");

                    if (mapHeight == -1 || mapWidth == -1) {
                        return false;
                    }

                    map = new char[mapHeight][mapWidth];
                } else {
                    line = line.replaceAll("\\s", "");
                    char[] lineArr = line.toCharArray();

                    if (lineArr.length != mapWidth) {
                        return false;
                    } else {
                        map[index - 1] = lineArr;
                    }
                }

                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
