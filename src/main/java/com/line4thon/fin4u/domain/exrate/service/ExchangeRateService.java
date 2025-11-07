package com.line4thon.fin4u.domain.exrate.service;

import com.line4thon.fin4u.domain.exrate.web.dto.ExchangeRateRes;

import java.util.List;


public interface ExchangeRateService {
    List<ExchangeRateRes> getExchangeRateGraphData();
    String getToastMessage(List<ExchangeRateRes.Exchange> list);
}
