package com.followinsider.core.service;

import com.followinsider.core.dto.FiscalQuarterStats;
import com.followinsider.core.repository.FormRepository;
import com.followinsider.loader.FormRefLoader;
import com.followinsider.parser.ref.FormRef;
import com.followinsider.common.util.DateUtil;
import com.followinsider.common.entity.container.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormService {

    private final FormRepository formRepository;

    private final FormRefLoader formRefLoader;

    public FiscalQuarterStats getQuarterStats(int year, int quarter) {
        List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
        Tuple2<Date, Date> timeSpan = getTimeSpanByRefs(refs);
        int savedNum = formRepository.countFiledBetween(timeSpan.first(), timeSpan.second());
        int totalNum = refs.size();
        return new FiscalQuarterStats(totalNum, savedNum);
    }

    public List<FormRef> filterOldRefs(List<FormRef> refs) {
        Tuple2<Date, Date> timePeriod = getTimeSpanByRefs(refs);
        Set<String> ids = formRepository.findIdsFiledBetween(timePeriod.first(), timePeriod.second());

        return refs.stream()
                .filter(ref -> !ids.contains(ref.getAccNum()))
                .collect(Collectors.toList());
    }

    private Tuple2<Date, Date> getTimeSpanByRefs(List<FormRef> refs) {
        Set<Date> dates = refs.stream().map(FormRef::getFiledAt).collect(Collectors.toSet());
        return DateUtil.getTimeSpan(dates);
    }

}
