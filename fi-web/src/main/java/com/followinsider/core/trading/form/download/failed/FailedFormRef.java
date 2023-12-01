package com.followinsider.core.trading.form.download.failed;

import com.followinsider.parser.ref.FormRef;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "failed_form_ref")
public class FailedFormRef {

    @Id
    @Column(length = 20)
    private String accNum;

    @Column(nullable = false, length = 10)
    private String issuerCik;

    @Column(nullable = false)
    private Date filedAt;

    public FailedFormRef(FormRef ref) {
        this.accNum = ref.getAccNum();
        this.issuerCik = ref.getIssuerCik();
        this.filedAt = ref.getFiledAt();
    }

}
