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
    @Column(name = "fiscal_quarter_id")
    private String id;

    @OneToOne
    @JoinColumn(
            name = "fiscal_quarter_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private FiscalQuarter fiscalQuarter;

    private Integer total;

    private Integer loaded;

    public FiscalQuarterForms(FiscalQuarter fiscalQuarter) {
        this.id = fiscalQuarter.getId();
    }

    public boolean isFull() {
        return total != null && total.equals(loaded);
    }

}
