package com.followinsider.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = {"company", "insider", "trades"})
@ToString(exclude = {"company", "insider", "trades"})
@Table(name = "insider_forms")
public class InsiderForm {

    @Id
    @Column(length = 20)
    private String accNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @OneToMany(mappedBy = "form")
    private List<Trade> trades;

    private Date filedAt;

    private Date reportedAt;

    private Date acceptedAt;

    private String txtUrl;

    private String xmlUrl;

}
