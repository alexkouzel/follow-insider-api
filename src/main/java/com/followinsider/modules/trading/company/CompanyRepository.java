package com.followinsider.modules.trading.company;

import com.followinsider.modules.trading.company.models.Company;
import com.followinsider.modules.trading.company.models.CompanyView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    @Query("SELECT c FROM Company c")
    List<CompanyView> findAllViews(Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.cik = :cik")
    CompanyView findViewsById(String cik);

    @Query("SELECT c.cik FROM Company c WHERE c.cik IN :ids")
    Set<String> findIdsPresentIn(Set<String> ids);

}
