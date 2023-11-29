package com.followinsider.core.entity;

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
    private InsiderForm form;

    @Column(nullable = false)
    private String securityTitle;

    @Column(nullable = false)
    private Double shareNum;

    @Column(nullable = false)
    private Double sharePrice;

    @Column(length = 1000)
    private String sharePriceFootnote;

    @Column(nullable = false)
    private Date executedAt;

    private Double sharesLeft;

    @Enumerated(EnumType.ORDINAL)
    private TradeType type;

}
