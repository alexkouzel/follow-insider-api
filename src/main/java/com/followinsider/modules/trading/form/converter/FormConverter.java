package com.followinsider.modules.trading.form.converter;

import com.alexkouzel.filing.type.f345.OwnershipDocument;
import com.followinsider.modules.trading.form.models.Form;

public interface FormConverter {

    Form convertToForm(OwnershipDocument doc);

}
