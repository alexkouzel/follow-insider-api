package com.followinsider.core.trading.insider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsiderService {

    private final InsiderRepository insiderRepository;

}
