package labtrack.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static void write(String file, String data) {
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            String lineToWrite = data + "\n";
            fileWriter.write(lineToWrite);
        } catch (IOException e) {
            System.out.println("File write error");
        }
    }

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