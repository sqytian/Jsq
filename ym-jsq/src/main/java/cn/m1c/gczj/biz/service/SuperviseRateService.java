package cn.m1c.gczj.biz.service;

import java.math.BigDecimal;
import java.util.List;

import cn.m1c.frame.service.BaseService;
import cn.m1c.gczj.biz.model.SuperviseRate;

public interface SuperviseRateService extends BaseService {

	List<SuperviseRate> getRateByMoney(BigDecimal money);


}
