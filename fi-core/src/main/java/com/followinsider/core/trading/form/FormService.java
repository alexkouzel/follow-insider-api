package com.followinsider.core.trading.form;

import com.followinsider.parsing.refs.FormRef;
import com.followinsider.common.utils.DateUtils;
import com.followinsider.common.entities.Tuple2;
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

    public List<FormRef> filterOldRefs(List<FormRef> refs) {
        Tuple2<Date, Date> timePeriod = getTimeSpanByRefs(refs);
        Set<String> ids = formRepository.findIdsFiledBetween(timePeriod.first(), timePeriod.second());

        return refs.stream()
                .filter(ref -> !ids.contains(ref.getAccNum()))
                .collect(Collectors.toList());
    }

    private Tuple2<Date, Date> getTimeSpanByRefs(List<FormRef> refs) {
        Set<Date> dates = refs.stream().map(FormRef::getFiledAt).collect(Collectors.toSet());
        return DateUtils.getTimeSpan(dates);
    }

}
