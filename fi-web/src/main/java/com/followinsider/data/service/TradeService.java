package com.followinsider.data.service;

import com.followinsider.data.entity.Trade;
import com.followinsider.data.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository repository;

    public List<Trade> getAll() {
        return repository.findAll();
    }

    public Trade getById(int id) {
        return repository.findById(id).orElse(null);
    }

}
