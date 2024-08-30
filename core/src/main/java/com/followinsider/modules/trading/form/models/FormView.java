package com.followinsider.modules.trading.form.models;

import com.followinsider.modules.trading.trade.models.BasicTradeView;

import java.util.List;

public interface FormView extends BasicFormView {

    List<BasicTradeView> getTrades();

}
