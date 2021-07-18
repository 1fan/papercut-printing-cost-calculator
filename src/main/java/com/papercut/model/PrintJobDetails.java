package com.papercut.model;

import java.math.BigDecimal;

public class PrintJobDetails {
    protected String paperSize;
    protected String printOption;
    protected int totalPages = 0;
    protected int blackWhitePages = 0;
    protected int colourfulPages = 0;
    protected BigDecimal totalCost = BigDecimal.ZERO;
    protected BigDecimal blackWhiteCost = BigDecimal.ZERO;
    protected BigDecimal colourfulCost = BigDecimal.ZERO;

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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getBlackWhitePages() {
        return blackWhitePages;
    }

    public void setBlackWhitePages(int blackWhitePages) {
        this.blackWhitePages = blackWhitePages;
    }

    public int getColourfulPages() {
        return colourfulPages;
    }

    public void setColourfulPages(int colourfulPages) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrintJobDetails)) {
            return false;
        }

        PrintJobDetails details = (PrintJobDetails) o;

        if (getTotalPages() != details.getTotalPages()) {
            return false;
        }
        if (getBlackWhitePages() != details.getBlackWhitePages()) {
            return false;
        }
        if (getColourfulPages() != details.getColourfulPages()) {
            return false;
        }
        if (getPaperSize() != null ? !getPaperSize().equals(details.getPaperSize()) : details.getPaperSize() != null) {
            return false;
        }
        if (getPrintOption() != null ? !getPrintOption().equals(details.getPrintOption()) : details.getPrintOption() != null) {
            return false;
        }
        if (getTotalCost() != null ? !getTotalCost().equals(details.getTotalCost()) : details.getTotalCost() != null) {
            return false;
        }
        if (getBlackWhiteCost() != null ? !getBlackWhiteCost().equals(details.getBlackWhiteCost()) : details.getBlackWhiteCost() != null) {
            return false;
        }
        return getColourfulCost() != null ? getColourfulCost().equals(details.getColourfulCost()) : details.getColourfulCost() == null;
    }

    @Override
    public int hashCode() {
        int result = getPaperSize() != null ? getPaperSize().hashCode() : 0;
        result = 31 * result + (getPrintOption() != null ? getPrintOption().hashCode() : 0);
        result = 31 * result + getTotalPages();
        result = 31 * result + getBlackWhitePages();
        result = 31 * result + getColourfulPages();
        result = 31 * result + (getTotalCost() != null ? getTotalCost().hashCode() : 0);
        result = 31 * result + (getBlackWhiteCost() != null ? getBlackWhiteCost().hashCode() : 0);
        result = 31 * result + (getColourfulCost() != null ? getColourfulCost().hashCode() : 0);
        return result;
    }
}
