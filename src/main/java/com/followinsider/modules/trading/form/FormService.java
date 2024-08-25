package com.followinsider.modules.trading.form;

import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    private static final int MAX_PAGE_SIZE = 20;

    public List<FormView> getPage(int page, int pageSize) {
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize);
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
