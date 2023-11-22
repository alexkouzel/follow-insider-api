package com.followinsider.secapi.forms.insider;

import com.followinsider.secapi.forms.f345.OwnershipDocument;
import com.followinsider.secapi.forms.f345.OwnershipForm;
import com.followinsider.secapi.forms.f345.footnote.Footnote;
import com.followinsider.secapi.forms.f345.footnote.FootnoteContainer;
import com.followinsider.secapi.forms.f345.footnote.FootnoteValue;
import com.followinsider.secapi.forms.f345.owner.Relationship;
import com.followinsider.secapi.forms.f345.owner.ReportingOwner;
import com.followinsider.secapi.forms.f345.transaction.PostTransactionAmounts;
import com.followinsider.secapi.forms.f345.transaction.TransactionCode;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTable;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTransaction;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTransactionAmounts;

import java.util.*;
import java.util.stream.Collectors;

public class InsiderFormParser {

    public static InsiderForm parseOwnershipDocument(OwnershipDocument doc) {
        OwnershipForm form = doc.getOwnershipForm();
        return InsiderForm.builder()
                .accNo(doc.getAccNo())
                .issuer(form.getIssuer())
                .insider(parseInsider(form))
                .trades(parseTrades(form))
                .reportedAt(doc.getReportedAt())
                .txtUrl(doc.getTxtUrl())
                .xmlUrl(doc.getXmlUrl())
                .build();
    }

    private static Insider parseInsider(OwnershipForm form) {
        ReportingOwner reportingOwner = form.getReportingOwner().get(0);
        Relationship relationship = reportingOwner.getReportingOwnerRelationship();
        ReportingOwner.ID id = reportingOwner.getReportingOwnerId();
        String cik = id.getRptOwnerCik();
        String name = id.getRptOwnerName();
        List<String> titles = parseInsiderTitles(relationship);
        return new Insider(cik, name, titles);
    }

    private static List<String> parseInsiderTitles(Relationship relationship) {
        List<String> titles = new ArrayList<>();
        if (relationship.isDirector()) titles.add("Director");
        if (relationship.isTenPercentOwner()) titles.add("10% Owner");
        if (relationship.isOfficer()) titles.add(relationship.getOfficerTitle());
        if (relationship.isOther()) titles.add(relationship.getOtherText());
        return titles;
    }

    private static List<InsiderTrade> parseTrades(OwnershipForm form) {
        NonDerivativeTable table = form.getNonDerivativeTable();
        if (table == null || table.getNonDerivativeTransaction() == null) return List.of();
        Map<String, String> footnotes = extractFootnotes(form);
        return table
                .getNonDerivativeTransaction().stream()
                .map(transaction -> InsiderFormParser.parseTransaction(transaction, footnotes))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static InsiderTrade parseTransaction(NonDerivativeTransaction transaction, Map<String, String> footnotes) {
        TransactionCode transactionCode = transaction.getTransactionCoding().getTransactionCode();
        InsiderTradeType tradeType = InsiderTradeType.fromTransactionCode(transactionCode);
        if (tradeType == InsiderTradeType.OTHER) return null;

        // Extract different transaction values
        NonDerivativeTransactionAmounts amounts = transaction.getTransactionAmounts();
        String securityTitle = transaction.getSecurityTitle().getValue();
        Date executedAt = transaction.getTransactionDate().getValue();
        Double shareNum = amounts.getTransactionShares().getValue();
        Double sharesOwned = extractSharesOwned(transaction);

        // Extract share price (if missing, then add a footnote)
        FootnoteValue<Double> sharePriceField = amounts.getTransactionPricePerShare();
        Double sharePrice = sharePriceField.getValue();
        String sharePriceFootnote = null;
        if (sharePrice == null) {
            String footnoteId = sharePriceField.getFootnoteId().getId();
            sharePriceFootnote = footnotes.get(footnoteId);
        }
        return InsiderTrade.builder()
                .securityTitle(securityTitle)
                .type(tradeType)
                .shareNum(shareNum)
                .sharesOwned(sharesOwned)
                .sharePrice(sharePrice)
                .sharePriceFootnote(sharePriceFootnote)
                .executedAt(executedAt)
                .build();
    }

    private static Map<String, String> extractFootnotes(OwnershipForm form) {
        FootnoteContainer wrapper = form.getFootnotes();
        if (wrapper == null || wrapper.getFootnote() == null) return Map.of();
        Map<String, String> footnotes = new HashMap<>();
        for (Footnote footnote : wrapper.getFootnote()) {
            footnotes.put(footnote.getId(), footnote.getValue());
        }
        return footnotes;
    }

    private static Double extractSharesOwned(NonDerivativeTransaction transaction) {
        PostTransactionAmounts postAmounts = transaction.getPostTransactionAmounts();
        FootnoteValue<Double> sharesOwnedField = postAmounts
                .getSharesOwnedFollowingTransaction();

        if (sharesOwnedField == null) {
            Double sharePrice = transaction
                    .getTransactionAmounts()
                    .getTransactionPricePerShare()
                    .getValue();

            return (sharePrice == null) ? null : postAmounts
                    .getValueOwnedFollowingTransaction()
                    .getValue() / sharePrice;
        }
        return sharesOwnedField.getValue();
    }

}
