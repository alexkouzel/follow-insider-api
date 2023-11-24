package com.followinsider.core.entity;

import com.followinsider.common.entity.BaseEntity;
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

    private String securityTitle;

    private Double shareNum;

    private Double sharePrice;

    @Column(length = 1000)
    private String sharePriceFootnote;

    private Double sharesLeft;

    private Date executedAt;

    @Enumerated(EnumType.ORDINAL)
    private TradeType type;

}
