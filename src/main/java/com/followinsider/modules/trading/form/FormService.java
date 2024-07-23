package com.followinsider.modules.trading.form;

import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<FormView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        return formRepository.findAllViews(pageable);
    }

    public int countBetween(LocalDate date1, LocalDate date2) {
        return formRepository.countByFiledAtBetween(date1, date2);
    }

    public int countBefore(LocalDate date) {
        return formRepository.countByFiledAtBefore(date);
    }

    public int countAfter(LocalDate date) {
        return formRepository.countByFiledAtAfter(date);
    }

}
