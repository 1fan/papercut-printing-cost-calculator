package com.papercut.model;

public class PrintJob {
    private long colourPages;
    private long whiteBlackPages;
    private PaperSize paperSize = PaperSize.A4;

    public PrintJob(long totalPages, long colorPages, boolean isDoubleSided) {
        //todo: convert csv column field into the print job entity
    }

    public String getJobDetails() {
        //paper size
        //print option:
        //total pages:
        //black-white pages:
        //black-white price:
        //colorful pages:
        //colorful cost:
        //total price
        return null;
    }
}
