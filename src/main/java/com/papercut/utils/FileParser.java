package com.papercut.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.papercut.exception.InvalidFileException;
import com.papercut.model.PrintJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * This class provide functions to read file and parse each row of the file.
 */
@Component
public class FileParser {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    /**
     * Parse the CSV file from the provided file path and return a List of {@link PrintJob}.
     *
     * @param filePath the path to read csv file.
     * @return List of parsed {@link PrintJob}.
     * @throws InvalidFileException if file is invalid. For instance, file not found, file is not CSV file, the row content is invalid, etc.
     */
    public List<PrintJob> parsePrintJobFromCsv(String filePath) throws InvalidFileException, IOException {
        File file = getFile(filePath);
        List<PrintJob> printJobs = new ArrayList<>();
        long rowNumber = 1;
        try (FileInputStream inputStream = new FileInputStream(file); Scanner sc = new Scanner(inputStream, "UTF-8")) {
            while (sc.hasNextLine()) {
                //if has the header, skip the header and start from the next row.
                if (rowNumber == 1 && env.getProperty("csv.contains.header", Boolean.class, true)) {
                    sc.nextLine();
                } else {
                    String line = sc.nextLine();
                    PrintJob printJob = new PrintJob(line);
                    printJobs.add(printJob);
                }
                rowNumber++;
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (Exception e) {
            throw new InvalidFileException(String.format("Calculation Failed - line %d of the csv file %s is invalid, please check the content again.", rowNumber, filePath), e);
        }
        return printJobs;
    }

    private File getFile(String filePath) throws InvalidFileException, IOException {
        //Ideally we should check the file mime type, but I think it's fine not to do so as we will check file postfix and then check each row to ensure the uploaded file can be parsed properly.
        if (filePath != null && !filePath.trim().isEmpty() && filePath.toLowerCase().endsWith(".csv")) {
            File file = new File(filePath);
            if (file.exists()) {
                float fileSizeMB = Files.size(file.toPath()) / 1000000f;
                int maxFileSizeMB = env.getProperty("csv.max.size.mb", Integer.class, 50);
                if (fileSizeMB > maxFileSizeMB) {
                    throw new InvalidFileException(String.format("Invalid File %s: file size is %s MB which exceeds the limit %d MB.", filePath, fileSizeMB, maxFileSizeMB));
                } else {
                    return file;
                }
            } else {
                throw new InvalidFileException(String.format("Invalid File: file not exists in filepath %s. Please insure the file path is correct.", filePath));
            }
        } else {
            throw new InvalidFileException(String.format("Invalid Input: file path is %s. Please insure the file path is referring to a correct csv file.", filePath));
        }
    }
}
