package com.papercut;

import com.papercut.exception.UnSupportedTaskException;
import com.papercut.model.PrintJobSummary;
import com.papercut.service.CostCalculator;
import com.papercut.utils.FileParser;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CostCalculatorCliTest {
    CostCalculatorCli cli = new CostCalculatorCli();
    FileParser fileParser;
    CostCalculator calculator;

    @BeforeMethod
    void setUp() throws UnSupportedTaskException {
        fileParser = Mockito.mock(FileParser.class);
        calculator = Mockito.mock(CostCalculator.class);
        Mockito.when(calculator.generatePrintJobSummary(Mockito.any())).thenReturn(new PrintJobSummary());
        cli.setCalculator(calculator);
        cli.setFileParser(fileParser);
    }

    @Test
    void shouldHandleMultipleArgsProperly() {
        String filePath1 = "filePath1";
        String filePath2 = "filePath2";
        String filePath3 = "filePath3";
        String[] args = new String[]{filePath1, filePath2, filePath3};
        cli.run(args);
        try {
            Mockito.verify(fileParser, Mockito.times(1)).parsePrintJobFromCsv(Mockito.eq("filePath1"));
            Mockito.verify(fileParser, Mockito.times(1)).parsePrintJobFromCsv(Mockito.eq("filePath2"));
            Mockito.verify(fileParser, Mockito.times(1)).parsePrintJobFromCsv(Mockito.eq("filePath3"));
            Mockito.verify(calculator, Mockito.times(3)).generatePrintJobSummary(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldHandleZeroArgsProperly() {
        try {
            cli.run();
            Mockito.verify(fileParser, Mockito.times(0)).parsePrintJobFromCsv(Mockito.any());
            Mockito.verify(calculator, Mockito.times(0)).generatePrintJobSummary(Mockito.any());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
