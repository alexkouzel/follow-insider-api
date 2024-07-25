package com.followinsider.modules.trading.form;

import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.form.models.FormView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {

    @Query("SELECT f FROM Form f")
    List<FormView> findAllViews(Pageable pageable);

    List<FormView> findAllByCompanyCik(int cik);

    List<FormView> findAllByInsiderCik(int cik);

    @Query("SELECT f.accNo FROM Form f WHERE f.filedAt BETWEEN :date1 AND :date2")
    Set<String> findIdsFiledBetween(LocalDate date1, LocalDate date2);

    int countByFiledAtBetween(LocalDate date1, LocalDate date2);

    int countByFiledAtBefore(LocalDate date);

    int countByFiledAtAfter(LocalDate date);

}
