package com.papercut.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintJobSummary {

    private List<PrintJobDetails> printJobDetails = new ArrayList<>();
    private BigDecimal totalCost = BigDecimal.ZERO;

    public List<PrintJobDetails> getPrintJobDetails() {
        return printJobDetails;
    }

    public void addPrintJobDetails(PrintJobDetails printJobDetail) {
        this.printJobDetails.add(printJobDetail);
        this.totalCost = this.totalCost.add(printJobDetail.getTotalCost());
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
}
