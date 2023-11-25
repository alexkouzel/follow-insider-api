package com.followinsider.data.repository;

import com.followinsider.data.entity.Company;
import com.followinsider.data.entity.Insider;
import com.followinsider.data.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    Position findByCompanyAndInsider(Company company, Insider insider);

}
