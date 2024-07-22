package com.followinsider.modules.trading.fiscalquarter;

import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarter;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterDto;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterRange;
import com.followinsider.modules.trading.fiscalquarter.models.FiscalQuarterVals;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FiscalQuarterService {

    private final FiscalQuarterRepository fiscalQuarterRepository;

    @Transactional
    @PostConstruct
    public void init() {
        if (fiscalQuarterRepository.count() != 0) return;

        LocalDate now = LocalDate.now();

        var from = new FiscalQuarterVals(2005, 1);
        var to = new FiscalQuarterVals(now.getYear(), now.get(IsoFields.QUARTER_OF_YEAR));

        List<FiscalQuarter> fiscalQuarters = new FiscalQuarterRange(from, to)
                .generate()
                .stream()
                .map(FiscalQuarter::new)
                .toList();

        fiscalQuarterRepository.saveAll(fiscalQuarters);
    }

    public List<FiscalQuarterDto> getAll() {
        return fiscalQuarterRepository.findAllDtos();
    }

    public FiscalQuarterDto getFirst() {
        return fiscalQuarterRepository.findFirstByOrderByYearAscQuarterAsc();
    }

    public FiscalQuarterDto getLast() {
        return fiscalQuarterRepository.findFirstByOrderByYearDescQuarterDesc();
    }

    public long count() {
        return fiscalQuarterRepository.count();
    }

    public long countYears() {
        return fiscalQuarterRepository.countYears();
    }

}
