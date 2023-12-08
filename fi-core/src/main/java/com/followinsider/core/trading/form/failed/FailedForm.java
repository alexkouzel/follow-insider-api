package com.followinsider.core.trading.form.failed;

import com.followinsider.common.entities.BaseEntity;
import com.followinsider.parsing.refs.FormRef;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "form_failed")
public class FailedForm extends BaseEntity {

    @Column(length = 20)
    private String accNum;

    @Column(nullable = false, length = 10)
    private String issuerCik;

    @Column(nullable = false)
    private Date filedAt;

    @Column(nullable = false)
    private String error;

    public FailedForm(FormRef ref, String error) {
        this.accNum = ref.getAccNum();
        this.issuerCik = ref.getIssuerCik();
        this.filedAt = ref.getFiledAt();
        this.error = error;
    }

}
