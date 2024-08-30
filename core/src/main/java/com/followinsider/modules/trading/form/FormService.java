package com.followinsider.modules.trading.form;

import com.followinsider.common.models.requests.GetPageRequest;
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

    public List<FormView> getPage(GetPageRequest getPageRequest) {
        Pageable pageable = PageRequest.of(getPageRequest.pageIdx(), getPageRequest.pageSize());
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
