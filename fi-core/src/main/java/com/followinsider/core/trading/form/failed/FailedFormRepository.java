package com.followinsider.core.trading.form.failed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedFormRepository extends JpaRepository<FailedForm, String> {
}
