package com.followinsider.data.service;

import com.followinsider.data.entity.Company;
import com.followinsider.data.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public List<Company> getAll() {
        return repository.findAll();
    }

    public Company getByCik(String cik) {
        return repository.findById(cik).orElse(null);
    }

}
