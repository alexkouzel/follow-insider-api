package com.followinsider.core.trading.form;

import com.followinsider.common.entities.Identifiable;
import com.followinsider.core.trading.company.Company;
import com.followinsider.core.trading.insider.Insider;
import com.followinsider.core.trading.trade.Trade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
    private String accNum;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    private List<Trade> trades;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @ElementCollection
    @CollectionTable(
            name = "insider_title",
            joinColumns = @JoinColumn(name = "acc_num")
    )
    private Set<String> insiderTitles;

    @Column(nullable = false)
    private LocalDate filedAt;

    @Column(nullable = false)
    private String xmlUrl;

    @Override
    public String getId() {
        return accNum;
    }

}
