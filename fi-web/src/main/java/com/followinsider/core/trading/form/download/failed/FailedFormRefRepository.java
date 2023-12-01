package com.followinsider.core.trading.form.download.failed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FailedFormRefRepository extends JpaRepository<FailedFormRef, String> {

    @Query("SELECT r.accNum FROM FailedFormRef r")
    Set<String> findAllIds();

}
