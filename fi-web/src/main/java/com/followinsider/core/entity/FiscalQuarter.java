package com.followinsider.core.entity;

import com.followinsider.common.entity.BaseEntity;
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
    private int yearVal;

    @Column(nullable = false)
    private int quarterVal;

    private Boolean downloaded;

    private Integer formNum;

    public FiscalQuarter(int year, int quarter) {
        this.yearVal = year;
        this.quarterVal = quarter;
    }

    public String getAlias() {
        return yearVal + "Q" + quarterVal;
    }

}
