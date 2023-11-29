package com.followinsider.core.repository;

import com.followinsider.core.entity.InsiderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface InsiderFormRepository extends JpaRepository<InsiderForm, String> {

    @Query("SELECT f.accNum FROM InsiderForm f WHERE f.filedAt BETWEEN :date1 AND :date2")
    Set<String> findIdsFiledBetween(Date date1, Date date2);

}
