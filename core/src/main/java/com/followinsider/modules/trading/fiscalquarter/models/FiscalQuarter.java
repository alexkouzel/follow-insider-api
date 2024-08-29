package com.followinsider.modules.trading.fiscalquarter.models;

import com.followinsider.common.models.Identifiable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "forms")
@ToString(exclude = "forms")
@Table(name = "fiscal_quarter")
public class FiscalQuarter implements Identifiable<String> {

    @Id
    private String id;

    @Column(nullable = false, name = "year_val")
    private int year;

    @Column(nullable = false, name = "quarter_val")
    private int quarter;

    @OneToOne(mappedBy = "fiscalQuarter", cascade = CascadeType.ALL)
    private FiscalQuarterForms forms;

    public FiscalQuarter(FiscalQuarterVals vals) {
        this.id = vals.toAlias();
        this.year = vals.year();
        this.quarter = vals.quarter();
        this.forms = new FiscalQuarterForms(this);
    }

    public FiscalQuarterVals getVals() {
        return new FiscalQuarterVals(year, quarter);
    }

}
