package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterFormsRepository fiscalQuarterFormsRepository;

    @PostConstruct
    public void init() {
        initFiscalQuarters();
    }

    @Transactional
    public void initFiscalQuarters() {
        if (fiscalQuarterRepository.count() != 0) return;

        List<FiscalQuarter> fiscalQuarters = generate();
        fiscalQuarters = fiscalQuarterRepository.saveAll(fiscalQuarters);

        log.info("Loaded fiscal quarters :: count: {}", fiscalQuarters.size());
    }

    @Transactional(readOnly = true)
    public List<FiscalQuarterView> getAll() {
        return fiscalQuarterRepository.findAllViews();
    }

    @Transactional(readOnly = true)
    public FiscalQuarterView getFirst() {
        return fiscalQuarterRepository.findFirstByOrderByYearAscQuarterAsc();
    }

    @Transactional(readOnly = true)
    public FiscalQuarterView getLast() {
        return fiscalQuarterRepository.findFirstByOrderByYearDescQuarterDesc();
    }

    @Transactional(readOnly = true)
    public long count() {
        return fiscalQuarterRepository.count();
    }

    @Transactional(readOnly = true)
    public long countYears() {
        return fiscalQuarterRepository.countYears();
    }

    @Transactional(readOnly = true)
    public List<FiscalQuarterFormsView> getFormLoaderProgress() {
        return fiscalQuarterFormsRepository.findAllViews();
    }

    @Transactional(readOnly = true)
    public Optional<FiscalQuarter> findByYearAndQuarter(int year, int quarter) {
        return fiscalQuarterRepository.findByYearAndQuarter(year, quarter);
    }

    @Transactional(readOnly = true)
    public FiscalQuarterForms findFormsByFiscalQuarter(FiscalQuarter fiscalQuarter) {
        return fiscalQuarterFormsRepository.findByFiscalQuarter(fiscalQuarter);
    }

    public void saveForms(FiscalQuarterForms forms) {
        fiscalQuarterFormsRepository.save(forms);
    }

    private List<FiscalQuarter> generate() {
        LocalDate now = LocalDate.now();

        var from = new FiscalQuarterVals(2005, 1);
        var to = new FiscalQuarterVals(now.getYear(), now.get(IsoFields.QUARTER_OF_YEAR));

        return new FiscalQuarterRange(from, to)
            .generate()
            .stream()
            .map(FiscalQuarter::new)
            .toList();
    }

}
