package com.followinsider.secapi.forms.refs;

import com.followinsider.common.entities.TimeRange;
import com.followinsider.common.utils.ListUtils;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class FormRefUtils {

    public TimeRange getTimeRange(List<FormRef> refs) {
        Set<LocalDate> dates = refs.stream().map(FormRef::filedAt).collect(Collectors.toSet());
        return new TimeRange(dates);
    }

    public FormRef getFirst(List<FormRef> refs) {
        return Collections.min(refs, Comparator.comparing(FormRef::filedAt));
    }

    public FormRef getLast(List<FormRef> refs) {
        return Collections.max(refs, Comparator.comparing(FormRef::filedAt));
    }

    public static List<FormRef> filterAccNums(List<FormRef> refs, Set<String> accNums) {
        return ListUtils.filter(refs, ref -> !accNums.contains(ref.accNum()));
    }

    public static Set<String> getAccNums(List<FormRef> refs) {
        return refs.stream().map(FormRef::accNum).collect(Collectors.toSet());
    }

    public static List<FormRef> removeDups(List<FormRef> refs) {
        return ListUtils.removeDups(refs, FormRef::accNum);
    }

}
