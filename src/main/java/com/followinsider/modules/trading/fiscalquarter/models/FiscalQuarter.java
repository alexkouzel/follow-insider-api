package com.followinsider.modules.trading.fiscalquarter.models;

import com.followinsider.common.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "fiscal_quarter")
public class FiscalQuarter extends BaseEntity {

    @Column(nullable = false, name = "year_val")
    private int year;

    @Column(nullable = false, name = "quarter_val")
    private int quarter;

    public FiscalQuarter(FiscalQuarterVals vals) {
        this.year = vals.year();
        this.quarter = vals.quarter();
    }

    public FiscalQuarterVals getVals() {
        return new FiscalQuarterVals(year, quarter);
    }

}
