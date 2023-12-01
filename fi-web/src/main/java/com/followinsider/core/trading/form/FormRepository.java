package com.followinsider.core.trading.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {

    @Query("SELECT f.accNum FROM Form f WHERE f.filedAt BETWEEN :date1 AND :date2")
    Set<String> findIdsFiledBetween(Date date1, Date date2);

    @Query("SELECT COUNT(f) FROM Form f WHERE f.filedAt BETWEEN :date1 AND :date2")
    int countFiledBetween(Date date1, Date date2);

}
