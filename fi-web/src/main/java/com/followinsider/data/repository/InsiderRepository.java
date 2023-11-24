package com.followinsider.data.repository;

import com.followinsider.data.entity.Insider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsiderRepository extends JpaRepository<Insider, String> {
}
