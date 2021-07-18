package com.papercut.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.papercut.exception.InvalidFileException;
import com.papercut.exception.UnSupportedTaskException;
import com.papercut.model.*;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CostCalculatorTest {

    private CostCalculator calculator;
    private Environment env = Mockito.mock(Environment.class);

    @BeforeMethod
    void setUp() {
        calculator = new CostCalculator();
        Mockito.when(env.<Integer>getProperty(Mockito.eq("a4.singlepage.blackwhite.cent"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(15);
        Mockito.when(env.<Integer>getProperty(Mockito.eq("a4.singlepage.colour.cent"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(25);
        Mockito.when(env.<Integer>getProperty(Mockito.eq("a4.doublepage.blackwhite.cent"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(10);
        Mockito.when(env.<Integer>getProperty(Mockito.eq("a4.doublepage.colour.cent"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(20);
        Mockito.when(env.getProperty(Mockito.eq("allowed.paper.size"), Mockito.eq(List.class), Mockito.any())).thenReturn(Collections.singletonList("a4"));
        calculator.setEnv(env);
    }

    @Test
    void shouldCalculateCostProperlForGivenTestCsv() throws InvalidFileException, UnSupportedTaskException {
        PrintJob row1 = new PrintJob(25, 10, false);
        PrintJob row2 = new PrintJob(55, 13, true);
        PrintJob row3 = new PrintJob(502, 22, true);
        PrintJob row4 = new PrintJob(1, 0, false);
        PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Arrays.asList(row1, row2, row3, row4));
        Assert.assertEquals(summary.getTotalCost().scaleByPowerOfTen(-2).toString(), "64.10");
        Assert.assertEquals(summary.getPrintJobDetails().size(), 4);

        PrintJobDetails row1Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost1 = BigDecimal.valueOf(15).multiply(BigDecimal.valueOf(15));
        BigDecimal colourCost1 = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(25));
        row1Detail.setTotalPages(25);
        row1Detail.setColourfulPages(10);
        row1Detail.setBlackWhitePages(15);
        row1Detail.setBlackWhiteCost(blackWhiteCost1);
        row1Detail.setColourfulCost(colourCost1);
        row1Detail.setTotalCost(blackWhiteCost1.add(colourCost1));
        row1Detail.setPrintOption(PrintSideOption.SINGLE_PAGE.name());
        row1Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row2Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost2 = BigDecimal.valueOf(42).multiply(BigDecimal.valueOf(10));
        BigDecimal colourCost2 = BigDecimal.valueOf(13).multiply(BigDecimal.valueOf(20));
        row2Detail.setTotalPages(55);
        row2Detail.setColourfulPages(13);
        row2Detail.setBlackWhitePages(42);
        row2Detail.setBlackWhiteCost(blackWhiteCost2);
        row2Detail.setColourfulCost(colourCost2);
        row2Detail.setTotalCost(blackWhiteCost2.add(colourCost2));
        row2Detail.setPrintOption(PrintSideOption.DOUBLE_PAGE.name());
        row2Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row3Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost3 = BigDecimal.valueOf(480).multiply(BigDecimal.valueOf(10));
        BigDecimal colourCost3 = BigDecimal.valueOf(22).multiply(BigDecimal.valueOf(20));
        row3Detail.setTotalPages(502);
        row3Detail.setColourfulPages(22);
        row3Detail.setBlackWhitePages(480);
        row3Detail.setBlackWhiteCost(blackWhiteCost3);
        row3Detail.setColourfulCost(colourCost3);
        row3Detail.setTotalCost(blackWhiteCost3.add(colourCost3));
        row3Detail.setPrintOption(PrintSideOption.DOUBLE_PAGE.name());
        row3Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row4Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost4 = BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(15));
        BigDecimal colourCost4 = BigDecimal.ZERO;
        row4Detail.setTotalPages(1);
        row4Detail.setColourfulPages(0);
        row4Detail.setBlackWhitePages(1);
        row4Detail.setBlackWhiteCost(blackWhiteCost4);
        row4Detail.setColourfulCost(colourCost4);
        row4Detail.setTotalCost(blackWhiteCost4.add(colourCost4));
        row4Detail.setPrintOption(PrintSideOption.SINGLE_PAGE.name());
        row4Detail.setPaperSize(PaperSize.A4.name());

        Assert.assertEquals(summary.getPrintJobDetails().get(0), row1Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(1), row2Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(2), row3Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(3), row4Detail);
    }

    @Test
    void shouldCalculateCostProperlyWhenEveryCaseMixed() throws InvalidFileException, UnSupportedTaskException {
        PrintJob onlyBlackAndWhiteSingle = new PrintJob(10, 0, false);
        PrintJob onlyColourSingle = new PrintJob(20, 20, false);
        PrintJob onlyBlackAndWhiteDouble = new PrintJob(30, 0, true);
        PrintJob onlyColourDouble = new PrintJob(40, 40, true);
        PrintJob mixtureOfBlackWhiteAndColourSingle = new PrintJob(60, 50, false);
        PrintJob mixtureOfBlackWhiteAndColourDouble = new PrintJob(70, 30, true);

        PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Arrays.asList(onlyBlackAndWhiteSingle, onlyColourSingle, onlyBlackAndWhiteDouble, onlyColourDouble, mixtureOfBlackWhiteAndColourSingle, mixtureOfBlackWhiteAndColourDouble));
        //total price: 10*15 + 20*25 + 30*10 + 40*20 + (10*15 + 50*25) + (40*10 + 30*20)) = 4150 Cents
        Assert.assertEquals(summary.getTotalCost().longValue(), 4150);
        Assert.assertEquals(summary.getPrintJobDetails().size(), 6);

        PrintJobDetails onlyBlackAndWhiteSingleJobDetail = new PrintJobDetails();
        BigDecimal onlyBlackAndWhiteSingleJobBlackWhiteCost = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(15));
        BigDecimal onlyBlackAndWhiteSingleJobColourCost = BigDecimal.ZERO;
        onlyBlackAndWhiteSingleJobDetail.setTotalPages(10);
        onlyBlackAndWhiteSingleJobDetail.setColourfulPages(0);
        onlyBlackAndWhiteSingleJobDetail.setBlackWhitePages(10);
        onlyBlackAndWhiteSingleJobDetail.setBlackWhiteCost(onlyBlackAndWhiteSingleJobBlackWhiteCost);
        onlyBlackAndWhiteSingleJobDetail.setColourfulCost(onlyBlackAndWhiteSingleJobColourCost);
        onlyBlackAndWhiteSingleJobDetail.setTotalCost(onlyBlackAndWhiteSingleJobBlackWhiteCost.add(onlyBlackAndWhiteSingleJobColourCost));
        onlyBlackAndWhiteSingleJobDetail.setPrintOption(PrintSideOption.SINGLE_PAGE.name());
        onlyBlackAndWhiteSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyColourSingleJobDetail = new PrintJobDetails();
        BigDecimal onlyColourSingleJobBlackWhiteCost = BigDecimal.ZERO;
        BigDecimal onlyColourSingleJobColourCost = BigDecimal.valueOf(20).multiply(BigDecimal.valueOf(25));
        onlyColourSingleJobDetail.setTotalPages(20);
        onlyColourSingleJobDetail.setColourfulPages(20);
        onlyColourSingleJobDetail.setBlackWhitePages(0);
        onlyColourSingleJobDetail.setBlackWhiteCost(onlyColourSingleJobBlackWhiteCost);
        onlyColourSingleJobDetail.setColourfulCost(onlyColourSingleJobColourCost);
        onlyColourSingleJobDetail.setTotalCost(onlyColourSingleJobBlackWhiteCost.add(onlyColourSingleJobColourCost));
        onlyColourSingleJobDetail.setPrintOption(PrintSideOption.SINGLE_PAGE.name());
        onlyColourSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyBlackAndWhiteDoubleJobDetail = new PrintJobDetails();
        BigDecimal onlyBlackAndWhiteDoubleJobBlackWhiteCost = BigDecimal.valueOf(30).multiply(BigDecimal.valueOf(10));
        BigDecimal onlyBlackAndWhiteDoubleJobColourCost = BigDecimal.ZERO;
        onlyBlackAndWhiteDoubleJobDetail.setTotalPages(30);
        onlyBlackAndWhiteDoubleJobDetail.setColourfulPages(0);
        onlyBlackAndWhiteDoubleJobDetail.setBlackWhitePages(30);
        onlyBlackAndWhiteDoubleJobDetail.setBlackWhiteCost(onlyBlackAndWhiteDoubleJobBlackWhiteCost);
        onlyBlackAndWhiteDoubleJobDetail.setColourfulCost(onlyBlackAndWhiteDoubleJobColourCost);
        onlyBlackAndWhiteDoubleJobDetail.setTotalCost(onlyBlackAndWhiteDoubleJobBlackWhiteCost.add(onlyBlackAndWhiteDoubleJobColourCost));
        onlyBlackAndWhiteDoubleJobDetail.setPrintOption(PrintSideOption.DOUBLE_PAGE.name());
        onlyBlackAndWhiteDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyColourDoubleJobDetail = new PrintJobDetails();
        BigDecimal onlyColourDoubleJobBlackWhiteCost = BigDecimal.ZERO;
        BigDecimal onlyColourDoubleJobColourCost = BigDecimal.valueOf(40).multiply(BigDecimal.valueOf(20));
        onlyColourDoubleJobDetail.setTotalPages(40);
        onlyColourDoubleJobDetail.setColourfulPages(40);
        onlyColourDoubleJobDetail.setBlackWhitePages(0);
        onlyColourDoubleJobDetail.setBlackWhiteCost(onlyColourDoubleJobBlackWhiteCost);
        onlyColourDoubleJobDetail.setColourfulCost(onlyColourDoubleJobColourCost);
        onlyColourDoubleJobDetail.setTotalCost(onlyColourDoubleJobBlackWhiteCost.add(onlyColourDoubleJobColourCost));
        onlyColourDoubleJobDetail.setPrintOption(PrintSideOption.DOUBLE_PAGE.name());
        onlyColourDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails mixtureOfBlackWhiteAndColourSingleJobDetail = new PrintJobDetails();
        BigDecimal mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(15));
        BigDecimal mixtureOfBlackWhiteAndColourSingleJobColourCost = BigDecimal.valueOf(50).multiply(BigDecimal.valueOf(25));
        mixtureOfBlackWhiteAndColourSingleJobDetail.setTotalPages(60);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setColourfulPages(50);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setBlackWhitePages(10);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setBlackWhiteCost(mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setColourfulCost(mixtureOfBlackWhiteAndColourSingleJobColourCost);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setTotalCost(mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost.add(mixtureOfBlackWhiteAndColourSingleJobColourCost));
        mixtureOfBlackWhiteAndColourSingleJobDetail.setPrintOption(PrintSideOption.SINGLE_PAGE.name());
        mixtureOfBlackWhiteAndColourSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails mixtureOfBlackWhiteAndColourDoubleJobDetail = new PrintJobDetails();
        BigDecimal mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost = BigDecimal.valueOf(40).multiply(BigDecimal.valueOf(10));
        BigDecimal mixtureOfBlackWhiteAndColourDoubleJobColourCost = BigDecimal.valueOf(30).multiply(BigDecimal.valueOf(20));
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setTotalPages(70);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setColourfulPages(30);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setBlackWhitePages(40);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setBlackWhiteCost(mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setColourfulCost(mixtureOfBlackWhiteAndColourDoubleJobColourCost);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setTotalCost(mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost.add(mixtureOfBlackWhiteAndColourDoubleJobColourCost));
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setPrintOption(PrintSideOption.DOUBLE_PAGE.name());
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        Assert.assertEquals(summary.getPrintJobDetails().get(0), onlyBlackAndWhiteSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(1), onlyColourSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(2), onlyBlackAndWhiteDoubleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(3), onlyColourDoubleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(4), mixtureOfBlackWhiteAndColourSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(5), mixtureOfBlackWhiteAndColourDoubleJobDetail);
    }

    @Test
    void shouldThrowUnSupportedTaskExceptionIfPaperSizeNotSupported() {
        Mockito.when(env.getProperty(Mockito.eq("allowed.paper.size"), Mockito.eq(List.class), Mockito.any())).thenReturn(Arrays.asList("a5"));
        try {
            PrintJob row1 = new PrintJob(25, 10, false);
            PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Collections.singletonList(row1));
            Assert.fail();
        } catch (Exception e) {
            //should throw UnSupportedTaskException since paper size is not supported
            Assert.assertTrue(e instanceof UnSupportedTaskException);
        }
    }
}
