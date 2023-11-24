package com.followinsider.core.repository;

import com.followinsider.core.entity.Insider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsiderRepository extends JpaRepository<Insider, String> {
}
