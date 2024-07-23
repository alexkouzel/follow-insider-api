package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterFormsRepository fiscalQuarterFormsRepository;

    @Transactional
    @PostConstruct
    public void init() {
        if (fiscalQuarterRepository.count() != 0) return;

        List<FiscalQuarter> fiscalQuarters = generate();
        fiscalQuarters = fiscalQuarterRepository.saveAll(fiscalQuarters);

        List<FiscalQuarterForms> fiscalQuartersForms = generateForms(fiscalQuarters);
        fiscalQuarterFormsRepository.saveAll(fiscalQuartersForms);
    }

    public List<FiscalQuarterView> getAll() {
        return fiscalQuarterRepository.findAllViews();
    }

    public FiscalQuarterView getFirst() {
        return fiscalQuarterRepository.findFirstByOrderByYearAscQuarterAsc();
    }

    public FiscalQuarterView getLast() {
        return fiscalQuarterRepository.findFirstByOrderByYearDescQuarterDesc();
    }

    public long count() {
        return fiscalQuarterRepository.count();
    }

    public long countYears() {
        return fiscalQuarterRepository.countYears();
    }

    public List<FiscalQuarterFormsView> getFormLoaderProgress() {
        return fiscalQuarterFormsRepository.findAllViews();
    }

    private List<FiscalQuarterForms> generateForms(List<FiscalQuarter> fiscalQuarters) {
        return fiscalQuarters.stream().map(FiscalQuarterForms::new).toList();
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
