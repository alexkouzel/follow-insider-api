package com.followinsider.core.entity;

import com.followinsider.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"company", "insider"})
@ToString(callSuper = true, exclude = {"company", "insider"})
@Table(name = "position")
public class Position extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_cik")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insider_cik")
    private Insider insider;

    @ElementCollection
    private List<String> titles;

}
