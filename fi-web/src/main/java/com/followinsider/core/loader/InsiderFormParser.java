package com.followinsider.core.loader;

import com.followinsider.common.entity.TradeType;
import com.followinsider.core.model.*;
import com.followinsider.secapi.form.f345.OwnershipDocument;
import com.followinsider.secapi.form.f345.OwnershipForm;
import com.followinsider.secapi.form.f345.footnote.Footnote;
import com.followinsider.secapi.form.f345.footnote.FootnoteContainer;
import com.followinsider.secapi.form.f345.footnote.FootnoteValue;
import com.followinsider.secapi.form.f345.owner.Issuer;
import com.followinsider.secapi.form.f345.owner.Relationship;
import com.followinsider.secapi.form.f345.owner.ReportingOwner;
import com.followinsider.secapi.form.f345.transaction.PostTransactionAmounts;
import com.followinsider.secapi.form.f345.transaction.TransactionCode;
import com.followinsider.secapi.form.f345.transaction.nonderivative.NonDerivativeTable;
import com.followinsider.secapi.form.f345.transaction.nonderivative.NonDerivativeTransaction;
import com.followinsider.secapi.form.f345.transaction.nonderivative.NonDerivativeTransactionAmounts;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class InsiderFormParser {

    public InsiderForm parseOwnershipDocument(OwnershipDocument doc) throws ParseException {
        OwnershipForm form = doc.getOwnershipForm();
        Company company = parseCompany(form);
        Insider insider = parseInsider(form, company);

        return InsiderForm.builder()
                .accNo(doc.getAccNo())
                .company(company)
                .insider(insider)
                .trades(parseTrades(form))
                .filedAt(doc.getFiledAt())
                .reportedAt(doc.getReportedAt())
                .acceptedAt(doc.getAcceptedAt())
                .txtUrl(doc.getTxtUrl())
                .xmlUrl(doc.getXmlUrl())
                .build();
    }

    private Company parseCompany(OwnershipForm form) {
        Issuer issuer = form.getIssuer();
        String cik = issuer.getIssuerCik();
        String name = issuer.getIssuerName();
        String symbol = issuer.getIssuerTradingSymbol();

        return new Company(cik, symbol, name, null);
    }

    private Insider parseInsider(OwnershipForm form, Company company) {
        ReportingOwner reportingOwner = form.getReportingOwner().get(0);

        // Parse insider name & CIK
        ReportingOwner.ID id = reportingOwner.getReportingOwnerId();
        String name = id.getRptOwnerName();
        String cik = id.getRptOwnerCik();

        // Parse relationship titles
        Relationship relationship = reportingOwner.getReportingOwnerRelationship();
        List<String> titles = parseTitles(relationship);

        Insider insider = new Insider(cik, name, null);
        Position position = new Position(company, insider, titles);

        List<Position> positions = List.of(position);
        insider.setPositions(positions);
        company.setPositions(positions);

        return insider;
    }

    private List<String> parseTitles(Relationship relationship) {
        List<String> titles = new ArrayList<>();
        if (relationship.isDirector()) titles.add("Director");
        if (relationship.isTenPercentOwner()) titles.add("10% Owner");
        if (relationship.isOfficer()) titles.add(relationship.getOfficerTitle());
        if (relationship.isOther()) titles.add(relationship.getOtherText());
        return titles;
    }

    private List<Trade> parseTrades(OwnershipForm form) {
        NonDerivativeTable table = form.getNonDerivativeTable();
        if (table == null || table.getNonDerivativeTransaction() == null) {
            return List.of();
        }
        Map<String, String> footnotes = extractFootnotes(form);
        return table
                .getNonDerivativeTransaction().stream()
                .map(transaction -> parseTransaction(transaction, footnotes))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Trade parseTransaction(NonDerivativeTransaction transaction, Map<String, String> footnotes) {
        TradeType tradeType = extractTradeType(transaction);
        if (tradeType == null) return null;

        NonDerivativeTransactionAmounts amounts = transaction.getTransactionAmounts();
        String securityTitle = transaction.getSecurityTitle().getValue();
        Date executedAt = transaction.getTransactionDate().getValue();
        Double shareNum = amounts.getTransactionShares().getValue();
        Double sharesLeft = getSharesLeft(transaction);

        // Extract shape price (if missing, then include a footnote)
        FootnoteValue<Double> sharePriceField = amounts.getTransactionPricePerShare();
        Double sharePrice = sharePriceField.getValue();
        String sharePriceFootnote = null;

        if (sharePrice == null) {
            String footnoteId = sharePriceField.getFootnoteId().getId();
            sharePriceFootnote = footnotes.get(footnoteId);
        }

        return Trade.builder()
                .securityTitle(securityTitle)
                .type(tradeType)
                .shareNum(shareNum)
                .sharesLeft(sharesLeft)
                .sharePrice(sharePrice)
                .sharePriceFootnote(sharePriceFootnote)
                .executedAt(executedAt)
                .build();
    }

    private TradeType extractTradeType(NonDerivativeTransaction transaction) {
        TransactionCode transactionCode = transaction.getTransactionCoding().getTransactionCode();
        return switch (transactionCode) {
            case PURCHASE -> TradeType.BUY;
            case SALE -> TradeType.SELL;
            default -> null;
        };
    }

    private Map<String, String> extractFootnotes(OwnershipForm form) {
        FootnoteContainer wrapper = form.getFootnotes();
        if (wrapper == null || wrapper.getFootnote() == null) {
            return Map.of();
        }
        Map<String, String> footnotes = new HashMap<>();
        for (Footnote footnote : wrapper.getFootnote()) {
            footnotes.put(footnote.getId(), footnote.getValue());
        }
        return footnotes;
    }

    private Double getSharesLeft(NonDerivativeTransaction transaction) {
        PostTransactionAmounts postAmounts = transaction.getPostTransactionAmounts();
        FootnoteValue<Double> sharesOwnedField = postAmounts.getSharesOwnedFollowingTransaction();

        if (sharesOwnedField != null) {
            return sharesOwnedField.getValue();
        }

        Double sharePrice = transaction
                .getTransactionAmounts()
                .getTransactionPricePerShare()
                .getValue();

        if (sharePrice == null) return null;

        Double valueLeft = postAmounts
                .getValueOwnedFollowingTransaction()
                .getValue();

        if (valueLeft == null) return null;

        return valueLeft / sharePrice;
    }

}
