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
@Table(name = "insiders")
public class Insider {

    @Id
    @Column(length = 10)
    private String cik;

    private String name;

    @OneToMany(mappedBy = "insider")
    private List<Position> positions;

}
