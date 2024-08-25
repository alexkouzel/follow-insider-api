package com.followinsider.modules.trading.form.models;

import com.followinsider.common.entities.Identifiable;
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
@Table(name = "form", indexes = @Index(columnList = "filedAt"))
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @ElementCollection
    @CollectionTable(
            name = "insider_title",
            joinColumns = @JoinColumn(name = "acc_no")
    )
    private Set<String> insiderTitles;

    @Override
    public String getId() {
        return accNo;
    }

}
