package com.followinsider.core.trading.form;

import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.utils.ListUtils;
import com.followinsider.core.trading.form.dto.FormDto;
import com.followinsider.core.trading.quarter.entities.QuarterVals;
import com.followinsider.forms.refs.FormRef;
import com.followinsider.forms.refs.FormRefLoader;
import com.followinsider.forms.refs.FormRefUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormService {

    private final FormRepository formRepository;

    private final FormRefLoader formRefLoader;

    private final ModelMapper modelMapper;

    public int countBetween(TimeRange timeRange) {
        return formRepository.countByFiledAtBetween(timeRange.from(), timeRange.to());
    }

    public int countBefore(Date date) {
        return formRepository.countByFiledAtBefore(date);
    }

    public int countAfter(Date date) {
        return formRepository.countByFiledAtAfter(date);
    }

    public FormDto getFirst() {
        return toDto(formRepository.findTopByOrderByFiledAtAsc());
    }

    public FormDto getQuarterFirst(String alias) {
        return getQuarterFirst(new QuarterVals(alias));
    }

    public FormDto getQuarterFirst(QuarterVals vals) {
        List<FormRef> refs = formRefLoader.loadByQuarter(vals.year(), vals.quarter());
        return new FormDto(FormRefUtils.getFirst(refs));
    }

    public FormDto getLast() {
        return toDto(formRepository.findTopByOrderByFiledAtDesc());
    }

    public int count() {
        return (int) formRepository.count();
    }

    public FormDto toDto(Form form) {
        return modelMapper.map(form, FormDto.class);
    }

    public List<FormRef> filterOldRefs(List<FormRef> refs) {
        if (ListUtils.isEmpty(refs)) return new ArrayList<>();

        TimeRange timeRange = FormRefUtils.getTimeRange(refs);
        Set<String> ids = formRepository.findIdsFiledBetween(timeRange.from(), timeRange.to());

        return ListUtils.filter(refs, ref -> !ids.contains(ref.accNum()));
    }

}
