package com.followinsider.core.trading.form.failed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FailedFormRepository extends JpaRepository<FailedForm, String> {

    @Query("SELECT f.accNum FROM FailedForm f")
    Set<String> findAllIds();

}
