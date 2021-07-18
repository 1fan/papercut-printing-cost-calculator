package com.papercut.service;

import java.math.BigDecimal;
import java.util.*;

import com.papercut.exception.UnSupportedTaskException;
import com.papercut.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CostCalculator {

    private Environment env;
    private final Map<String, BigDecimal> rates = new HashMap<>();
    private List<String> allowedPaperSize = null;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    public PrintJobSummary generateTotalPrintJobDetails(List<PrintJob> printJobs) throws UnSupportedTaskException {
        if(allowedPaperSize == null) {
            allowedPaperSize = env.getProperty("allowed.paper.size", List.class, Collections.singletonList("a4"));
        }
        PrintJobSummary summary = new PrintJobSummary();
        for (PrintJob job : printJobs) {
            if(allowedPaperSize.contains(job.getPaperSize().name().toLowerCase())) {
                PrintJobDetails detail = generatePrintJobDetail(job);
                summary.addPrintJobDetails(detail);
            } else {
                throw new UnSupportedTaskException(String.format("Paper Size %s is not supported.", job.getPaperSize().name()));
            }
        }
        return summary;
    }

    private PrintJobDetails generatePrintJobDetail(PrintJob job) {
        PrintJobDetails details = new PrintJobDetails();
        details.setTotalPages(job.getTotalPages());
        details.setColourfulPages(job.getColourPages());
        details.setBlackWhitePages(job.getWhiteBlackPages());
        details.setPrintOption(job.getPrintSideOption().name());
        details.setPaperSize(job.getPaperSize().name());
        details.setBlackWhiteCost(calculateBlackWhiteCost(job));
        details.setColourfulCost(calculateColourfulCost(job));
        details.setTotalCost(details.getColourfulCost().add(details.getBlackWhiteCost()));
        return details;
    }

    private BigDecimal calculateBlackWhiteCost(PrintJob job) {
        BigDecimal rate;
        if (PrintSideOption.SINGLE_PAGE.equals(job.getPrintSideOption())) {
            rate = getPrintCostRateForJob(job, ColourType.BLACK_WHITE, 15);
        } else {
            rate = getPrintCostRateForJob(job, ColourType.BLACK_WHITE, 10);
        }
        return rate.multiply(BigDecimal.valueOf(job.getWhiteBlackPages()));
    }

    private BigDecimal calculateColourfulCost(PrintJob job) {
        BigDecimal rate;
        if (PrintSideOption.SINGLE_PAGE.equals(job.getPrintSideOption())) {
            rate = getPrintCostRateForJob(job, ColourType.COLOUR, 25);
        } else {
            rate = getPrintCostRateForJob(job, ColourType.COLOUR, 15);
        }
        return rate.multiply(BigDecimal.valueOf(job.getColourPages()));
    }

    private BigDecimal getPrintCostRateForJob(PrintJob job, ColourType colourType, int defaultVal) {
        String key = normaliseStr(String.format("%s.%s.%s.cent", job.getPaperSize().name(), job.getPrintSideOption(), colourType.name()));
        BigDecimal rate = rates.get(key);
        if (rate == null) {
            rate = new BigDecimal(env.getProperty(key, Integer.class, defaultVal));
            rates.put(key, rate);
        }
        return rate;
    }

    private String normaliseStr(String str) {
        return str.toLowerCase().replace("_", "");
    }
}
