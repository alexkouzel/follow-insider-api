package com.followinsider.modules.trading.insider;

import com.followinsider.modules.trading.insider.models.Insider;
import com.followinsider.modules.trading.insider.models.InsiderView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InsiderRepository extends JpaRepository<Insider, String> {

    @Query("SELECT i FROM Insider i")
    List<InsiderView> findAllViews(Pageable pageable);

    @Query("SELECT i FROM Insider i WHERE i.cik = :cik")
    InsiderView findViewById(String cik);

    @Query("SELECT i.cik FROM Insider i WHERE i.cik IN :ids")
    Set<String> findIdsPresentIn(Set<String> ids);

}
