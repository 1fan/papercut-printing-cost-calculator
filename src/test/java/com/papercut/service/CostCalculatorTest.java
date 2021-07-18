package com.papercut.service;

import java.math.BigDecimal;
import java.util.Arrays;

import com.papercut.model.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CostCalculatorTest {

    CostCalculator calculator = new CostCalculator();

    @Test
    void shouldCalculateCostProperlForGivenTestCsv() {
        PrintJob row1 = new PrintJob(25, 10, false);
        PrintJob row2 = new PrintJob(55, 13, true);
        PrintJob row3 = new PrintJob(502, 22, true);
        PrintJob row4 = new PrintJob(1, 0, false);
        PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Arrays.asList(row1, row2, row3, row4));
        Assert.assertEquals(summary.getTotalCost().divide(BigDecimal.valueOf(100), 2).toString(), "64.10");
        Assert.assertEquals(summary.getPrintJobDetails().size(), 4);

        PrintJobDetails row1Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost1 = BigDecimal.valueOf(15L).multiply(BigDecimal.valueOf(15));
        BigDecimal colourCost1 = BigDecimal.valueOf(10L).multiply(BigDecimal.valueOf(25));
        row1Detail.setTotalPages(25L);
        row1Detail.setColourfulPages(10L);
        row1Detail.setBlackWhitePages(15L);
        row1Detail.setBlackWhiteCost(blackWhiteCost1);
        row1Detail.setColourfulCost(colourCost1);
        row1Detail.setTotalCost(blackWhiteCost1.add(colourCost1));
        row1Detail.setPrintOption(JobType.SINGLE_PAGE.name());
        row1Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row2Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost2 = BigDecimal.valueOf(42L).multiply(BigDecimal.valueOf(10));
        BigDecimal colourCost2 = BigDecimal.valueOf(13L).multiply(BigDecimal.valueOf(20));
        row2Detail.setTotalPages(55L);
        row2Detail.setColourfulPages(13L);
        row2Detail.setBlackWhitePages(42L);
        row2Detail.setBlackWhiteCost(blackWhiteCost2);
        row2Detail.setColourfulCost(colourCost2);
        row2Detail.setTotalCost(blackWhiteCost2.add(colourCost2));
        row2Detail.setPrintOption(JobType.DOUBLE_PAGE.name());
        row2Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row3Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost3 = BigDecimal.valueOf(480).multiply(BigDecimal.valueOf(10));
        BigDecimal colourCost3 = BigDecimal.valueOf(22L).multiply(BigDecimal.valueOf(20));
        row3Detail.setTotalPages(502L);
        row3Detail.setColourfulPages(22L);
        row3Detail.setBlackWhitePages(480L);
        row3Detail.setBlackWhiteCost(blackWhiteCost3);
        row3Detail.setColourfulCost(colourCost3);
        row3Detail.setTotalCost(blackWhiteCost3.add(colourCost3));
        row3Detail.setPrintOption(JobType.DOUBLE_PAGE.name());
        row3Detail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails row4Detail = new PrintJobDetails();
        BigDecimal blackWhiteCost4 = BigDecimal.valueOf(1L).multiply(BigDecimal.valueOf(15));
        BigDecimal colourCost4 = BigDecimal.ZERO;
        row4Detail.setTotalPages(1L);
        row4Detail.setColourfulPages(0);
        row4Detail.setBlackWhitePages(1L);
        row4Detail.setBlackWhiteCost(blackWhiteCost4);
        row4Detail.setColourfulCost(colourCost4);
        row4Detail.setTotalCost(blackWhiteCost4.add(colourCost4));
        row4Detail.setPrintOption(JobType.SINGLE_PAGE.name());
        row4Detail.setPaperSize(PaperSize.A4.name());

        Assert.assertEquals(summary.getPrintJobDetails().get(0), row1Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(1), row2Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(2), row3Detail);
        Assert.assertEquals(summary.getPrintJobDetails().get(3), row4Detail);
    }

    @Test
    void shouldCalculateCostProperlyWhenEveryCaseMixed() {
        PrintJob onlyBlackAndWhiteSingle = new PrintJob(10, 0, false);
        PrintJob onlyColourSingle = new PrintJob(20, 20, false);
        PrintJob onlyBlackAndWhiteDouble = new PrintJob(30, 0, true);
        PrintJob onlyColourDouble = new PrintJob(40, 40, true);
        PrintJob mixtureOfBlackWhiteAndColourSingle = new PrintJob(60, 50, false);
        PrintJob mixtureOfBlackWhiteAndColourDouble = new PrintJob(70, 30, true);

        PrintJobSummary summary = calculator.generateTotalPrintJobDetails(Arrays.asList(onlyBlackAndWhiteSingle, onlyColourSingle, onlyBlackAndWhiteDouble, onlyColourDouble, mixtureOfBlackWhiteAndColourSingle, mixtureOfBlackWhiteAndColourDouble));
        //total price: 10*15 + 20*25 + 30*10 + 40*20 + (10*15 + 50*25) + (40*10 + 30*20)) = 4150 Cents
        Assert.assertEquals(summary.getTotalCost().longValue(), 4150L);
        Assert.assertEquals(summary.getPrintJobDetails().size(), 6);

        PrintJobDetails onlyBlackAndWhiteSingleJobDetail = new PrintJobDetails();
        BigDecimal onlyBlackAndWhiteSingleJobBlackWhiteCost = BigDecimal.valueOf(10L).multiply(BigDecimal.valueOf(15));
        BigDecimal onlyBlackAndWhiteSingleJobColourCost = BigDecimal.ZERO;
        onlyBlackAndWhiteSingleJobDetail.setTotalPages(10L);
        onlyBlackAndWhiteSingleJobDetail.setColourfulPages(0L);
        onlyBlackAndWhiteSingleJobDetail.setBlackWhitePages(10L);
        onlyBlackAndWhiteSingleJobDetail.setBlackWhiteCost(onlyBlackAndWhiteSingleJobBlackWhiteCost);
        onlyBlackAndWhiteSingleJobDetail.setColourfulCost(onlyBlackAndWhiteSingleJobColourCost);
        onlyBlackAndWhiteSingleJobDetail.setTotalCost(onlyBlackAndWhiteSingleJobBlackWhiteCost.add(onlyBlackAndWhiteSingleJobColourCost));
        onlyBlackAndWhiteSingleJobDetail.setPrintOption(JobType.SINGLE_PAGE.name());
        onlyBlackAndWhiteSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyColourSingleJobDetail = new PrintJobDetails();
        BigDecimal onlyColourSingleJobBlackWhiteCost = BigDecimal.ZERO;
        BigDecimal onlyColourSingleJobColourCost = BigDecimal.valueOf(20L).multiply(BigDecimal.valueOf(25));
        onlyColourSingleJobDetail.setTotalPages(20L);
        onlyColourSingleJobDetail.setColourfulPages(20L);
        onlyColourSingleJobDetail.setBlackWhitePages(0L);
        onlyColourSingleJobDetail.setBlackWhiteCost(onlyColourSingleJobBlackWhiteCost);
        onlyColourSingleJobDetail.setColourfulCost(onlyColourSingleJobColourCost);
        onlyColourSingleJobDetail.setTotalCost(onlyColourSingleJobBlackWhiteCost.add(onlyColourSingleJobColourCost));
        onlyColourSingleJobDetail.setPrintOption(JobType.SINGLE_PAGE.name());
        onlyColourSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyBlackAndWhiteDoubleJobDetail = new PrintJobDetails();
        BigDecimal onlyBlackAndWhiteDoubleJobBlackWhiteCost = BigDecimal.valueOf(30L).multiply(BigDecimal.valueOf(10));
        BigDecimal onlyBlackAndWhiteDoubleJobColourCost = BigDecimal.ZERO;
        onlyBlackAndWhiteDoubleJobDetail.setTotalPages(30L);
        onlyBlackAndWhiteDoubleJobDetail.setColourfulPages(0L);
        onlyBlackAndWhiteDoubleJobDetail.setBlackWhitePages(30L);
        onlyBlackAndWhiteDoubleJobDetail.setBlackWhiteCost(onlyBlackAndWhiteDoubleJobBlackWhiteCost);
        onlyBlackAndWhiteDoubleJobDetail.setColourfulCost(onlyBlackAndWhiteDoubleJobColourCost);
        onlyBlackAndWhiteDoubleJobDetail.setTotalCost(onlyBlackAndWhiteDoubleJobBlackWhiteCost.add(onlyBlackAndWhiteDoubleJobColourCost));
        onlyBlackAndWhiteDoubleJobDetail.setPrintOption(JobType.DOUBLE_PAGE.name());
        onlyBlackAndWhiteDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails onlyColourDoubleJobDetail = new PrintJobDetails();
        BigDecimal onlyColourDoubleJobBlackWhiteCost = BigDecimal.ZERO;
        BigDecimal onlyColourDoubleJobColourCost = BigDecimal.valueOf(40L).multiply(BigDecimal.valueOf(20));
        onlyColourDoubleJobDetail.setTotalPages(40L);
        onlyColourDoubleJobDetail.setColourfulPages(40L);
        onlyColourDoubleJobDetail.setBlackWhitePages(0L);
        onlyColourDoubleJobDetail.setBlackWhiteCost(onlyColourDoubleJobBlackWhiteCost);
        onlyColourDoubleJobDetail.setColourfulCost(onlyColourDoubleJobColourCost);
        onlyColourDoubleJobDetail.setTotalCost(onlyColourDoubleJobBlackWhiteCost.add(onlyColourDoubleJobColourCost));
        onlyColourDoubleJobDetail.setPrintOption(JobType.DOUBLE_PAGE.name());
        onlyColourDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails mixtureOfBlackWhiteAndColourSingleJobDetail = new PrintJobDetails();
        BigDecimal mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost = BigDecimal.valueOf(10L).multiply(BigDecimal.valueOf(15));
        BigDecimal mixtureOfBlackWhiteAndColourSingleJobColourCost = BigDecimal.valueOf(50L).multiply(BigDecimal.valueOf(25));
        mixtureOfBlackWhiteAndColourSingleJobDetail.setTotalPages(60L);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setColourfulPages(50L);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setBlackWhitePages(10L);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setBlackWhiteCost(mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setColourfulCost(mixtureOfBlackWhiteAndColourSingleJobColourCost);
        mixtureOfBlackWhiteAndColourSingleJobDetail.setTotalCost(mixtureOfBlackWhiteAndColourSingleJobBlackWhiteCost.add(mixtureOfBlackWhiteAndColourSingleJobColourCost));
        mixtureOfBlackWhiteAndColourSingleJobDetail.setPrintOption(JobType.SINGLE_PAGE.name());
        mixtureOfBlackWhiteAndColourSingleJobDetail.setPaperSize(PaperSize.A4.name());

        PrintJobDetails mixtureOfBlackWhiteAndColourDoubleJobDetail = new PrintJobDetails();
        BigDecimal mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost = BigDecimal.valueOf(40L).multiply(BigDecimal.valueOf(10));
        BigDecimal mixtureOfBlackWhiteAndColourDoubleJobColourCost = BigDecimal.valueOf(30L).multiply(BigDecimal.valueOf(20));
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setTotalPages(70L);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setColourfulPages(30L);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setBlackWhitePages(40L);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setBlackWhiteCost(mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setColourfulCost(mixtureOfBlackWhiteAndColourDoubleJobColourCost);
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setTotalCost(mixtureOfBlackWhiteAndColourDoubleJobBlackWhiteCost.add(mixtureOfBlackWhiteAndColourDoubleJobColourCost));
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setPrintOption(JobType.DOUBLE_PAGE.name());
        mixtureOfBlackWhiteAndColourDoubleJobDetail.setPaperSize(PaperSize.A4.name());

        Assert.assertEquals(summary.getPrintJobDetails().get(0), onlyBlackAndWhiteSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(1), onlyColourSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(2), onlyBlackAndWhiteDoubleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(3), onlyColourDoubleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(4), mixtureOfBlackWhiteAndColourSingleJobDetail);
        Assert.assertEquals(summary.getPrintJobDetails().get(5), mixtureOfBlackWhiteAndColourDoubleJobDetail);
    }
}
