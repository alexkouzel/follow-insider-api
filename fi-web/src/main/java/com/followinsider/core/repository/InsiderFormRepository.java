package com.followinsider.core.repository;

import com.followinsider.core.model.InsiderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsiderFormRepository extends JpaRepository<InsiderForm, String> {
}
