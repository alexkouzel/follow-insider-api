package com.followinsider.modules.trading.insider.models;

import com.followinsider.common.models.Identifiable;
import com.followinsider.modules.trading.form.models.Form;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(exclude = "forms")
@ToString(exclude = "forms")
@Table(name = "insider")
public class Insider implements Identifiable<Integer> {

    @Id
    private int cik;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "insider")
    private List<Form> forms;

    @Override
    public Integer getId() {
        return cik;
    }

}
