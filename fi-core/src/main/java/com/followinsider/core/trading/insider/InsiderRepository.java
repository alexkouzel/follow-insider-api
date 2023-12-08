package com.followinsider.core.trading.insider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface InsiderRepository extends JpaRepository<Insider, String> {

    @Query("SELECT i.cik FROM Insider i WHERE i.cik IN :ids")
    Set<String> findIdsByIdIn(Set<String> ids);

}
