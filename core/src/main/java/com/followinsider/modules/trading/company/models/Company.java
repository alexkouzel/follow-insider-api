package com.followinsider.modules.trading.company.models;

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
@Table(name = "company")
public class Company implements Identifiable<Integer> {

    @Id
    private int cik;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 10)
    private String ticker;

    private String exchange;

    @OneToMany(mappedBy = "company")
    private List<Form> forms;

    @Override
    public Integer getId() {
        return cik;
    }

}
