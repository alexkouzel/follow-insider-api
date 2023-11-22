package com.followinsider.secapi.forms.f345;

import com.followinsider.secapi.forms.f345.footnote.*;
import com.followinsider.secapi.forms.f345.owner.Issuer;
import com.followinsider.secapi.forms.f345.owner.Relationship;
import com.followinsider.secapi.forms.f345.owner.ReportingOwner;
import com.followinsider.secapi.forms.f345.owner.Signature;
import com.followinsider.secapi.forms.f345.transaction.OwnershipNature;
import com.followinsider.secapi.forms.f345.transaction.PostTransactionAmounts;
import com.followinsider.secapi.forms.f345.transaction.Transaction;
import com.followinsider.secapi.forms.f345.transaction.TransactionCode;
import com.followinsider.secapi.forms.f345.transaction.derivative.DerivativeTable;
import com.followinsider.secapi.forms.f345.transaction.derivative.DerivativeTransaction;
import com.followinsider.secapi.forms.f345.transaction.derivative.DerivativeTransactionAmounts;
import com.followinsider.secapi.forms.f345.transaction.derivative.UnderlyingSecurity;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTable;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTransaction;
import com.followinsider.secapi.forms.f345.transaction.nonderivative.NonDerivativeTransactionAmounts;
import com.followinsider.secapi.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class OwnershipFormFactory {

    public static OwnershipForm build() throws ParseException {
        Date signatureDate = DateUtils.parse("2023-03-10", "yyyy-MM-dd");
        Signature signature = new Signature("Elon Musk", signatureDate);

        return OwnershipForm.builder()
                .schemaVersion("X0306")
                .documentType("4")
                .periodOfReport(DateUtils.parse("2023-03-08", "yyyy-MM-dd"))
                .notSubjectToSection16(false)
                .issuer(issuer())
                .reportingOwner(reportingOwner())
                .derivativeTable(derivativeTable())
                .nonDerivativeTable(nonDerivativeTable())
                .footnotes(footnotes())
                .remarks("")
                .ownerSignature(List.of(signature))
                .build();
    }

    private static NonDerivativeTable nonDerivativeTable() throws ParseException {
        return new NonDerivativeTable(List.of(nonDerivativeTransaction0()), null);
    }

    private static NonDerivativeTransaction nonDerivativeTransaction0() throws ParseException {
        NonDerivativeTransactionAmounts transactionAmounts = NonDerivativeTransactionAmounts.builder()
                .transactionShares(new FootnoteValue<>(5250.0))
                .transactionPricePerShare(new FootnoteValue<>(2.79))
                .transactionAcquiredDisposedCode(new FootnoteValue<>("A"))
                .build();

        PostTransactionAmounts postTransactionAmounts = new PostTransactionAmounts(
                new FootnoteValue<>(411056826.0), null);

        OwnershipNature ownershipNature = new OwnershipNature(
                new FootnoteValue<>("I"),
                new FootnoteValue<>("by Trust", "F1"));

        return NonDerivativeTransaction.builder()
                .securityTitle(new FootnoteValue<>("Common Stock"))
                .transactionDate(new FootnoteEdgarDate("2023-03-08"))
                .deemedExecutionDate(new FootnoteEdgarDate())
                .transactionCoding(new Transaction.Coding("4", TransactionCode.OPTION_EXERCISE, false, null))
                .transactionTimeliness(new FootnoteValue<>(""))
                .transactionAmounts(transactionAmounts)
                .postTransactionAmounts(postTransactionAmounts)
                .ownershipNature(ownershipNature)
                .build();
    }

    private static DerivativeTable derivativeTable() throws ParseException {
        return new DerivativeTable(List.of(derivativeTransaction0()), null);
    }

    private static DerivativeTransaction derivativeTransaction0() throws ParseException {
        DerivativeTransactionAmounts transactionAmounts = DerivativeTransactionAmounts.builder()
                .transactionShares(new FootnoteValue<>(5250.0))
                .transactionPricePerShare(new FootnoteValue<>(0.0))
                .transactionAcquiredDisposedCode(new FootnoteValue<>("D"))
                .build();

        UnderlyingSecurity underlyingSecurity = new UnderlyingSecurity(
                new FootnoteValue<>("Common Stock"),
                new FootnoteValue<>(5250.0), null);

        PostTransactionAmounts postTransactionAmounts = new PostTransactionAmounts(
                new FootnoteValue<>(0.0), null);

        OwnershipNature ownershipNature = new OwnershipNature(
                new FootnoteValue<>("D"), null);

        return DerivativeTransaction.builder()
                .securityTitle(new FootnoteValue<>("Non-Qualifed Stock Option (right to buy)"))
                .conversionOrExercisePrice(new FootnoteValue<>(2.79))
                .transactionDate(new FootnoteEdgarDate("2023-03-08"))
                .deemedExecutionDate(new FootnoteEdgarDate())
                .transactionCoding(new Transaction.Coding("4", TransactionCode.OPTION_EXERCISE, false, null))
                .transactionTimeliness(new FootnoteValue<>(""))
                .transactionAmounts(transactionAmounts)
                .exerciseDate(new FootnoteEdgarDate(new FootnoteID("F2"), null))
                .expirationDate(new FootnoteEdgarDate("2023-04-08"))
                .underlyingSecurity(underlyingSecurity)
                .postTransactionAmounts(postTransactionAmounts)
                .ownershipNature(ownershipNature)
                .build();
    }

    private static Issuer issuer() {
        Issuer issuer = new Issuer();
        issuer.setIssuerCik("0001318605");
        issuer.setIssuerName("Tesla, Inc.");
        issuer.setIssuerTradingSymbol("TSLA");
        return issuer;
    }

    private static List<ReportingOwner> reportingOwner() {
        ReportingOwner.ID id = new ReportingOwner.ID("0001494730", null, "Musk Elon");
        Relationship relationship = new Relationship(true, true, true, false, "CEO", null);
        ReportingOwner.Address address = new ReportingOwner.Address(
                "C/O TESLA, INC.", "1 TESLA ROAD", "AUSTIN", "TX", "78725", "", null);
        return List.of(new ReportingOwner(id, address, relationship));
    }

    private static FootnoteContainer footnotes() {
        return new FootnoteContainer(List.of(
                new Footnote("The Elon Musk Revocable...", "F1"),
                new Footnote("Effective April 8, 2013...", "F2"),
                new Footnote("Effective June 10, 2013...", "F3")));
    }

}
