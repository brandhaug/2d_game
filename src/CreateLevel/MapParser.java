package CreateLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: Lag egne exceptions
public class MapParser {

    static char[][] map;

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
