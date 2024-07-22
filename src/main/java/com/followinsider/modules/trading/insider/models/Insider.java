package com.followinsider.modules.trading.insider.models;

import com.followinsider.common.entities.Identifiable;
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
public class Insider implements Identifiable<String> {

    @Id
    @Column(length = 10)
    private String cik;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "insider")
    private List<Form> forms;

    @Override
    public String getId() {
        return cik;
    }

}
