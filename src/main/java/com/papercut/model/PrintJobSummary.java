package com.papercut.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.vandermeer.asciitable.AT_Context;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class PrintJobSummary extends PrintJobDetails{

    private List<PrintJobDetails> printJobDetails = new ArrayList<>();

    private final int costSmallestUnitDigit = 2;

    public List<PrintJobDetails> getPrintJobDetails() {
        return printJobDetails;
    }

    public void addPrintJobDetails(PrintJobDetails printJobDetail) {
        this.printJobDetails.add(printJobDetail);
        this.totalCost = this.totalCost.add(printJobDetail.getTotalCost());
        this.colourfulCost = this.colourfulCost.add(printJobDetail.getColourfulCost());
        this.blackWhiteCost = this.blackWhiteCost.add(printJobDetail.getBlackWhiteCost());
        this.totalPages += printJobDetail.getTotalPages();
        this.blackWhitePages += printJobDetail.getBlackWhitePages();
        this.colourfulPages += printJobDetail.getColourfulPages();
    }

    private String getCostInString(BigDecimal cost) {
        return cost.scaleByPowerOfTen(-1 * costSmallestUnitDigit).toString();
    }

    @Override
    public String toString() {
        AT_Context ctx = new AT_Context();
        ctx.setWidth(120);
        AsciiTable at = new AsciiTable(ctx);
        at.addRule();
        at.addRow("id", "Paper Size", "Side Option", "Total pages", "BlackWhite Pages", "Colorful Pages", "Total Cost", "BlackWhite Cost", "Colorful Cost");
        at.addRule();
        for (int i = 1; i <= this.printJobDetails.size(); i++) {
            PrintJobDetails details = this.printJobDetails.get(i - 1);
            at.addRow(i, details.getPaperSize(), details.getPrintOption(), details.getTotalPages(), details.getBlackWhitePages(),
                    details.getColourfulPages(), getCostInString(details.getTotalCost()), getCostInString(details.getBlackWhiteCost()), getCostInString(details.getColourfulCost()));
            at.addRule();
        }
        at.addRule();
        at.addRow("Summary", "/", "/", getTotalPages(), getBlackWhitePages(),
                getColourfulPages(), getCostInString(getTotalCost()), getCostInString(getBlackWhiteCost()), getCostInString(getColourfulCost()));
        at.addRule();
        at.setTextAlignment(TextAlignment.CENTER);
        return "Total print jobs: " + printJobDetails.size() + "\n" +
                at.render() + "\n" +
                "Total cost: " + getCostInString(totalCost) + "\n";

    }
}
