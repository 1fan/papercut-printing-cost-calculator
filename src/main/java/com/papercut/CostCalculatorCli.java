package com.papercut;

import java.util.List;

import com.papercut.exception.InvalidFileException;
import com.papercut.model.PrintJob;
import com.papercut.model.PrintJobSummary;
import com.papercut.service.CostCalculator;
import com.papercut.utils.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CostCalculatorCli implements CommandLineRunner {

    private Environment env;
    private FileParser fileParser;
    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
    @Autowired
    public void setFileParser(FileParser fileParser) {
        this.fileParser = fileParser;
    }

    public static void main(String[] args) {
        SpringApplication.run(CostCalculatorCli.class, args);
    }

    /**
     * Read a CSV file in which each row defined a printing task, print the details of each task as well as the total cost.
     *
     * @param args - contain the filepath(s) of csv file(s) that need to be parsed.
     */
    @Override
    public void run(String... args) {
        CostCalculator calculator = new CostCalculator();
        for (String filePath : args) {
            try {
                List<PrintJob> jobs = fileParser.parsePrintJobFromCsv(filePath);
                PrintJobSummary summary = calculator.generateTotalPrintJobDetails(jobs);
                System.out.println(summary.toString());

            } catch (InvalidFileException e) {
                e.printStackTrace();
            }
        }
        System.exit(-1);
    }
}
