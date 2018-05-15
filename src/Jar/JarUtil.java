package Jar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by stgr99 on 15.05.2018.
 */
public class JarUtil {

    /**
     * Get the file from filename through JAR. Returns null if not running with JAR.
     * @param fileName the filepath
     * @return File if running with JAR, null if not.
     */
    public File getFileFromJar(String fileName) {
        File file = null;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if (jarFile.isFile()) {
            try {
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String filePath = entries.nextElement().getName();

                    if (filePath.equals(fileName)) { //filter according to the path
                        file = new File(filePath);
                        InputStream inputStream = getClass().getResourceAsStream("/" + filePath);
                        FileUtils.copyInputStreamToFile(inputStream, file);
                    }
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Gets the JAR File in project
     * @return File
     */
    public File getJarFile() {
        return new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    }
}
