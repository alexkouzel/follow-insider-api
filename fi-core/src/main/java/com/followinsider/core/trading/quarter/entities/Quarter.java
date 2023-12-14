package com.followinsider.core.trading.quarter.entities;

import com.followinsider.common.entities.BaseEntity;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.entities.sync.Synchronizable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
public class Quarter extends BaseEntity implements Synchronizable {

    @Column(nullable = false, name = "year_val")
    private int year;

    @Column(nullable = false, name = "quarter_val")
    private int quarter;

    @Enumerated
    private SyncStatus syncStatus = SyncStatus.PENDING;

    private Integer formNum;

    public Quarter(QuarterVals vals) {
        this.year = vals.year();
        this.quarter = vals.quarter();
    }

    public QuarterVals getVals() {
        return new QuarterVals(year, quarter);
    }

}
