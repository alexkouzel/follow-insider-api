package com.followinsider.data.entity;

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

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int quarter;

    private Integer formNum;

    private boolean fullyLoaded = false;

    public FiscalQuarter(int year, int quarter) {
        this.year = year;
        this.quarter = quarter;
    }

}
