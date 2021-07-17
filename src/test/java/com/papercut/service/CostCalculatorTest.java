package com.papercut.service;

import java.util.Arrays;

import com.papercut.model.PrintJob;
import com.papercut.model.PrintJobSummary;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CostCalculatorTest {

    CostCalculator calculator = new CostCalculator();

    PrintJob onlyBlackAndWhiteSingle = new PrintJob(10, 0, false);
    PrintJob onlyColourSingle = new PrintJob(0, 20, false);
    PrintJob onlyBlackAndWhiteDouble = new PrintJob(30, 0, true);
    PrintJob onlyColourDouble = new PrintJob(0, 40, true);
    PrintJob mixtureOfBlackWhiteAndColourSingle = new PrintJob(50, 60, false);
    PrintJob mixtureOfBlackWhiteAndColourDouble = new PrintJob(70, 80, true);

    @Test
    void shouldCalculateCostProperly() {
        PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Arrays.asList(onlyBlackAndWhiteSingle, onlyColourSingle, onlyBlackAndWhiteDouble, onlyColourDouble, mixtureOfBlackWhiteAndColourSingle, mixtureOfBlackWhiteAndColourDouble));
        //todo: confirm total count, and check each PrintJobDetails
    }
}
