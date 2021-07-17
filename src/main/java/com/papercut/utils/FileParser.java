package com.papercut.utils;

import java.beans.Transient;
import java.util.List;

import com.papercut.model.PrintJob;
import com.papercut.exception.InvalidFileException;
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
     * @param filePath the path to read csv file.
     * @return List of parsed {@link PrintJob}.
     * @throws InvalidFileException if file is invalid. For instance, file not found, file is not CSV file, the row content is invalid, etc.
     */
    public List<PrintJob> parsePrintJobFromCsv(String filePath) throws InvalidFileException {
        return null;
    }
}
