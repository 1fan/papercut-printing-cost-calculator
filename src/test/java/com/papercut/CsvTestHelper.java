package com.papercut;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvTestHelper {

    public static void populateCsvFileWithContent(List<String> rows, String path) {
        try (PrintWriter writer = new PrintWriter(path)) {
            StringBuilder sb = new StringBuilder();
            for (String row : rows) {
                sb.append(row).append("\n");
            }
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
