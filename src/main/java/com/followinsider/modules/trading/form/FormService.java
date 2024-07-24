package com.followinsider.modules.trading.form;

import com.alexkouzel.filing.FilingUrlBuilder;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    private static final int PAGE_SIZE = 20;

    public List<FormView> getPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return formRepository.findAllViews(pageable);
    }

    public List<FormView> getByCompanyCik(String cik) {
        return formRepository.findAllByCompanyCik(cik);
    }

    public List<FormView> getByInsiderCik(String cik) {
        return formRepository.findAllByInsiderCik(cik);
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

    public String getXmlUrl(Form form) {
        return FilingUrlBuilder.buildXmlUrl(
                form.getCompany().getCik(),
                form.getAccNo(),
                form.getXmlFilename());
    }

}
