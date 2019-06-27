package cn.m1c.gczj.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.m1c.frame.dao.IBaseDao;
import cn.m1c.frame.page.PageBaseModel;
import cn.m1c.frame.service.impl.BaseServiceImpl;
import cn.m1c.frame.utils.UUIDGenerator;
import cn.m1c.gczj.biz.dao.RateDao;
import cn.m1c.gczj.biz.model.Rate;
import cn.m1c.gczj.biz.model.RateVo;
import cn.m1c.gczj.biz.service.RateService;

@Service("rateService")
public class RateServiceImpl extends BaseServiceImpl implements RateService {
	
	@Resource
	private RateDao rateDao;

	@Override
	public IBaseDao getDao() {
		return rateDao;
	}
	
	/**
	 * 通过项目id和区号查询费率
	 * @param formulaId  项目id
	 * @param areaCode   区号
	 * @return
	 */
	@Override
	public List<Rate> getRateByForidArea(Long formulaId, String areaCode) {
		return rateDao.getRateByForidArea(formulaId,areaCode);	
		 
	}

	/**
	 * 通过 项目id,公式等级对应的查询价格
	 * @param i  公式等级
	 * @param formulaId  项目id
	 */
	@Override
	public Long getPriceByLevelForidArea(int i,String formulaId) {
		return rateDao.getPriceByLevelForidArea(i,formulaId);
	}

	/**
	 * 造价公式列表
	 * @param areaCode  区号
	 * @param formulaName  咨询项目名称
	 * @param pageNum   页数，默认1
	 * @param pageSize  一页多少条，默认10
	 */
	@Override
	public Map<Integer, List<RateVo>> getRateVoList(Integer pageSize, Integer pageNum, String areaCode,
			String formulaName) {
		Map<Integer, List<RateVo>> map = new HashMap<Integer, List<RateVo>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where r.deleted = 0 ");
		if(areaCode!=null){
			sqlCondition.append(" and r.area_code = "+areaCode);	
		}
		if(formulaName!=null){
			sqlCondition.append(" and f.formula_name = "+"'"+formulaName+"'");	
		}
		page.setOrderBy(" order by r.updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<RateVo> rateVoList = rateDao.getRateVoList(page);
		map.put(page.getTotalCount(), rateVoList);
		return map;
	}

	/**
	 * 通过formulaId查询公式
	 * @param formulaId
	 */
	@Override
	public List<Rate> getRateByFormulaId(String formulaId) {
		return rateDao.getRateByFormulaId(formulaId);
	}

	@Override
	public List<Rate> getRateByFormulaIdAcrualMoney(String formulaId) {
		// TODO Auto-generated method stub
		return rateDao.getRateByFormulaIdAcrualMoney(formulaId);
	}

	/**
	 * 通过 项目id,公式等级对应的查询价格（核增额/核减额）
	 * @param i  公式等级
	 * @param formulaId  项目id
	 */
	@Override
	public Long getPriceByLevelForidAreaAcrualMoney(int i, String formulaId) {
		// TODO Auto-generated method stub
		return rateDao.getPriceByLevelForidAreaAcrualMoney(i,formulaId);
	}

	@Override
	public String updateRate(String id, String rate, Integer rateLevel, BigDecimal price, String areaCode,
			String formulaId, String remark) {
		//新建
		if(!StringUtils.hasText(id)){
			Rate rateTable = new Rate();
			id=UUIDGenerator.getUUID();
			rateTable.setId(id);
			rateTable.setRate(rate);
			rateTable.setRateLevel(rateLevel);
			rateTable.setPrice(price.longValue());
			rateTable.setAreaCode(areaCode);
			rateTable.setFormulaId(formulaId);
			rateTable.setRemark(remark);
			rateTable.setDeleted(false);
			rateTable.setUpdated(new Date());
			rateTable.setCreated(new Date());
			rateDao.insertSelective(rateTable);
		}else{	
			Rate rateTable = (Rate) rateDao.selectByPrimaryKey(id);
			rateTable.setId(id);
			rateTable.setRate(rate);
			rateTable.setRateLevel(rateLevel);
			rateTable.setPrice(price.longValue());
			rateTable.setAreaCode(areaCode);
			rateTable.setFormulaId(formulaId);
			rateTable.setRemark(remark);
			rateTable.setUpdated(new Date());
			rateDao.updateByPrimaryKeySelective(rateTable);
		}
		 return id;
	}

	@Override
	public void deletedRate(String id) {
		Rate rateTable = (Rate) rateDao.selectByPrimaryKey(id);
		rateTable.setDeleted(true);
		rateDao.updateByPrimaryKeySelective(rateTable);
		
	}

	@Override
	public Map<Integer, List<Rate>> getRateList(Integer pageSize, Integer pageNum, String formulaId) {
		Map<Integer, List<Rate>> map = new HashMap<Integer, List<Rate>>();
		// 分页
		PageBaseModel page = new PageBaseModel(pageNum, pageSize);
		StringBuffer sqlCondition = new StringBuffer("where deleted = 0 ");
		if(formulaId!=null){
			sqlCondition.append(" and formula_id = "+"'"+formulaId+"'");	
		}
		page.setOrderBy(" order by updated DESC ");
		page.setSqlCondition(sqlCondition.toString());
		List<Rate> rateList = rateDao.getRateList(page);
		map.put(page.getTotalCount(), rateList);
		return map;
	}

}
