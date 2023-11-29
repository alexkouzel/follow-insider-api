package com.followinsider.core.service;

import com.followinsider.core.entity.Insider;
import com.followinsider.core.repository.InsiderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsiderService {

    private final InsiderRepository repository;

    public List<Insider> getAll() {
        return repository.findAll();
    }

    public Insider getByCik(String cik) {
        return repository.findById(cik).orElse(null);
    }

}
