package com.followinsider.core.repository;

import com.followinsider.core.entity.FailedFormRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FailedFormRefRepository extends JpaRepository<FailedFormRef, String> {

    @Query("SELECT r.accNum FROM FailedFormRef r")
    Set<String> findAllIds();

}
