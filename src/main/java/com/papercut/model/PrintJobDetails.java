package com.papercut.model;

import java.math.BigDecimal;

public class PrintJobDetails {
    private String paperSize;
    private String printOption;
    private long totalPages;
    private long blackWhitePages;
    private long colourfulPages;
    private BigDecimal totalCost;
    private BigDecimal blackWhiteCost;
    private BigDecimal colourfulCost;

    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public String getPrintOption() {
        return printOption;
    }

    public void setPrintOption(String printOption) {
        this.printOption = printOption;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getBlackWhitePages() {
        return blackWhitePages;
    }

    public void setBlackWhitePages(long blackWhitePages) {
        this.blackWhitePages = blackWhitePages;
    }

    public long getColourfulPages() {
        return colourfulPages;
    }

    public void setColourfulPages(long colourfulPages) {
        this.colourfulPages = colourfulPages;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getBlackWhiteCost() {
        return blackWhiteCost;
    }

    public void setBlackWhiteCost(BigDecimal blackWhiteCost) {
        this.blackWhiteCost = blackWhiteCost;
    }

    public BigDecimal getColourfulCost() {
        return colourfulCost;
    }

    public void setColourfulCost(BigDecimal colourfulCost) {
        this.colourfulCost = colourfulCost;
    }

    @Override
    public String toString() {
        return "PrintJobDetail{" +
                "paperSize='" + paperSize + '\'' +
                ", printOption='" + printOption + '\'' +
                ", totalPages=" + totalPages +
                ", blackWhitePages=" + blackWhitePages +
                ", colourfulPages=" + colourfulPages +
                ", totalCost=" + totalCost +
                ", blackWhiteCost=" + blackWhiteCost +
                ", colourfulCost=" + colourfulCost +
                '}';
    }
}
