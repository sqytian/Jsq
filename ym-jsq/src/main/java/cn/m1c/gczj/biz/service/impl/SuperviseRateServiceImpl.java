package cn.m1c.gczj.biz.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.gczj.biz.dao.SuperviseRateDao;
import cn.m1c.gczj.biz.model.SuperviseRate;
import cn.m1c.gczj.biz.service.SuperviseRateService;

@Service("superviseRateService")
public class SuperviseRateServiceImpl extends BaseServiceImpl implements SuperviseRateService {
	
	@Resource
	private SuperviseRateDao superviseRateDao;

	@Override
	public IBaseDao getDao() {
		return superviseRateDao;
	}

	@Override
	public List<SuperviseRate> getRateByMoney(BigDecimal money) {
		
		return superviseRateDao.getRateByMoney(money);
	}
	

}
