package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.BaseEntity;
import com.followinsider.core.trading.form.sync.SyncStatus;
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
public class FiscalQuarter extends BaseEntity {

    @Column(nullable = false)
    private int yearVal;

    @Column(nullable = false)
    private int quarterVal;

    @Enumerated
    private SyncStatus syncStatus = SyncStatus.PENDING;

    private Integer formNum;

    public FiscalQuarter(int yearVal, int quarterVal) {
        this.yearVal = yearVal;
        this.quarterVal = quarterVal;
    }

}
