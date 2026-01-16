package Student;

import java.io.*;
import java.util.*;

public class FileUtil {

    // ===================== READ CSV (WITH QUOTES SUPPORT) =====================
    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return data;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(parseCSVLine(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // ===================== CSV LINE PARSER =====================
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // toggle
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString().trim());

        // Remove surrounding quotes
        for (int i = 0; i < result.size(); i++) {
            String s = result.get(i);
            if (s.startsWith("\"") && s.endsWith("\"")) {
                result.set(i, s.substring(1, s.length() - 1));
            }
        }

        return result.toArray(new String[0]);
    }

    // ===================== APPEND ONE LINE =====================
    public static void writeLine(String filePath, String line) {
        try {
            File file = new File(filePath);

            // If file exists and is not empty, ensure it ends with a newline
            if (file.exists() && file.length() > 0) {
                try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                    raf.seek(file.length() - 1);
                    if (raf.readByte() != '\n') {
                        try (FileWriter fw = new FileWriter(file, true)) {
                            fw.write(System.lineSeparator());
                        }
                    }
                }
            }

            // Append the new line
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===================== OVERWRITE FILE =====================
    public static void writeAll(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
