package com.followinsider.modules.trading.form.models;

import com.followinsider.common.models.Identifiable;
import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.trade.models.Trade;
import com.followinsider.modules.trading.insider.models.Insider;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = {"company", "insider", "trades"})
@ToString(exclude = {"company", "insider", "trades"})
@Table(
    name = "form",
    indexes = {
        @Index(name = "form_company_cik", columnList = "company_cik"),
        @Index(name = "form_insider_cik", columnList = "insider_cik"),
        @Index(name = "form_filed_at", columnList = "filedAt")
    }
)
public class Form implements Identifiable<String> {

    @Id
    @Column(length = 20)
    private String accNo;

    @Column(nullable = false)
    private LocalDate filedAt;

    @Column(nullable = false)
    private String xmlFilename;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    private Set<Trade> trades;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "insider_title",
        joinColumns = @JoinColumn(name = "acc_no"),
        indexes = @Index(name = "insider_title_acc_no", columnList = "acc_no")
    )
    private Set<String> insiderTitles;

    @Override
    public String getId() {
        return accNo;
    }

}
