package com.followinsider.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "positions")
@ToString(exclude = "positions")
@Table(name = "companies")
public class Company {

    @Id
    @Column(length = 10)
    private String cik;

    @Column(length = 10)
    private String symbol;

    private String name;

    @OneToMany(mappedBy = "company")
    private List<Position> positions;

}
