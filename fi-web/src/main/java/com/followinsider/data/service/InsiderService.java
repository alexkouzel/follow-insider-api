package com.followinsider.data.service;

import com.followinsider.data.entity.Insider;
import com.followinsider.data.repository.InsiderRepository;
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

    public void saveNew(List<Insider> insiders) {
        for (Insider insider : insiders) {
            String id = insider.getCik();

            if (repository.existsById(id)) {

                Insider insiderRef = repository.getReferenceById(id);
                // TODO: Implement this.

            } else {
                repository.save(insider);
            }
        }
    }

}
