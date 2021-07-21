package com.papercut;

import java.util.List;

import com.papercut.model.PrintJob;
import com.papercut.model.PrintJobSummary;
import com.papercut.service.CostCalculator;
import com.papercut.utils.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CostCalculatorCli implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FileParser fileParser;
    private CostCalculator calculator;

    @Autowired
    public void setFileParser(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    @Autowired
    public void setCalculator(CostCalculator calculator) {
        this.calculator = calculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(CostCalculatorCli.class, args).close();
    }

    /**
     * Read CSV files from the filepath provided by commandline arguments, calculate and print the cost details to the console.
     *
     * @param args - the filepath(s) of csv file(s) that need to be parsed.
     */
    @Override
    public void run(String... args) {
        if (args.length == 0) {
            System.out.println("No CSV Filepath is provided.");
            return;
        }

        for (String filePath : args) {
            try {
                List<PrintJob> jobs = fileParser.parsePrintJobFromCsv(filePath);
                PrintJobSummary summary = calculator.generatePrintJobSummary(jobs);
                System.out.printf("Print Job Summary for file - %s:%n", filePath);
                System.out.println(summary.toString() + "\n");
            } catch (Exception e) {
                String errorMsg = String.format("Calculation Failed for file %s: %s", filePath, e.toString());
                logger.warn(errorMsg, e);
                System.out.println(errorMsg);
            }
        }

    }
}
