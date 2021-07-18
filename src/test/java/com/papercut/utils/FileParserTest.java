package com.papercut.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.papercut.CsvTestHelper;
import com.papercut.exception.InvalidFileException;
import com.papercut.model.PrintJob;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FileParserTest {

    private final String header = "Total Pages, Color Pages, Double Sided";
    private Environment env = Mockito.mock(Environment.class);
    FileParser fileParser = new FileParser();
    String filePath = "";

    @BeforeMethod
    void setUp() {
        fileParser = new FileParser();
        Mockito.when(env.getProperty(Mockito.eq("csv.contains.header"), Mockito.eq(Boolean.class), Mockito.any())).thenReturn(true);
        Mockito.when(env.getProperty(Mockito.eq("csv.max.size.mb"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(50);
        fileParser.setEnv(env);
    }

    @AfterMethod
    void clearUp() {
        CsvTestHelper.deleteFile(filePath);
    }

    @Test
    void shouldParseEmptyCsvFile() {
        filePath = "empty-csv.csv";
        CsvTestHelper.populateCsvFileWithContent(Arrays.asList(), filePath);
        try {
            List<PrintJob> printJobs = fileParser.parsePrintJobFromCsv(filePath);
            Assert.assertTrue(printJobs.isEmpty());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    void shouldParseCsvFileWithValidEntriesWithHeader() {
        filePath = "one-job-csv.csv";
        String row = "25, 10, false";
        CsvTestHelper.populateCsvFileWithContent(Arrays.asList(header, row), filePath);
        try {
            List<PrintJob> printJobs = fileParser.parsePrintJobFromCsv(filePath);
            Assert.assertEquals(printJobs.size(), 1);
            PrintJob job = new PrintJob(row);
            Assert.assertEquals(printJobs.get(0), job);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    void shouldParseCsvFileWithValidEntriesWithoutHeader() {
        filePath = "one-job-csv.csv";
        String row = "25, 10, false";
        CsvTestHelper.populateCsvFileWithContent(Arrays.asList(row), filePath);
        Mockito.when(env.getProperty(Mockito.eq("csv.contains.header"), Mockito.eq(Boolean.class), Mockito.any())).thenReturn(false);
        try {
            List<PrintJob> printJobs = fileParser.parsePrintJobFromCsv(filePath);
            Assert.assertEquals(printJobs.size(), 1);
            PrintJob job = new PrintJob(row);
            Assert.assertEquals(printJobs.get(0), job);
        } catch (Exception e) {
            Assert.fail();
        }
    }


    @Test
    void shouldThrowInvalidFileExceptionWhenFileNotExisted() {
        filePath = "not-existed-file.csv";
        try {
            fileParser.parsePrintJobFromCsv(filePath);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidFileException);
        }
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenFileIsNotCSV() {
        filePath = "not-csv-file.txt";
        CsvTestHelper.populateCsvFileWithContent(Arrays.asList(header, "25, 10, false"), filePath);
        try {
            fileParser.parsePrintJobFromCsv(filePath);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidFileException);
        }
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenMandatoryFieldIsMissing() {
        String emptyTotalPage = ", 10, false";
        String missingTotalPage = "10, false";
        String emptyColourPage = "10, , false";
        String missingColourPage = "10, false";
        String emptyDoubleSided = "10, 10,";
        String missingDoubleSided = "10, 10";
        for (String row : Arrays.asList(emptyTotalPage, missingTotalPage, emptyColourPage, missingColourPage, emptyDoubleSided, missingDoubleSided)) {
            String filePath = "csv-with-invalid-entry.txt";
            CsvTestHelper.populateCsvFileWithContent(Arrays.asList(header, row), filePath);
            try {
                fileParser.parsePrintJobFromCsv(filePath);
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(e instanceof InvalidFileException);
            }
            CsvTestHelper.deleteFile(filePath);
        }
    }

    @Test
    void shouldThrowInvalidFieldExceptionWhenEntryValueIsInvalid() {
        String totalPageNotNumeric = "asd, 10, false";
        String colourPageNotNumeric = "10, qwe, false";
        String doubleSidedNotBoolean = "10, 10, 10";
        String colorPageGreaterThanTotalPages = "10, 20, true";
        for (String row : Arrays.asList(totalPageNotNumeric, colourPageNotNumeric, doubleSidedNotBoolean, colorPageGreaterThanTotalPages)) {
            String filePath = "csv-with-invalid-entry.txt";
            CsvTestHelper.populateCsvFileWithContent(Arrays.asList(header, row), filePath);
            try {
                fileParser.parsePrintJobFromCsv(filePath);
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(e instanceof InvalidFileException);
            }
            CsvTestHelper.deleteFile(filePath);
        }
    }

    @Test
    void shouldThrowInvalidFieldExceptionWhenEntryValueExceedLimit() {
        Mockito.when(env.getProperty(Mockito.eq("total.pages.max"), Mockito.anyString())).thenReturn("10000");
        Mockito.when(env.getProperty(Mockito.eq("total.pages.min"), Mockito.anyString())).thenReturn("0");
        Mockito.when(env.getProperty(Mockito.eq("color.pages.max"), Mockito.anyString())).thenReturn("10000");
        Mockito.when(env.getProperty(Mockito.eq("color.pages.min"), Mockito.anyString())).thenReturn("0");

        String totalPageGreaterThanMax = "10001, 10, false";
        String totalPageLessThanMin = "-1, 10, false";
        String colourPageGreaterThanMax = "10, 10001, false";
        String colourPageLessThanMin = "10, -1, false";

        for (String row : Arrays.asList(totalPageGreaterThanMax, totalPageLessThanMin, colourPageGreaterThanMax, colourPageLessThanMin)) {
            String filePath = "csv-with-invalid-entry.txt";
            CsvTestHelper.populateCsvFileWithContent(Arrays.asList(header, row), filePath);
            try {
                fileParser.parsePrintJobFromCsv(filePath);
                Assert.fail();
            } catch (Exception e) {
                Assert.assertTrue(e instanceof InvalidFileException);
            }
            CsvTestHelper.deleteFile(filePath);
        }
    }

    @Test
    void shouldThrowInvalidFileExceptionWhenFileExceedSizeRestriction() {
        Mockito.when(env.getProperty(Mockito.eq("csv.max.size.mb"), Mockito.eq(Integer.class), Mockito.any())).thenReturn(0);
        filePath = "one-job-csv.csv";
        String row = "25, 10, false";
        CsvTestHelper.populateCsvFileWithContent(Arrays.asList(row), filePath);
        Mockito.when(env.getProperty(Mockito.eq("csv.contains.header"), Mockito.eq(Boolean.class), Mockito.any())).thenReturn(false);
        try {
            List<PrintJob> printJobs = fileParser.parsePrintJobFromCsv(filePath);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidFileException);
        }
    }
}
