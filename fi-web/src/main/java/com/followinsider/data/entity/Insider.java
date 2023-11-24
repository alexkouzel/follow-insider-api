package com.followinsider.data.entity;

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
@Table(name = "insider")
public class Insider {

    @Id
    @Column(length = 10)
    private String cik;

    private String name;

    @OneToMany(mappedBy = "insider")
    private List<Position> positions;

}
