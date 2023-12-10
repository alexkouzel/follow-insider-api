package com.followinsider.core.trading.trade;

import com.followinsider.common.entities.BaseEntity;
import com.followinsider.core.trading.form.Form;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true, exclude = "form")
@ToString(callSuper = true, exclude = "form")
@Table(name = "trade")
public class Trade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_no")
    private Form form;

    @Column(nullable = false)
    private String securityTitle;

    @Column(nullable = false)
    private Double shareNum;

    // if null, then indicated in the footnote
    private Double sharePrice;

    @Column(length = 1000)
    private String sharePriceFootnote;

    @Column(nullable = false)
    private Date executedAt;

    private Double sharesLeft;

    @Enumerated(EnumType.ORDINAL)
    private TradeType type;

}
