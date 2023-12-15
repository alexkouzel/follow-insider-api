package com.followinsider.core.trading.form;

import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.core.trading.form.dtos.FormDto;
import com.followinsider.secapi.forms.refs.FormRef;
import com.followinsider.secapi.forms.refs.FormRefUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormService {

    private final FormRepository formRepository;

    public FormDto getFirst() {
        return formRepository.findTopByOrderByFiledAtAsc();
    }

    public FormDto getLast() {
        return formRepository.findTopByOrderByFiledAtDesc();
    }

    public int countAll() {
        return (int) formRepository.count();
    }

    public int countBetween(TimeRange timeRange) {
        return countBetween(timeRange.from(), timeRange.to());
    }

    public int countBetween(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) return countAll();
        if (date1 == null) return formRepository.countByFiledAtBefore(date2);
        if (date2 == null) return formRepository.countByFiledAtAfter(date1);
        return formRepository.countByFiledAtBetween(date1, date2);
    }

    public List<FormRef> filterOldRefs(List<FormRef> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();
        TimeRange timeRange = FormRefUtils.getTimeRange(refs);

        boolean oneByOne = (refs.size() < 10)
                        || (refs.size() < 50 && timeRange.days() > 90);
        return oneByOne
                ? filterOldRefsOneByOne(refs)
                : filterOldRefsByTimeRange(refs, timeRange);
    }

    private List<FormRef> filterOldRefsByTimeRange(List<FormRef> refs, TimeRange timeRange) {
        Set<String> ids = formRepository.findIdsFiledBetween(timeRange.from(), timeRange.to());
        return FormRefUtils.filterAccNums(refs, ids);
    }

    private List<FormRef> filterOldRefsOneByOne(List<FormRef> refs) {
        return ListUtils.filter(refs, ref -> !formRepository.existsById(ref.accNum()));
    }

}
