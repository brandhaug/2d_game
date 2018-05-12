package Highscores;


import CreateLevel.MapParser;
import Game.GameController;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by stgr99 on 07.03.2018.
 */
public class FileHandler {

    private final static String HIGH_SCORE_PATH = "highscore.txt";
    private final static String KEY_PATH = "key.txt";
    private final static String FILE_PATH_SURVIVAL = "survivalInfo.txt";
    private final static String PROGRESS_PATH = "progress.txt";
    private final static short NUMBER_OF_PLACEMENTS = 3;
    private Path highScorePath;
    private Path keyPath;
    private Path survivalPath;
    private Path progressPath;
    private SecretKey secretKey;
    private Cipher cipher;
    private String errorLabel = "";
    private static FileHandler instance;

    /**
     * Gets the instance of the class using Singleton.
     */
    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    /**
     * Sets secret key and cipher.
     * Sets all path variables.
     * Creates all necessary files that don't exists
     */
    private FileHandler() {
        try {
            secretKey = KeyGenerator.getInstance("DES").generateKey();
            cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        highScorePath = Paths.get(HIGH_SCORE_PATH);
        keyPath = Paths.get(KEY_PATH);
        survivalPath = Paths.get(FILE_PATH_SURVIVAL);
        progressPath = Paths.get(PROGRESS_PATH);
        createFilesIfNotExists();
    }

    /**
     * Adds a new score to the high score file. If the map does not exist, adds automatically new first place.
     * If not, place new high score accordingly.
     *
     * @param mapName the name of the map
     * @param time    the time spent
     * @param coins   the coins collected
     */
    public void addToHighScore(String mapName, int time, int coins) {
        decryptFile(highScorePath);

        if (!mapExistsInFile(mapName)) {
            addFirstPlacement(mapName, time, coins);
        } else {
            addNewPlacement(mapName, time, coins);
            deleteOverload();
        }

        encryptFile(highScorePath);
    }

    /**
     * Deletes every score in the high score files that is not top 3.
     */
    private void deleteOverload() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_PATH));
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
                        List<String> lines = Files.readAllLines(highScorePath, StandardCharsets.ISO_8859_1);
                        lines.remove(position);
                        Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
                        break;
                    }
                } else {
                    position++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds game points to the survival file
     *
     * @param gamePoints game points collected from session
     */
    public void addSurvivalInfo(int gamePoints) {
        try {
            decryptFile(survivalPath);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(survivalPath, StandardCharsets.ISO_8859_1));
            int currentPoints = Integer.parseInt(fileContent.get(0));
            int newPoints = currentPoints + gamePoints;
            fileContent.set(0, Integer.toString(newPoints));
            Files.write(survivalPath, fileContent, StandardCharsets.ISO_8859_1);
            encryptFile(survivalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new placement to the high score file, depending on time and object amount.
     * Finds correct placement between other placements.
     *
     * @param mapName      the name of the map
     * @param time         the time spent
     * @param objectAmount the object amount collected. Could be coins or enemies killed.
     */
    private void addNewPlacement(String mapName, int time, int objectAmount) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_PATH));
            int position = 0;
            String line = reader.readLine();

            while (line != null) {
                if (line.replaceAll("map=", "").equals(mapName)) {

                    List<String> lines = Files.readAllLines(highScorePath, StandardCharsets.ISO_8859_1);

                    for (int i = 0; i < NUMBER_OF_PLACEMENTS; i++) {
                        line = reader.readLine();
                        position++;

                        if (line == null || line.contains("map=")) {
                            if (mapName.matches(".*survival.*")) {
                                lines.add(position, "time=" + time + ",kills=" + objectAmount);
                            } else {
                                lines.add(position, "time=" + time + ",coins=" + objectAmount);
                            }
                            Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
                            break;
                        } else {
                            int placementObjects = getObjectAmountFromLine(line);
                            int placementTime = getTimeFromLine(line);
                            if (mapName.matches(".*survival.*")) {
                                if (objectAmount > placementObjects) {
                                    lines.add(position, "time=" + time + ",kills=" + objectAmount);
                                    Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
                                    break;
                                } else if (objectAmount == placementObjects) {
                                    if (time < placementTime) {
                                        lines.add(position, "time=" + time + ",kills=" + objectAmount);
                                        Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
                                        break;
                                    }
                                }
                            } else {
                                if (time < placementTime) {
                                    lines.add(position, "time=" + time + ",coins=" + objectAmount);
                                    Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
                                    break;
                                } else if (time == placementTime) {
                                    if (objectAmount > placementObjects) {
                                        lines.add(position - 1, "time=" + time + ",coins=" + objectAmount);
                                        Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
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

    /**
     * Adds a new first placement to the high score file, if map is succeeded for the first time.
     *
     * @param mapName the name of the map
     * @param time    the time spent
     * @param objects the object amount collected
     */
    private void addFirstPlacement(String mapName, int time, int objects) {
        try {
            List<String> lines = Files.readAllLines(highScorePath, StandardCharsets.ISO_8859_1);
            lines.add(lines.size(), "map=" + mapName);

            if (mapName.matches(".*survival.*")) {
                lines.add(lines.size(), "time=" + time + ",kills=" + objects);
            } else {
                lines.add(lines.size(), "time=" + time + ",coins=" + objects);
            }

            Files.write(highScorePath, lines, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the current score is a new high score, and returns true or false accordingly.
     *
     * @param mapName the name of the map
     * @param time    the time spent
     * @param coins   the coins collected
     * @return true if it's a new high score, false if not.
     */
    public boolean isNewHighScore(String mapName, int time, int coins) {
        decryptFile(highScorePath);
        if (!mapExistsInFile(mapName) || isBetterPlacement(mapName, time, coins)) {
            return true;
        }
        encryptFile(highScorePath);
        return false;
    }

    /**
     * Given that the current map exists in the high score file, checks if it's a better placement than earlier.
     *
     * @param mapName the name of the map
     * @param time    the time spent
     * @param objects the object amount collected
     * @return true if it's a better placement, false if not.
     */
    private boolean isBetterPlacement(String mapName, int time, int objects) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_PATH));
            String line = reader.readLine();

            while (line != null) {
                if (line.replaceAll("map=", "").equals(mapName)) {
                    for (int i = 0; i < NUMBER_OF_PLACEMENTS; i++) {
                        line = reader.readLine();
                        if (line == null || line.startsWith("map=")) {
                            return true;
                        } else {
                            int placementTime = getTimeFromLine(line);
                            int placementObjects = getObjectAmountFromLine(line);
                            if (mapName.matches(".*survival.*")) {
                                if (objects > placementObjects) {
                                    return true;
                                } else if (objects == placementObjects) {
                                    if (time < placementTime) {
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

    /**
     * Checks if current map played exists in high score file or not
     * @param mapName the name of the map
     * @return true if map exists in high score file, false if not
     */
    private boolean mapExistsInFile(String mapName) {
        try (Stream<String> lines = Files.lines(highScorePath, StandardCharsets.ISO_8859_1)) {
            return lines.anyMatch(l -> l.replaceAll("map=", "").equals(mapName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates all necessary files, if they don't exist. Encrypts created files.
     */
    private void createFilesIfNotExists() {
        try {
            File file = new File(HIGH_SCORE_PATH);
            boolean fileCreated = file.createNewFile();

            File keyFile = new File(KEY_PATH);
            boolean keyFileCreated = keyFile.createNewFile();

            File fileSurvivalScore = new File(FILE_PATH_SURVIVAL);
            boolean survivalFileCreated = fileSurvivalScore.createNewFile();

            File progressFile = new File(PROGRESS_PATH);
            boolean progressFileCreated = progressFile.createNewFile();

            if (progressFileCreated) {
                setProgressFileContent();
                if (!keyFileCreated) {
                    getSecretKeyFromFile();
                }
                encryptFile(progressPath);
            }

            if (keyFileCreated) {
                writeBytesToFile(keyPath.toFile(), secretKey.getEncoded());
            } else {
                getSecretKeyFromFile();
            }

            if (fileCreated) {
                if (!keyFileCreated) {
                    getSecretKeyFromFile();
                }
                encryptFile(highScorePath);
            }

            if (survivalFileCreated) {
                setSurvivalFileContent();
                if (!keyFileCreated) {
                    getSecretKeyFromFile();
                }
                encryptFile(survivalPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the standard content of the survival file. User starts with 10 kill points, and no bullets available.
     */
    private void setSurvivalFileContent() {
        try {
            List<String> lines = Files.readAllLines(survivalPath, StandardCharsets.ISO_8859_1);
            lines.add(lines.size(), "10");
            lines.add(lines.size(), "BULLET_A=false");
            lines.add(lines.size(), "BULLET_B=false");
            lines.add(lines.size(), "BULLET_C=false");

            Files.write(survivalPath, lines, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the secret key from file by reading its bytes.
     */
    private void getSecretKeyFromFile() {
        try {
            byte[] keyFromFile = readBytesFromFile(keyPath.toFile());
            secretKey = new SecretKeySpec(keyFromFile, 0, keyFromFile.length, "DES");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the time from a line in high score
     * @param line the line from high score file
     * @return time in int
     */
    int getTimeFromLine(String line) {
        String timeString = line.replaceAll("time=", "");
        StringBuilder sb = new StringBuilder(timeString);
        int index = sb.indexOf(",");
        sb.delete(index, sb.length());
        timeString = sb.toString();
        return Integer.parseInt(timeString);
    }

    /**
     * Gets the object amount from a line in high score
     * @param line the line from high score file
     * @return object amount in int
     */
    int getObjectAmountFromLine(String line) {
        StringBuilder sb = new StringBuilder(line);
        int index = sb.lastIndexOf("=");
        String amountString = sb.substring(index + 1, sb.length());
        return Integer.parseInt(amountString);
    }

    /**
     * Gets the array list from file given by path
     * @param filePath the file path
     * @return ArrayList of Strings
     * @see ArrayList
     * @see Path
     */
    public ArrayList<String> getArrayListFromFile(Path filePath) {
        ArrayList<String> list = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.ISO_8859_1)) {
            lines.forEach(list::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Encrypt the file given by the path, using the secret key stored in key.txt
     * @param filePath the file path
     */
    public void encryptFile(Path filePath) {
        try {
            ArrayList<String> list = getArrayListFromFile(filePath);
            byte[] text = list.toString().getBytes("ISO-8859-1");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            writeBytesToFile(filePath.toFile(), cipher.doFinal(text));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            errorLabel = "There seems to be something wrong. Please delete 'key.txt', 'progress.txt' and 'survivalInfo.txt'. All progress will be lost.";
        }
    }

    /**
     * Decrypts the file given by the path, using the secret key stored in key.txt
     * @param filePath the file path
     */
    public void decryptFile(Path filePath) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] textDecrypted = cipher.doFinal(readBytesFromFile(filePath.toFile()));

            if (filePath == this.highScorePath) {
                writeDecryptedTextToFile(HIGH_SCORE_PATH, new String(textDecrypted));
            } else if (filePath == this.survivalPath) {
                writeDecryptedTextToFile(FILE_PATH_SURVIVAL, new String(textDecrypted));
            } else if (filePath == this.progressPath) {
                writeDecryptedTextToFile(PROGRESS_PATH, new String(textDecrypted));
            }

        } catch (InvalidKeyException | IOException e) {
            errorLabel = "There seems to be something wrong. Please delete 'key.txt', 'progress.txt' and 'survivalInfo.txt'. All progress will be lost.";
        } catch (BadPaddingException | IllegalBlockSizeException ignored) {

        }
    }

    /**
     * Writes the decrypted text to file in a readable format
     * @param filePath the path of the file
     * @param s the decrypted string
     */
    private void writeDecryptedTextToFile(String filePath, String s) {
        try {
            PrintWriter printWriter = new PrintWriter(filePath, "ISO-8859-1");
            s = s.replaceAll(", ", " ");
            if (s.length() > 1) {
                s = s.substring(1, s.length() - 1);
            }

            StringBuilder line = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != ' ') {
                    line.append(s.charAt(i));
                } else {
                    printWriter.println(line);
                    line.setLength(0);
                }
            }
            if (!line.toString().equals("")) {
                printWriter.println(line);
            }

            printWriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and returns bytes from a file
     * @param file the file
     * @return a byte array read from the file
     * @throws IOException
     */
    private byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" +
                    length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * Writes bytes to file
     * @param theFile the file
     * @param bytes byte array to be written to file
     * @throws IOException
     */
    private void writeBytesToFile(File theFile, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(theFile);
        try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        }
    }

    /**
     * Resets all high scores
     */
    void resetHighScores() {
        try {
            Files.delete(highScorePath);
            Files.delete(keyPath);
            createFilesIfNotExists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the level progress by writing to file. Opens a new level if making the hardest map available.
     */
    public void setProgress() {
        try {
            decryptFile(progressPath);
            List<String> lines = Files.readAllLines(progressPath, StandardCharsets.ISO_8859_1);
            int progress = Integer.parseInt(lines.get(0));

            File map = new File("src/Resources/maps/" + GameController.mapName);
            int levelProgress = MapParser.getValueFromFile(map, "level");

            if (levelProgress == progress) {
                lines.add(0, Integer.toString(progress + 1));
                Files.write(progressPath, lines, StandardCharsets.ISO_8859_1);
            }
            encryptFile(progressPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the progress from file.
     * @return int which represents the level progress
     */
    public short getProgress() {
        try {
            decryptFile(progressPath);
            List<String> lines = Files.readAllLines(progressPath, StandardCharsets.ISO_8859_1);
            encryptFile(progressPath);
            return Short.parseShort(lines.get(0));
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * Sets the progress file content, if it's being created
     */
    private void setProgressFileContent() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("progress.txt"), StandardCharsets.ISO_8859_1);
            lines.add(lines.size(), "1");
            Files.write(Paths.get("progress.txt"), lines, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the high score path
     * @return highScorePath
     */
    Path getHighScorePath() {
        return highScorePath;
    }

    /**
     * Gets the survival path
     * @return survivalPath
     */
    public Path getSurvivalPath() {
        return survivalPath;
    }

    /**
     * Gets the error label
     * @return errorLabel
     */
    public String getErrorLabel() {
        return errorLabel;
    }
}
