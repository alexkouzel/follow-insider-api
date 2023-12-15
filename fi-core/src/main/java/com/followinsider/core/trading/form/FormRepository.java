package com.followinsider.core.trading.form;

import com.followinsider.core.trading.form.dtos.FormDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {

    @Query("SELECT f.accNum FROM Form f WHERE f.filedAt BETWEEN :date1 AND :date2")
    Set<String> findIdsFiledBetween(LocalDate date1, LocalDate date2);

    FormDto findTopByOrderByFiledAtDesc();

    FormDto findTopByOrderByFiledAtAsc();

    int countByFiledAtBetween(LocalDate date1, LocalDate date2);

    int countByFiledAtBefore(LocalDate date);

    int countByFiledAtAfter(LocalDate date);

}
