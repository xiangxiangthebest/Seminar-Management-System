import java.io.*;
import java.util.*;

public class FileUtil {

    public static void writeLine(String file, String line) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSV(String file) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        } catch (IOException e) {
            // file may not exist yet
        }
        return list;
    }
}
