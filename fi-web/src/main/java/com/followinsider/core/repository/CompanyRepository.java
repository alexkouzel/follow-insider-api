package com.followinsider.core.repository;

import com.followinsider.core.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    @Query("SELECT c.cik FROM Company c WHERE c.cik in :ciks")
    Set<String> findIdsByIdIn(Set<String> ciks);

}
