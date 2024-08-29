package com.followinsider.modules.trading.form;

import com.followinsider.common.models.dtos.PageRequestDto;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    public List<FormView> getPage(PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(pageRequest.pageIdx(), pageRequest.pageSize());
        return formRepository.findPage(pageable).getContent();
    }

    public List<FormView> getByCompanyCik(int cik) {
        return formRepository.findAllByCompanyCik(cik);
    }

    public List<FormView> getByInsiderCik(int cik) {
        return formRepository.findAllByInsiderCik(cik);
    }

    public long count() {
        return formRepository.count();
    }

}
