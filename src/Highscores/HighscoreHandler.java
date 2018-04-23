package Highscores;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by stgr99 on 07.03.2018.
 */
public class HighscoreHandler {

    private final static String FILE_PATH = "highscore.txt";
    private final static String FILE_PATH_SurvivalInfo = "survivalInfo.txt";
    private final static short NUMBER_OF_PLACEMENTS = 3;
    private Path path;
    private Path survivalPath;

    public HighscoreHandler() {
        createFileIfNotExists();
        path = Paths.get(FILE_PATH);
        survivalPath = Paths.get(FILE_PATH_SurvivalInfo);
    }

    public void addToHighscore(String mapName, int time, int coins) {
        if (!mapExistsInFile(mapName)) {
            addFirstPlacement(mapName, time, coins);
        } else {
            addNewPlacement(mapName, time, coins);
            deleteOverload();
        }
    }

    private void deleteOverload() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            int position = 0;
            String line = reader.readLine();

            while (line != null) {
                if (line.contains("map=")) {
                    for (int i = 0; i < NUMBER_OF_PLACEMENTS; i++) {
                        reader.readLine();
                        position++;
                    }

                    line = reader.readLine();
                    position++;
                    if (line != null && !line.contains("map=")) {
                        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        lines.remove(position);
                        Files.write(path, lines, StandardCharsets.UTF_8);
                        break;
                    }
                }
                position++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSurvivalInfo(int gamePoints){
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(survivalPath, StandardCharsets.UTF_8));
            int currentPoints = Integer.parseInt(fileContent.get(0));
            int newPoints = currentPoints + gamePoints;
            fileContent.set(0,Integer.toString(newPoints));
            Files.write(survivalPath,fileContent, StandardCharsets.UTF_8);
        }catch (IOException e) {
          e.printStackTrace();
        }
    }

    private void addNewPlacement(String mapName, int time, int objectAmount) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            int position = 0;
            String line = reader.readLine();

            while (line != null) {
                if (line.replaceAll("map=", "").equals(mapName)) {

                    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

                    for (int i = 0; i < NUMBER_OF_PLACEMENTS; i++) {
                        line = reader.readLine();
                        position++;

                        if (line == null || line.contains("map=")) {
                            if(mapName.matches(".*survival.*")){
                                lines.add(position, "time=" + time + ",kills=" + objectAmount);
                            }else{
                                lines.add(position, "time=" + time + ",coins=" + objectAmount);
                            }
                            Files.write(path, lines, StandardCharsets.UTF_8);
                            break;
                        } else {
                            int placementObjects = getObjectAmountFromLine(line);
                            int placementTime = getTimeFromLine(line);
                            if (mapName.matches(".*survival.*")) {
                                if (objectAmount > placementObjects) {
                                    lines.add(position, "time=" + time + ",kills=" + objectAmount);
                                    Files.write(path, lines, StandardCharsets.UTF_8);
                                    break;
                                } else if (objectAmount == placementObjects) {
                                    if (time < placementTime) {
                                        lines.add(position - 1, "time=" + time + ",kills=" + objectAmount);
                                        Files.write(path, lines, StandardCharsets.UTF_8);
                                        break;
                                    }
                                }
                            } else {
                                if (time < placementTime) {
                                    lines.add(position, "time=" + time + ",coins=" + objectAmount);
                                    Files.write(path, lines, StandardCharsets.UTF_8);
                                    break;
                                } else if (time == placementTime) {
                                    if (objectAmount > placementObjects) {
                                        lines.add(position - 1, "time=" + time + ",coins=" + objectAmount);
                                        Files.write(path, lines, StandardCharsets.UTF_8);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
                line = reader.readLine();
                position++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFirstPlacement(String mapName, int time, int objects) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.add(lines.size(), "map=" + mapName);

            if (mapName.matches(".*survival.*")) {
                lines.add(lines.size(), "time=" + time + ",kills=" + objects);
            }else{
                lines.add(lines.size(), "time=" + time + ",coins=" + objects);
            }

            Files.write(path, lines, StandardCharsets.UTF_8);

            /*FileWriter fw = new FileWriter(FILE_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            out.println("map=" + mapName);
            out.println("time=" + time + ",coins=" + coins);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewHighscore(String mapName, int time, int coins) {
        return !mapExistsInFile(mapName) || isBetterPlacement(mapName, time, coins);
    }

    private boolean isBetterPlacement(String mapName, int time, int objects) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line = reader.readLine();

            while (line != null) {
                if (line.replaceAll("map=", "").equals(mapName)) {
                    for (int i = 0; i < NUMBER_OF_PLACEMENTS; i++) {
                        line = reader.readLine();
                        if (line == null) {
                            return true;
                        } else {
                            int placementTime = getTimeFromLine(line);
                            int placementObjects = getObjectAmountFromLine(line);
                            if (mapName.matches(".*survival.*")) {
                                if(objects > placementObjects){
                                    return true;
                                }else if(objects == placementObjects){
                                    if(time < placementTime){
                                        return true;
                                    }
                                }
                            } else {
                                if (time < placementTime) {
                                    return true;
                                } else if (time == placementTime) {
                                    if (objects > placementObjects) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    line = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean mapExistsInFile(String mapName) {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.anyMatch(l -> l.replaceAll("map=", "").equals(mapName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createFileIfNotExists() {
        try {
            File file = new File(FILE_PATH);
            file.createNewFile();
            File fileSurvivalScore = new File(FILE_PATH_SurvivalInfo);
            fileSurvivalScore.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTimeFromLine(String line) {
        String timeString = line.replaceAll("time=", "");
        StringBuilder sb = new StringBuilder(timeString);
        int index = sb.indexOf(",");
        sb.delete(index, sb.length());
        timeString = sb.toString();
        return Integer.parseInt(timeString);
    }

    public int getObjectAmountFromLine(String line) {
        StringBuilder sb = new StringBuilder(line);
        int index = sb.lastIndexOf("=");
        String amountString = sb.substring(index + 1, sb.length());
        return Integer.parseInt(amountString);
    }

    public ArrayList<String> getArrayListFromFile() {
        ArrayList<String> list = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.forEach(list::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
