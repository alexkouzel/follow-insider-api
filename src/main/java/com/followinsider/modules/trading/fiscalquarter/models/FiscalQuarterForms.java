package com.followinsider.modules.trading.fiscalquarter.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "fiscalQuarter")
@ToString(exclude = "fiscalQuarter")
@Table(name = "fiscal_quarter_forms")
public class FiscalQuarterForms {

    @Id
    private String id;

    private Integer total;

    private Integer loaded;

    private LocalDate lastUpdated;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private FiscalQuarter fiscalQuarter;

    public FiscalQuarterForms(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

}
