package Coordinator;

import java.io.*;
import java.util.*;

public class FileUtil {

    // Read CSV file
    public static java.util.List<String[]> readCSV(String filename){
        java.util.List<String[]> data = new ArrayList<>();
        File file = new File(filename);
        if(!file.exists()) return data;

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                data.add(line.split(",", -1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    // Write line to file (append by default)
    public static void writeLine(String filename, String line){
        writeLine(filename,line,true);
    }

    public static void writeLine(String filename, String line, boolean append){
        File file = new File(filename);
        file.getParentFile().mkdirs(); // create folder if missing
        try(PrintWriter pw = new PrintWriter(new FileWriter(file, append))){
            pw.println(line);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
