package com.followinsider.core.trading.company;

import com.followinsider.common.entity.Identifiable;
import com.followinsider.core.trading.form.Form;
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
public class Company implements Identifiable<String> {

    @Id
    @Column(length = 10)
    private String cik;

    @Column(length = 10)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "company")
    private List<Form> forms;

    @Override
    public String getId() {
        return cik;
    }

}
