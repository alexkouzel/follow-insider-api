package com.followinsider.modules.trading.trade.models;

import com.followinsider.common.models.BaseEntity;
import com.followinsider.modules.trading.form.models.Form;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true, exclude = "form")
@ToString(callSuper = true, exclude = "form")
@Table(
    name = "trade",
    indexes = {
        @Index(name = "trade_acc_no", columnList = "acc_no"),
        @Index(name = "trade_executed_at", columnList = "executedAt DESC, acc_no"),
        @Index(name = "trade_type", columnList = "type")
    }
)
public class Trade extends BaseEntity {

    @Column(nullable = false)
    private String securityTitle;

    @Column(nullable = false)
    private double shareCount;

    // If null, then indicated in the footnotes
    private Double sharePrice;

    @Column(nullable = false)
    private LocalDate executedAt;

    // If null, then valueLeft is not null
    private Double sharesLeft;

    // If null, then sharesLeft is not null
    private Double valueLeft;

    @Enumerated(EnumType.ORDINAL)
    private TradeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_no")
    private Form form;

}
