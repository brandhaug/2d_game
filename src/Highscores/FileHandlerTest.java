package Highscores;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by stgr99 on 14.05.2018.
 */
public class FileHandlerTest {

    private FileHandler fileHandler = FileHandler.getInstance();

    @Test
    public void writeAndReadBytes() {
        File testFile = new File("testFile.txt");

        byte[] bytes = {1, 2, 3, 4, 5};
        try {
            testFile.createNewFile();
            fileHandler.writeBytesToFile(testFile, bytes);
            byte[] bytesFromReading = fileHandler.readBytesFromFile(testFile);
            Files.delete(testFile.toPath());
            assertTrue(Arrays.equals(bytes, bytesFromReading));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProgressFile() {
        File progressFile = new File("progress.txt");
        if (progressFile.exists()) {
            short progressNumber = fileHandler.getProgress();
            assertTrue(progressNumber > 0 && progressNumber < 7);
        }
    }

    @Test
    public void testEncryptionDecryption() {
        File testFile = new File("testFile.txt");

        try {
            testFile.createNewFile();
            List<String> fileContent = new ArrayList<>();
            fileContent.add("This text should contain the word JUnit");
            Files.write(testFile.toPath(), fileContent, StandardCharsets.ISO_8859_1);

            fileHandler.encryptFile(testFile.toPath());
            List<String> contentRead = Files.readAllLines(testFile.toPath(), StandardCharsets.ISO_8859_1);
            assertFalse(contentRead.get(0).contains("JUnit"));

            fileHandler.decryptFile(testFile.toPath());
            contentRead = Files.readAllLines(testFile.toPath(), StandardCharsets.ISO_8859_1);
            assertTrue(contentRead.get(contentRead.size() - 1).contains("JUnit"));

            Files.delete(testFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetObjectAmountFromLine() {
        String testString = "time=10,coins=56";
        assertEquals(56, fileHandler.getObjectAmountFromLine(testString));
    }

    @Test
    public void testGetTimeFromLine() {
        String testString = "time=10,coins=56";
        assertEquals(10, fileHandler.getTimeFromLine(testString));
    }

}
