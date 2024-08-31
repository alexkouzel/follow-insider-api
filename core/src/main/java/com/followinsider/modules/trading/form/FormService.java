package com.followinsider.modules.trading.form;

import com.followinsider.common.models.requests.GetPageRequest;
import com.followinsider.modules.trading.form.models.Form;
import com.followinsider.modules.trading.form.models.FormView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    @Transactional(readOnly = true)
    public List<FormView> getPage(GetPageRequest getPageRequest) {
        Pageable pageable = getPageRequest.prepare();
        List<String> accNos = formRepository.findAccNosByPage(pageable);
        return formRepository.findByAccNos(accNos);
    }

    @Transactional(readOnly = true)
    public List<FormView> getByCompanyCik(int cik) {
        return formRepository.findAllByCompanyCik(cik);
    }

    @Transactional(readOnly = true)
    public List<FormView> getByInsiderCik(int cik) {
        return formRepository.findAllByInsiderCik(cik);
    }

    @Transactional(readOnly = true)
    public long count() {
        return formRepository.count();
    }

    @Transactional(readOnly = true)
    public Set<String> getIdsPresentIn(Set<String> accNos) {
        return formRepository.findIdsPresentIn(accNos);
    }

    @Transactional(readOnly = true)
    public Set<String> getIdsFiledBetween(LocalDate date1, LocalDate date2) {
        return formRepository.findIdsFiledBetween(date1, date2);
    }

    public void saveAll(List<Form> forms) {
        formRepository.saveAll(forms);
    }

}
