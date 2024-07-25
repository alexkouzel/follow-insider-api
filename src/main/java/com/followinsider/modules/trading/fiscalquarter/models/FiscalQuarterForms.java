package com.followinsider.modules.trading.fiscalquarter.models;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private FiscalQuarter fiscalQuarter;

    public FiscalQuarterForms(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public boolean isFull() {
        return total != null && total.equals(loaded);
    }

}
