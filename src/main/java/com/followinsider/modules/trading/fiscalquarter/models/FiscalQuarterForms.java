package com.followinsider.modules.trading.fiscalquarter.models;

import com.followinsider.modules.trading.form.loader.FormLoaderStatus;
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
    private long id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "fiscal_quarter_id", referencedColumnName = "id")
    private FiscalQuarter fiscalQuarter;

    @Enumerated
    private FormLoaderStatus loaderStatus = FormLoaderStatus.PENDING;

    private Integer formCount;

    public FiscalQuarterForms(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

}
