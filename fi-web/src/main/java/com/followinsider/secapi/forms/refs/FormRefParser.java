package com.followinsider.secapi.forms.refs;

import java.text.ParseException;
import java.util.List;

public interface FormRefParser<T> {
    List<FormRef> parse(T t) throws ParseException;
}
