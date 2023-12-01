package com.followinsider.core.trading.company;

import com.followinsider.core.trading.company.CompanyRepository;
import com.followinsider.core.trading.company.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository repository;

    public List<Company> getAll() {
        return repository.findAll();
    }

    public Company getByCik(String cik) {
        return repository.findById(cik).orElse(null);
    }

}
