package com.followinsider.forms.refs;

import com.followinsider.common.entities.TimeRange;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class FormRefUtils {

    public TimeRange getTimeRange(List<FormRef> refs) {
        Date minDate = getFirst(refs).filedAt();
        Date maxDate = getLast(refs).filedAt();
        return new TimeRange(minDate, maxDate);
    }

    public FormRef getFirst(List<FormRef> refs) {
        return Collections.min(refs, (r1, r2) -> r1.filedAt().compareTo(r2.filedAt()));
    }

    public FormRef getLast(List<FormRef> refs) {
        return Collections.max(refs, (r1, r2) -> r1.filedAt().compareTo(r2.filedAt()));
    }

    public static List<FormRef> filterAccNums(List<FormRef> refs, Set<String> accNums) {
        return refs.stream()
                .filter(ref -> !accNums.contains(ref.accNum()))
                .collect(Collectors.toList());
    }

    public static Set<String> getAccNums(List<FormRef> refs) {
        return refs.stream().map(FormRef::accNum).collect(Collectors.toSet());
    }

}
