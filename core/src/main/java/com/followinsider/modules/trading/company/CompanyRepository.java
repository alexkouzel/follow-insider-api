package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT c FROM Company c " +
            "WHERE str(c.cik) LIKE :text% " +
            "OR c.ticker LIKE :text% " +
            "OR UPPER(c.name) LIKE %:text%")
    List<CompanyView> findLike(String text, Pageable pageable);

    @Query("SELECT c FROM Company c")
    Page<CompanyView> findPage(Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.cik = :cik")
    CompanyView findViewById(int cik);

    @Query("SELECT c.cik FROM Company c WHERE c.cik IN :ids")
    Set<Integer> findIdsPresentIn(Set<Integer> ids);

}
