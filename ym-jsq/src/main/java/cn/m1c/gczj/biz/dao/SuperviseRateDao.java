package cn.m1c.gczj.biz.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.gczj.biz.model.SuperviseRate;

public interface SuperviseRateDao extends IBaseDao {

	List<SuperviseRate> getRateByMoney(@Param("price")BigDecimal price);

}