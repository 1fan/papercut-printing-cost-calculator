package com.papercut.model;

import com.papercut.exception.InvalidFileException;

public class PrintJob {
    private int totalPages;
    private int colourPages;
    private int whiteBlackPages;
    private PrintSideOption printSideOption;
    private PaperSize paperSize = PaperSize.A4;

    public PrintJob(String csvRow) throws InvalidFileException {
        csvRow = csvRow.replaceAll("\\s+", "").toLowerCase(); //remove white space
        if (csvRow != null && csvRow.length() != 0 && csvRow.split(",").length == 3) {
            try {
                totalPages = Integer.parseInt(csvRow.split(",")[0]);
                colourPages = Integer.parseInt(csvRow.split(",")[1]);
                whiteBlackPages = totalPages - colourPages;
                printSideOption = Boolean.parseBoolean(csvRow.split(",")[2]) ? PrintSideOption.DOUBLE_PAGE : PrintSideOption.SINGLE_PAGE;
                if (!isPageNumberValid(totalPages)) {
                    throw new InvalidFileException(String.format("Invalid row: %s - Total Page number must be equals or greater than 0.", csvRow));
                }
                if (!isPageNumberValid(colourPages)) {
                    throw new InvalidFileException(String.format("Invalid row: %s - Colour Page number must be equals or greater than 0.", csvRow));
                }
                if (!isPageNumberValid(whiteBlackPages)) {
                    throw new InvalidFileException(String.format("Invalid row: %s - Black-White Page number must be equals or greater than 0.", csvRow));
                }
            } catch (NumberFormatException e) {
                throw new InvalidFileException(String.format("Invalid row: %s - Page number is not numeric.", csvRow));
            }
        } else {
            throw new InvalidFileException(String.format("CSV contains invalid row: %s. Please ensure it follows the format 'TotalPages(number >= 0), ColourPages(number >= 0), IsDoubleSided(true or false)'.", csvRow));
        }
    }

    public PrintJob(int totalPages, int colorPages, boolean isDoubleSided) throws InvalidFileException {
        this.totalPages = totalPages;
        this.colourPages = colorPages;
        this.whiteBlackPages = totalPages-colorPages;
        this.printSideOption = isDoubleSided ? PrintSideOption.DOUBLE_PAGE : PrintSideOption.SINGLE_PAGE;
        if (!isPageNumberValid(totalPages)) {
            throw new InvalidFileException(String.format("Total Page number %d is invalid - must be equals or greater than 0.", totalPages));
        }
        if (!isPageNumberValid(colourPages)) {
            throw new InvalidFileException(String.format("Colour Page number %d is invalid - must be equals or greater than 0.", colorPages));
        }
        if (!isPageNumberValid(whiteBlackPages)) {
            throw new InvalidFileException(String.format("Black-White Page number %d is invalid - must be equals or greater than 0.", whiteBlackPages));
        }
    }

    private boolean isPageNumberValid(int pageNumber) {
        return pageNumber >= 0;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getColourPages() {
        return colourPages;
    }

    public void setColourPages(int colourPages) {
        this.colourPages = colourPages;
    }

    public int getWhiteBlackPages() {
        return whiteBlackPages;
    }

    public void setWhiteBlackPages(int whiteBlackPages) {
        this.whiteBlackPages = whiteBlackPages;
    }

    public PrintSideOption getPrintSideOption() {
        return printSideOption;
    }

    public void setPrintSideOption(PrintSideOption printSideOption) {
        this.printSideOption = printSideOption;
    }

    public PaperSize getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        this.paperSize = paperSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrintJob)) {
            return false;
        }

        PrintJob printJob = (PrintJob) o;

        if (totalPages != printJob.totalPages) {
            return false;
        }
        if (colourPages != printJob.colourPages) {
            return false;
        }
        if (whiteBlackPages != printJob.whiteBlackPages) {
            return false;
        }
        if (printSideOption != printJob.printSideOption) {
            return false;
        }
        return paperSize == printJob.paperSize;
    }

    @Override
    public int hashCode() {
        int result = totalPages;
        result = 31 * result + colourPages;
        result = 31 * result + whiteBlackPages;
        result = 31 * result + (printSideOption != null ? printSideOption.hashCode() : 0);
        result = 31 * result + (paperSize != null ? paperSize.hashCode() : 0);
        return result;
    }
}
