package labtrack.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static void write(String file, String data) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(data + "\n");
        } catch (IOException e) {
            System.out.println("File write error");
        }
    }

    public static List<String> readAllLines(String file) {
        List<String> lines = new ArrayList<>();
        File f = new File(file);

        if (!f.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("File read error");
        }
        return lines;
    }

    public static void overwrite(String file, List<String> lines) {
        try (FileWriter fw = new FileWriter(file, false)) {
            for (String line : lines) {
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("File overwrite error");
        }
    }
}