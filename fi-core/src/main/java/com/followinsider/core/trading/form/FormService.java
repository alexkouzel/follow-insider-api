package com.followinsider.core.trading.form;

import com.followinsider.common.utils.ListUtils;
import com.followinsider.data.forms.refs.FormRefLoader;
import com.followinsider.data.forms.refs.FormRef;
import com.followinsider.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public String[] getQuarterRange(int year, int quarter) {
        List<FormRef> refs = formRefLoader.loadByQuarter(year, quarter);
        Date[] timeRange = getTimeRangeByRefs(refs);
        return DateUtils.formatDates(timeRange, "dd-MM-yyyy");
    }

    public List<FormRef> filterOldRefs(List<FormRef> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();

        Date[] timeRange = getTimeRangeByRefs(refs);
        Set<String> ids = formRepository.findIdsFiledBetween(timeRange[0], timeRange[1]);

        return refs.stream()
                .filter(ref -> !ids.contains(ref.accNum()))
                .collect(Collectors.toList());
    }

    private Date[] getTimeRangeByRefs(List<FormRef> refs) {
        Set<Date> dates = refs.stream().map(FormRef::filedAt).collect(Collectors.toSet());
        return DateUtils.getMinMax(dates);
    }

    public int count() {
        return (int) formRepository.count();
    }

}
