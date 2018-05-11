package CreateLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: Lag egne exceptions
//TODO: Make not static
public class MapParser {

    static char[][] map;
    static int bulletAmount;

    public static char[][] getArrayFromFile(File file) {

        Scanner scanner;
        try {
            scanner = new Scanner(file);

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

    //TODO: Find a way to get bullet amount and return it in loadMap function.
    public static int getValueFromFile(File file, String name) {
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

    public static int getValueFromMapHeader(String header, String name) {
        String reg = "(?<=" + name + ": )[0-9]+";

        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(header);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return -1;
    }
}
