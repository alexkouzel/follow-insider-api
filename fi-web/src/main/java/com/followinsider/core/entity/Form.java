package com.followinsider.core.entity;

import com.followinsider.common.entity.Identifiable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = {"company", "insider", "trades"})
@ToString(exclude = {"company", "insider", "trades"})
@Table(name = "form", indexes = @Index(columnList = "filedAt"))
public class Form implements Identifiable<String> {

    @Id
    @Column(length = 20)
    private String accNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @ElementCollection
    @CollectionTable(name = "insider_title", joinColumns = @JoinColumn(name = "acc_num"))
    private Set<String> titles;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    private List<Trade> trades;

    @Column(nullable = false)
    private Date filedAt;

    @Column(nullable = false)
    private String xmlUrl;

    @Override
    public String getId() {
        return accNum;
    }
}