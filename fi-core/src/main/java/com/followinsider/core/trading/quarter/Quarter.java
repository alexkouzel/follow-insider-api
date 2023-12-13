package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.BaseEntity;
import com.followinsider.common.entities.sync.SyncStatus;
import com.followinsider.common.entities.sync.Synchronizable;
import com.followinsider.common.entities.tuples.Tuple2;
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

    @Column(nullable = false)
    private int yearVal;

    @Column(nullable = false)
    private int quarterVal;

    @Enumerated
    private SyncStatus syncStatus = SyncStatus.PENDING;

    private Integer formNum;

    public Quarter(int yearVal, int quarterVal) {
        this.yearVal = yearVal;
        this.quarterVal = quarterVal;
    }

    public Quarter(Tuple2<Integer, Integer> vals) {
        this.yearVal = vals.first();
        this.quarterVal = vals.second();
    }

}
