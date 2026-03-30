package labtrack.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Local file persistence utility for reading and writing data in CSV format.
 * Abstracts standard IO operations and ensures files exist before access.
 */
public class FileManager {

    /**
     * Appends a line of data to a file.
     * @param file The path to the file.
     * @param data The data string to append.
     */
    public static void write(String file, String data) {
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            String lineToWrite = data + "\n";
            fileWriter.write(lineToWrite);
        } catch (IOException e) {
            System.out.println("File write error");
        }
    }

    /**
     * Reads all non-empty lines from a file.
     * @param file The path to the file.
     * @return A list of strings, each representing a line in the file.
     */
    public static List<String> readAllLines(String file) {
        List<String> lines = new ArrayList<>();
        File targetFile = new File(file);

        if (!targetFile.exists()) {
            return lines;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(targetFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("File read error");
        }
        return lines;
    }

    /**
     * Overwrites a file with the provided list of lines.
     * @param file The path to the file.
     * @param lines The list of strings to write to the file.
     */
    public static void overwrite(String file, List<String> lines) {
        try (FileWriter fileWriter = new FileWriter(file, false)) {
            for (String line : lines) {
                String lineToWrite = line + "\n";
                fileWriter.write(lineToWrite);
            }
        } catch (IOException e) {
            System.out.println("File overwrite error");
        }
    }
}