package com.followinsider.modules.trading.fiscalquarter.models;

import com.followinsider.common.entities.Identifiable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "fiscal_quarter")
public class FiscalQuarter implements Identifiable<String> {

    @Id
    private String id;

    @Column(nullable = false, name = "year_val")
    private int year;

    @Column(nullable = false, name = "quarter_val")
    private int quarter;

    public FiscalQuarter(FiscalQuarterVals vals) {
        this.id = vals.toAlias();
        this.year = vals.year();
        this.quarter = vals.quarter();
    }

    public FiscalQuarterVals getVals() {
        return new FiscalQuarterVals(year, quarter);
    }

}
