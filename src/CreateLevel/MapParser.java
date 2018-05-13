package CreateLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapParser {

    private char[][] map;

    /**
     * Gets the array from a map file, by collecting the height and the width of the map, as well as its content.
     * @param file the map
     * @return map
     */
    public char[][] getArrayFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            int index = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (index == 0) {
                    int mapHeight = getValueFromMapHeader(line, "height");
                    int mapWidth = getValueFromMapHeader(line, "width");
                    map = new char[mapHeight][mapWidth];
                } else {
                    line = line.replaceAll("\\s", "");
                    char[] lineArr = line.toCharArray();
                    map[index - 1] = lineArr;
                }

                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Gets value from the map given by the string passed in. Could be 'height' of 'width'.
     * @param file the map name
     * @param name the value name
     * @return value
     */
    public int getValueFromFileHeader(File file, String name) {
        Scanner scanner;
        int value = 0;

        try {
            scanner = new Scanner(file);
            String line = scanner.nextLine();
            value = getValueFromMapHeader(line, name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Gets the value from the header of the map, by using regex.
     * Returns -1 if the value could not be found.
     * @param header the first line in the map file
     * @param name the value name
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
}
