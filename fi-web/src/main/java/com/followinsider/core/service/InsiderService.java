package com.followinsider.core.service;

import com.followinsider.core.model.Insider;
import com.followinsider.core.repository.InsiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsiderService {

    private final InsiderRepository repository;

    public List<Insider> getAll() {
        return repository.findAll();
    }

    public Insider getByCik(String cik) {
        return repository.findById(cik).orElse(null);
    }

}
